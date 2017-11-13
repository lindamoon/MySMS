package com.lixb.mysms.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lixb.mysms.R;
import com.lixb.mysms.base.App;
import com.lixb.mysms.db.greendao.RepeatTaskInfoDao;
import com.lixb.mysms.db.greendao.TaskDao;
import com.lixb.mysms.entity.RepeatTaskInfo;
import com.lixb.mysms.entity.Task;
import com.lixb.mysms.entity.enums.TaskStatus;
import com.lixb.mysms.entity.enums.TaskStatusConverter;
import com.lixb.mysms.third.PullableListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskListActivity extends AppCompatActivity {

    private static final int MSG_QUERY_TASKS = 1;
    private static final int MSG_REFRESH = 2;
    @BindView(R.id.rv_tasks)
    PullableListView mRvTasks;
    @BindView(R.id.iv_add_task)
    ImageView mIvAddTask;
    @BindView(R.id.tv_cur_socre)
    TextView mTvCurSocre;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    //更新列表
                    mListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private List<Task> mTasks;
    private TaskListAdapter mListAdapter;
    private TaskStatusConverter mTaskStatusConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initListView();
        mTaskStatusConverter = new TaskStatusConverter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshDataAndUI();
    }

    private void initListView() {
        mListAdapter = new TaskListAdapter();
        mRvTasks.setAdapter(mListAdapter);
        mRvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(TaskListActivity.this, TaskDetailActivity.class).putExtra("taskIdStr", mTasks.get(position).getTaskIdStr()));
            }
        });
    }

    @OnClick(R.id.iv_add_task)
    public void onViewClicked() {
        startActivity(new Intent(this, AddTaskActivity.class));
    }

    class TaskListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return null == mTasks ? 0 : mTasks.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (null == convertView) {
                view = LayoutInflater.from(TaskListActivity.this).inflate(R.layout.item_task, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvTaskInfo.setText(mTasks.get(position).getTitle());
            return view;
        }


        class ViewHolder {
            @BindView(R.id.tv_task_info)
            TextView mTvTaskInfo;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    /**
     * 刷新页面
     */
    private void refreshDataAndUI() {
        App.getApplication().getDaoSession().clear();
        if (null != mTasks) {
            mTasks.clear();
        } else {
            mTasks = new ArrayList<>();
        }
        //把除了已关闭的任务列举出来
        List<Task> tasks = App.getApplication().getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.TaskStatus.notEq(mTaskStatusConverter.convertToDatabaseValue(TaskStatus.CLOSED))).list();
        Date today = new Date();
        for (Task task : tasks) {
            RepeatTaskInfo repeatInfo = App.getApplication().getDaoSession().getRepeatTaskInfoDao().queryBuilder().where(RepeatTaskInfoDao.Properties.TaskIdStr.eq(task.getTaskIdStr())).unique();
            String repeatStrategy = repeatInfo.getRepeatStrategy();
            Date lastCompletedDate = repeatInfo.getLastCompletedDate();
            switch (repeatInfo.getRepeatMode()) {
                case NOREPEAT:
                    if (!task.getTaskStatus().equals(TaskStatus.FINISHED)) {
                        mTasks.add(task);
                    }
                    break;
                case EVERYDAY:
                    int offsetDay = Integer.parseInt(repeatStrategy);
                    if (null == lastCompletedDate) {
                        mTasks.add(task);
                    } else {
                        int dateLast = lastCompletedDate.getDate();
                        int dateNow = today.getDate();
                        if (Math.abs(dateNow - dateLast) > offsetDay) {
                            mTasks.add(task);
                        }
                    }
                    break;
                case EVERYWEEK:
                    String todayweek = getWeekStr(today);
                    if (!TextUtils.isEmpty(repeatStrategy)) {
                        if (lastCompletedDate == null && repeatStrategy.contains(todayweek)) {
                            mTasks.add(task);
                        } else if (lastCompletedDate != null) {
                            LogUtils.d(task.getTitle().concat("**").concat(repeatStrategy).concat("todayweek:").concat(todayweek).concat("lastCompletedDate:").concat(TimeUtils.date2String(lastCompletedDate)));
                            if (repeatStrategy.contains(todayweek) && lastCompletedDate.getDate() != today.getDate()) {
                                mTasks.add(task);
                            }
                        }

                    }
                    break;
                case EVERYMONTH:
                    int date = Integer.parseInt(repeatStrategy);
                    if (null == lastCompletedDate && today.getDate() == date) {
                        mTasks.add(task);
                    }

                    if (null != lastCompletedDate && today.getDate() == date && today.getMonth() != lastCompletedDate.getMonth()) {
                        mTasks.add(task);
                    }

                    break;
                case EVERYYEAR:
                    String[] split = repeatStrategy.split("-");
                    int month = Integer.valueOf(split[0]);
                    int day = Integer.valueOf(split[1]);

                    if (month == today.getMonth() && day == today.getDate() && lastCompletedDate == null) {
                        mTasks.add(task);
                    }
                    if (month == today.getMonth() && day == today.getDate() && lastCompletedDate != null && lastCompletedDate.getYear() != today.getYear()) {
                        mTasks.add(task);
                    }
                    break;
            }
        }

        mTvCurSocre.setText("当前积分:"+App.getApplication().getScore());
        mHandler.obtainMessage(MSG_REFRESH).sendToTarget();
    }

    private String getWeekStr(Date date) {
        String week = "";
        String chineseWeek = TimeUtils.getChineseWeek(date);
        if (chineseWeek.contains("一")) {
            week = "1";
        } else if (chineseWeek.contains("二")) {
            week = "2";
        } else if (chineseWeek.contains("三")) {
            week = "3";
        } else if (chineseWeek.contains("四")) {
            week = "4";
        } else if (chineseWeek.contains("五")) {
            week = "5";
        } else if (chineseWeek.contains("六")) {
            week = "6";
        } else if (chineseWeek.contains("日")) {
            week = "7";
        }

        return week;
    }

}
