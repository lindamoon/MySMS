package com.lixb.mysms.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lixb.mysms.R;
import com.lixb.mysms.base.App;
import com.lixb.mysms.biz.user.CurrentUser;
import com.lixb.mysms.db.greendao.RepeatTaskInfoDao;
import com.lixb.mysms.db.greendao.TaskDao;
import com.lixb.mysms.entity.DoHistory;
import com.lixb.mysms.entity.RepeatTaskInfo;
import com.lixb.mysms.entity.Task;
import com.lixb.mysms.entity.TaskComment;
import com.lixb.mysms.entity.enums.PriorityConverter;
import com.lixb.mysms.entity.enums.RepeatMode;
import com.lixb.mysms.entity.enums.RepeatModeConverter;
import com.lixb.mysms.entity.enums.TaskStatus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskDetailActivity extends AppCompatActivity {

    private static final int MSG_UPDATE_TASK_START_DATE = 1;
    private static final int MSG_UPDATE_TASK_DEADLINE = 2;
    private static final int MSG_UPDATE_REPEAT_YEAR = 3;
    private static final int MSG_REFRESH_COMMENT = 4;
    @BindView(R.id.tv_close)
    TextView mTvClose;
    @BindView(R.id.tv_completedTask)
    TextView mTvCompletedTask;
    @BindView(R.id.tv_add_task_title)
    TextView mTvAddTaskTitle;
    @BindView(R.id.et_task_title)
    EditText mEtTaskTitle;
    @BindView(R.id.et_task_desc)
    EditText mEtTaskDesc;
    @BindView(R.id.tv_priority)
    TextView mTvPriority;
    @BindView(R.id.spinner_priority)
    Spinner mSpinnerPriority;
    @BindView(R.id.tv_repeatmode)
    TextView mTvRepeatmode;
    @BindView(R.id.spinner_repeatmode)
    Spinner mSpinnerRepeatmode;
    @BindView(R.id.et_repeat_day)
    EditText mEtRepeatDay;
    @BindView(R.id.ll_repeat_day)
    LinearLayout mLlRepeatDay;
    @BindView(R.id.repeat_monday)
    CheckBox mRepeatMonday;
    @BindView(R.id.repeat_tuesday)
    CheckBox mRepeatTuesday;
    @BindView(R.id.repeat_wednesday)
    CheckBox mRepeatWednesday;
    @BindView(R.id.repeat_thursday)
    CheckBox mRepeatThursday;
    @BindView(R.id.repeat_friday)
    CheckBox mRepeatFriday;
    @BindView(R.id.repeat_saturday)
    CheckBox mRepeatSaturday;
    @BindView(R.id.repeat_sunday)
    CheckBox mRepeatSunday;
    @BindView(R.id.ll_repeat_week)
    LinearLayout mLlRepeatWeek;
    @BindView(R.id.et_repeat_month)
    EditText mEtRepeatMonth;
    @BindView(R.id.ll_repeat_month)
    LinearLayout mLlRepeatMonth;
    @BindView(R.id.et_repeat_year)
    EditText mEtRepeatYear;
    @BindView(R.id.btn_select_repeat_date)
    Button mBtnSelectRepeatDate;
    @BindView(R.id.ll_repeat_year)
    LinearLayout mLlRepeatYear;
    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.btn_select_start_date)
    Button mBtnSelectStartDate;
    @BindView(R.id.tv_deadline)
    TextView mTvDeadline;
    @BindView(R.id.btn_select_deadline)
    Button mBtnSelectDeadline;
    @BindView(R.id.ll_repeat_no)
    LinearLayout mLlRepeatNo;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.tv_commit)
    TextView mTvCommit;
    @BindView(R.id.lv_comments)
    ListView mLvComments;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    Button mBtnFinish;
    Button mBtnClose;
    Button mBtnDelete;
    Button mBtnSavechanges;
    Button mBtnModify;
    @BindView(R.id.tv_task_topic)
    TextView mTvTaskTopic;
    private Task mTask;
    private CommentsAdapter mCommentsAdapter;
    private RepeatTaskInfo mRepeatTaskInfo;
    private RepeatModeConverter mRepeatModeConverter;
    private int timePickerFlag;
    private DatePicker mDatePicker;
    private AlertDialog mDatePickerDialog;
    private TimePicker mTimePicker;
    private AlertDialog mTimePickerDialog;
    private Date mDateAndTimeFromPicker;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TASK_START_DATE:
                    mTvStartDate.setText(TimeUtils.date2String(mDateAndTimeFromPicker));
                    mTask.setStartDate(mDateAndTimeFromPicker);
                    break;
                case MSG_UPDATE_TASK_DEADLINE:
                    mTvDeadline.setText(TimeUtils.date2String(mDateAndTimeFromPicker));
                    mTask.setDeadLine(mDateAndTimeFromPicker);
                    break;
                case MSG_UPDATE_REPEAT_YEAR:
                    mEtRepeatYear.setText(String.valueOf(mDateAndTimeFromPicker.getMonth() + 1).concat("月").concat(String.valueOf(mDateAndTimeFromPicker.getDate())).concat("日"));
                    mRepeatTaskInfo.setRepeatStrategy(String.valueOf(mDateAndTimeFromPicker.getMonth()).concat("-").concat(String.valueOf(mDateAndTimeFromPicker.getDay())));
                    break;
                case MSG_REFRESH_COMMENT:
                    mCommentsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private PriorityConverter mPriorityConverter;
    private PopupWindow mPopupWindow;
    private View mPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        mRepeatModeConverter = new RepeatModeConverter();
        mPriorityConverter = new PriorityConverter();
        initPopView();
        initCommentsListView();
        initUIListenner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshUIByTask();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initPopView() {
        mPopView = LayoutInflater.from(this).inflate(R.layout.layout_more_menu, null);
        mBtnFinish = mPopView.findViewById(R.id.btn_finish);
        mBtnClose = mPopView.findViewById(R.id.btn_close);
        mBtnDelete = mPopView.findViewById(R.id.btn_delete);
        mBtnSavechanges = mPopView.findViewById(R.id.btn_savechanges);
        mBtnModify = mPopView.findViewById(R.id.btn_modify);
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在");
                    return;
                }
                updateUIByTaskStatus(TaskStatus.MODIFYABLE);
            }
        });
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在");
                    return;
                }
                new AlertDialog.Builder(TaskDetailActivity.this).setTitle("提示").setMessage("是否完成任务").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTask.setTaskStatus(TaskStatus.FINISHED);
                        mTask.setCompleteDate(new Date());
                        mRepeatTaskInfo.setLastCompletedDate(new Date());
                        mRepeatTaskInfo.setRepeatCount(mRepeatTaskInfo.getRepeatCount()+1);
                        App.getApplication().getDaoSession().getTaskDao().update(mTask);
                        App.getApplication().getDaoSession().getRepeatTaskInfoDao().update(mRepeatTaskInfo);
                        DoHistory history = new DoHistory();
                        history.setId(null);
                        history.setUserId(CurrentUser.getInstance().getUserId());
                        history.setUserName(CurrentUser.getInstance().getUserName());
                        history.setTaskIdStr(mTask.getTaskIdStr());
                        history.setTaskTitle(mTask.getTitle());
                        history.setCompletedDate(new Date());
                        App.getApplication().getDaoSession().getDoHistoryDao().insert(history);
                        App.getApplication().addScore(mTask.getScore());
                        dialog.dismiss();
                        getPopupWindow().dismiss();
                        TaskDetailActivity.this.finish();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在");
                    return;
                }

                if (mTask.getTaskStatus().equals(TaskStatus.FINISHED)) {
                    ToastUtils.showShort("该任务已经完成了，无需关闭!");
                    return;
                }
                new AlertDialog.Builder(TaskDetailActivity.this).setTitle("提示").setMessage("确定要关闭该任务吗").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTask.setTaskStatus(TaskStatus.CLOSED);
                        App.getApplication().getDaoSession().getTaskDao().update(mTask);
                        getPopupWindow().dismiss();
                        TaskDetailActivity.this.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });

        mBtnSavechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在");
                    return;
                }
                //标题
                String taskTitle = mEtTaskTitle.getText().toString().trim();
                if (!TextUtils.isEmpty(taskTitle)) {
                    mTask.setTitle(taskTitle);
                } else {
                    Toast.makeText(TaskDetailActivity.this, "标题无效", Toast.LENGTH_SHORT).show();
                    return;
                }

                //描述
                String desc = mEtTaskDesc.getText().toString().trim();
                if (!TextUtils.isEmpty(desc)) {
                    mTask.setDescription(desc);
                }

                //优先级
                mTask.setPriority(new PriorityConverter().convertToEntityProperty(mSpinnerPriority.getSelectedItemPosition()));
                ;


                //重复模式
                mRepeatTaskInfo.setRepeatMode(mRepeatModeConverter.convertToEntityProperty(mSpinnerRepeatmode.getSelectedItemPosition()));
                LogUtils.d("saveTask: " + mSpinnerRepeatmode.getSelectedItemPosition());
                switch (mSpinnerRepeatmode.getSelectedItemPosition()) {
                    case 0:
                        break;
                    case 1://日
                        String offsetDayStr = mEtRepeatDay.getText().toString().trim();
                        if (TextUtils.isEmpty(offsetDayStr)) {
                            offsetDayStr = "0";
                        }
                        int offsetDay = Integer.parseInt(offsetDayStr);
                        if (offsetDay >= 0 && offsetDay <= 365) {
                            mRepeatTaskInfo.setRepeatStrategy(offsetDayStr);
                        } else {
                            Toast.makeText(TaskDetailActivity.this, "间隔天数不合法,无法保存", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case 2://周
                        String weekStr = "";
                        if (mRepeatMonday.isChecked()) {
                            weekStr = weekStr.concat("1,");
                        }
                        if (mRepeatTuesday.isChecked()) {
                            weekStr = weekStr.concat("2,");
                        }
                        if (mRepeatWednesday.isChecked()) {
                            weekStr = weekStr.concat("3,");
                        }
                        if (mRepeatThursday.isChecked()) {
                            weekStr = weekStr.concat("4,");
                        }
                        if (mRepeatFriday.isChecked()) {
                            weekStr = weekStr.concat("5,");
                        }
                        if (mRepeatSaturday.isChecked()) {
                            weekStr = weekStr.concat("6,");
                        }
                        if (mRepeatSunday.isChecked()) {
                            weekStr = weekStr.concat("7");
                        }
                        if (weekStr.length() > 1) {
                            mRepeatTaskInfo.setRepeatStrategy(weekStr);
                        } else {
                            Toast.makeText(TaskDetailActivity.this, "没有选择重复规则，无法保存", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case 3://月
                        String monthStr = mEtRepeatMonth.getText().toString().trim();
                        int month = Integer.parseInt(monthStr);
                        if (month >= 0 && month <= 31) {
                            mRepeatTaskInfo.setRepeatStrategy(monthStr);
                        } else {
                            Toast.makeText(TaskDetailActivity.this, "间隔天数不合法,无法保存", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case 4://年
                        break;

                }

                App.getApplication().getDaoSession().getTaskDao().update(mTask);
                App.getApplication().getDaoSession().getRepeatTaskInfoDao().update(mRepeatTaskInfo);
                refreshUIByTask();
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在");
                    return;
                }
                new AlertDialog.Builder(TaskDetailActivity.this).setTitle("提示").setMessage("是否删除任务，\n一旦删除无法还原！").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getApplication().getDaoSession().getTaskDao().delete(mTask);
                        App.getApplication().getDaoSession().getRepeatTaskInfoDao().delete(mRepeatTaskInfo);
                        dialog.dismiss();
                        getPopupWindow().dismiss();
                        TaskDetailActivity.this.finish();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();



            }
        });
    }

    private void refreshUIByTask() {
        App.getApplication().getDaoSession().clear();
        mTask = App.getApplication().getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.TaskIdStr.eq(getIntent().getStringExtra("taskIdStr"))).unique();
        mRepeatTaskInfo = App.getApplication().getDaoSession().getRepeatTaskInfoDao().queryBuilder().where(RepeatTaskInfoDao.Properties.TaskIdStr.eq(getIntent().getStringExtra("taskIdStr"))).unique();
        if (mTask == null || mRepeatTaskInfo == null) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("任务已被删除").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getPopupWindow().dismiss();
                    TaskDetailActivity.this.finish();
                }
            }).show();
            mEtTaskTitle.setText("");
            mEtTaskDesc.setText("");
            mCommentsAdapter.notifyDataSetChanged();
            return;
        }
        mEtTaskTitle.setText(mTask.getTitle());
        mEtTaskDesc.setText(mTask.getDescription());
        mSpinnerPriority.setSelection(mPriorityConverter.convertToDatabaseValue(mTask.getPriority()));
        updateUIByTaskStatus(mTask.getTaskStatus());
        updateUIByRepeatInfo(mRepeatTaskInfo);
        mCommentsAdapter.notifyDataSetChanged();
    }

    private void updateUIByRepeatInfo(RepeatTaskInfo info) {
        Integer integer = mRepeatModeConverter.convertToDatabaseValue(info.getRepeatMode());
        mSpinnerRepeatmode.setSelection(integer);
        updateUIByRepeatMode(info.getRepeatMode());
        switch (info.getRepeatMode()) {
            case EVERYYEAR:
                break;
            case EVERYMONTH:
                String month = info.getRepeatStrategy();
                mEtRepeatMonth.setText(month);
                break;
            case EVERYWEEK:
                String weeks = info.getRepeatStrategy();
                if (weeks.contains("1")) {
                    mRepeatMonday.setChecked(true);
                }
                if (weeks.contains("2")) {
                    mRepeatTuesday.setChecked(true);
                }
                if (weeks.contains("3")) {
                    mRepeatWednesday.setChecked(true);
                }
                if (weeks.contains("4")) {
                    mRepeatThursday.setChecked(true);
                }
                if (weeks.contains("5")) {
                    mRepeatFriday.setChecked(true);
                }
                if (weeks.contains("6")) {
                    mRepeatSaturday.setChecked(true);
                }
                if (weeks.contains("7")) {
                    mRepeatSunday.setChecked(true);
                }
                break;
            case EVERYDAY:
                String day = info.getRepeatStrategy();
                mEtRepeatDay.setText(day);
                break;
            case NOREPEAT:

                break;
        }
    }

    private void updateUIByRepeatMode(RepeatMode repeatMode) {
        switch (repeatMode) {
            default:
                mLlRepeatNo.setVisibility(View.VISIBLE);
                mLlRepeatDay.setVisibility(View.GONE);
                mLlRepeatWeek.setVisibility(View.GONE);
                mLlRepeatMonth.setVisibility(View.GONE);
                mLlRepeatYear.setVisibility(View.GONE);
                break;
            case EVERYDAY:
                mLlRepeatNo.setVisibility(View.GONE);
                mLlRepeatDay.setVisibility(View.VISIBLE);
                mLlRepeatWeek.setVisibility(View.GONE);
                mLlRepeatMonth.setVisibility(View.GONE);
                mLlRepeatYear.setVisibility(View.GONE);
                break;
            case EVERYWEEK:
                mLlRepeatNo.setVisibility(View.GONE);
                mLlRepeatDay.setVisibility(View.GONE);
                mLlRepeatWeek.setVisibility(View.VISIBLE);
                mLlRepeatMonth.setVisibility(View.GONE);
                mLlRepeatYear.setVisibility(View.GONE);
                break;
            case EVERYMONTH:
                mLlRepeatNo.setVisibility(View.GONE);
                mLlRepeatDay.setVisibility(View.GONE);
                mLlRepeatWeek.setVisibility(View.GONE);
                mLlRepeatMonth.setVisibility(View.VISIBLE);
                mLlRepeatYear.setVisibility(View.GONE);
                break;
            case EVERYYEAR:
                mLlRepeatNo.setVisibility(View.GONE);
                mLlRepeatDay.setVisibility(View.GONE);
                mLlRepeatWeek.setVisibility(View.GONE);
                mLlRepeatMonth.setVisibility(View.GONE);
                mLlRepeatYear.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void initUIListenner() {
        /*
         *选择重复模式
         */
        mSpinnerRepeatmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("onItemSelected=" + position);
                if (null!=mRepeatTaskInfo) {
                    mRepeatTaskInfo.setRepeatMode(mRepeatModeConverter.convertToEntityProperty(position));
                    updateUIByRepeatMode(mRepeatModeConverter.convertToEntityProperty(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initCommentsListView() {
        mCommentsAdapter = new CommentsAdapter();
        mLvComments.setAdapter(mCommentsAdapter);
    }


    public class CommentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTask == null ? 0 : mTask.getComments().size();
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
            ViewHolder viewHolder = null;
            if (null == convertView) {
                view = View.inflate(TaskDetailActivity.this, R.layout.item_comment, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.mTvCommentContent.setText(mTask.getComments().get(position).getContent());
            viewHolder.mTvCommentInfo.setText(TimeUtils.date2String(mTask.getComments().get(position).getCommentDate()));

            return view;
        }


        class ViewHolder {
            @BindView(R.id.tv_comment_content)
            TextView mTvCommentContent;
            @BindView(R.id.tv_comment_info)
            TextView mTvCommentInfo;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @OnClick({R.id.tv_close, R.id.tv_completedTask, R.id.btn_select_repeat_date, R.id.btn_select_start_date, R.id.btn_select_deadline, R.id.tv_commit, R.id.iv_more
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    getPopupWindow().dismiss();
                } else {
                    getPopupWindow().showAsDropDown(mIvMore);
                }
                break;
            case R.id.tv_close:
                getPopupWindow().dismiss();
                finish();
                break;
            case R.id.tv_completedTask:
                mTask.setTaskStatus(TaskStatus.FINISHED);
                App.getApplication().getDaoSession().getTaskDao().update(mTask);
                break;
            case R.id.btn_select_start_date:
                timePickerFlag = MSG_UPDATE_TASK_START_DATE;
                getDatePickerDialog().show();
                getTimePickerDialog().show();
                break;
            case R.id.btn_select_deadline:
                timePickerFlag = MSG_UPDATE_TASK_DEADLINE;
                getDatePickerDialog().show();
                getTimePickerDialog().show();
                break;
            case R.id.btn_select_repeat_date:
                timePickerFlag = MSG_UPDATE_REPEAT_YEAR;
                getDatePickerDialog().show();

                break;
            case R.id.tv_commit:
                if (null == mTask) {
                    ToastUtils.showShort("任务不存在!");
                    return;
                }
                String comment = mEtComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.showShort("备注内容为空!");
                    return;
                }
                TaskComment taskComment = new TaskComment();
                taskComment.setCommentDate(new Date());
                taskComment.setContent(comment);
                taskComment.setId(null);
                taskComment.setTaskIdStr(mTask.getTaskIdStr());
                long insert = App.getApplication().getDaoSession().getTaskCommentDao().insert(taskComment);
                if (insert > 0) {
                    mEtComment.setText("");
                }
                App.getApplication().getDaoSession().clear();
                mTask = App.getApplication().getDaoSession().getTaskDao().queryBuilder().where(TaskDao.Properties.TaskIdStr.eq(mTask.getTaskIdStr())).unique();
                mHandler.obtainMessage(MSG_REFRESH_COMMENT).sendToTarget();
                break;
        }
    }

    private PopupWindow getPopupWindow() {
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow();
            mPopupWindow.setContentView(mPopView);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return mPopupWindow;
    }

    private AlertDialog getDatePickerDialog() {
        if (null == mDatePicker) {
            mDatePicker = new DatePicker(TaskDetailActivity.this);
        }
        if (null == mDatePickerDialog) {
            AlertDialog.Builder mDatePickerDialogBuilder = new AlertDialog.Builder(TaskDetailActivity.this);
            mDatePickerDialogBuilder.setView(mDatePicker);
            mDatePickerDialogBuilder.setTitle("请选择日期");
            mDatePickerDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //只有点击确定按钮时，才更改时间，并设置在文本中显示
                    if (null == mDateAndTimeFromPicker) {
                        mDateAndTimeFromPicker = new Date();
                    }
                    mDateAndTimeFromPicker.setYear(mDatePicker.getYear() - 1900);
                    mDateAndTimeFromPicker.setMonth(mDatePicker.getMonth());
                    mDateAndTimeFromPicker.setDate(mDatePicker.getDayOfMonth());
                    dialog.dismiss();
                    mHandler.obtainMessage(timePickerFlag).sendToTarget();
                }
            });
            mDatePickerDialogBuilder.setNegativeButton("取消", null);
            mDatePickerDialog = mDatePickerDialogBuilder.create();
        }

        return mDatePickerDialog;
    }

    private AlertDialog getTimePickerDialog() {
        if (null == mTimePicker) {
            mTimePicker = new TimePicker(TaskDetailActivity.this);
        }
        if (null == mTimePickerDialog) {
            AlertDialog.Builder timePickerBuilder = new AlertDialog.Builder(TaskDetailActivity.this);
            timePickerBuilder.setView(mTimePicker);
            timePickerBuilder.setTitle("请选择时间");
            timePickerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //只有点击确定按钮时，才更改时间，并设置在文本中显示
                    if (null == mDateAndTimeFromPicker) {
                        mDateAndTimeFromPicker = new Date();
                    }
                    mDateAndTimeFromPicker.setHours(mTimePicker.getCurrentHour());
                    mDateAndTimeFromPicker.setMinutes(mTimePicker.getCurrentMinute());
                    dialog.dismiss();
                    mHandler.obtainMessage(timePickerFlag).sendToTarget();
                }
            });
            timePickerBuilder.setNegativeButton("取消", null);
            mTimePickerDialog = timePickerBuilder.create();
        }
        return mTimePickerDialog;
    }

    private void updateUIByTaskStatus(TaskStatus status) {
        switch (status) {
            case MODIFYABLE:
                mEtTaskTitle.setEnabled(true);
                mEtTaskDesc.setEnabled(true);
                mSpinnerPriority.setEnabled(true);
                mSpinnerRepeatmode.setEnabled(true);
                mBtnSelectDeadline.setEnabled(true);
                mBtnSelectRepeatDate.setEnabled(true);
                mBtnSelectStartDate.setEnabled(true);
                mEtRepeatDay.setEnabled(true);
                mEtRepeatMonth.setEnabled(true);
                mEtRepeatYear.setEnabled(true);
                mRepeatMonday.setEnabled(true);
                mRepeatTuesday.setEnabled(true);
                mRepeatWednesday.setEnabled(true);
                mRepeatThursday.setEnabled(true);
                mRepeatFriday.setEnabled(true);
                mRepeatSaturday.setEnabled(true);
                mRepeatSunday.setEnabled(true);
                break;
            default:
                mEtTaskTitle.setEnabled(false);
                mEtTaskDesc.setEnabled(false);
                mSpinnerPriority.setEnabled(false);
                mSpinnerRepeatmode.setEnabled(false);
                mBtnSelectDeadline.setEnabled(false);
                mBtnSelectRepeatDate.setEnabled(false);
                mBtnSelectStartDate.setEnabled(false);
                mEtRepeatDay.setEnabled(false);
                mEtRepeatMonth.setEnabled(false);
                mEtRepeatYear.setEnabled(false);
                mRepeatMonday.setEnabled(false);
                mRepeatTuesday.setEnabled(false);
                mRepeatWednesday.setEnabled(false);
                mRepeatThursday.setEnabled(false);
                mRepeatFriday.setEnabled(false);
                mRepeatSaturday.setEnabled(false);
                mRepeatSunday.setEnabled(false);
                break;
        }

//        if (TaskStatus.FINISHED.equals(status)) {
//            mTvTaskTopic.setText("任务(已完成)");
//        } else if (TaskStatus.CLOSED.equals(status)) {
//            mTvTaskTopic.setText("任务(已关闭)");
//        } else if (TaskStatus.NORMAL.equals(status)) {
//            mTvTaskTopic.setText("任务(正常)");
//        } else if (TaskStatus.OVERTIME.equals(status)) {
//            mTvTaskTopic.setText("任务(超时)");
//        } else if (TaskStatus.UNFINISHED.equals(status)) {
//            mTvTaskTopic.setText("任务(未完成)");
//        } else {
//            mTvTaskTopic.setText("任务");
//        }



    }

}
