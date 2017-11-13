package com.lixb.mysms.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lixb.mysms.R;
import com.lixb.mysms.base.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.btn_add_task)
    Button mBtnAddTask;
    @BindView(R.id.btn_show_tasks)
    Button mBtnShowTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }



    @OnClick({R.id.btn_add_task, R.id.btn_show_tasks})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_task:
                startActivity(new Intent(this, AddTaskActivity.class));
                break;
            case R.id.btn_show_tasks:
                startActivity(new Intent(this, TaskListActivity.class));
                break;
        }
    }
}
