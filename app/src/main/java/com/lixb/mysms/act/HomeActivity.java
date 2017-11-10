package com.lixb.mysms.act;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.lixb.mysms.R;
import com.lixb.mysms.base.App;
import com.lixb.mysms.entity.RepeatTaskInfo;
import com.lixb.mysms.entity.Task;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.rv_tasks)
    RecyclerView mRvTasks;
    private ArrayList<Task> mTodayTasks;
    private HashMap<String, RepeatTaskInfo> mTodayRepeatModeInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
        initRecyclerView();
    }

    private void initData() {
        mTodayTasks = new ArrayList<>();
        mTodayRepeatModeInfos = new HashMap<>();
        new Thread() {
            @Override
            public void run() {
                QueryBuilder<RepeatTaskInfo> repeatTaskInfoQueryBuilder = App.getApplication().getDaoSession().queryBuilder(RepeatTaskInfo.class);
            }
        };
    }

    private void initRecyclerView() {

    }



}
