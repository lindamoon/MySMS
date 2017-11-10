package com.lixb.mysms.act;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lixb.mysms.R;
import com.lixb.mysms.base.App;
import com.lixb.mysms.biz.user.CurrentUser;
import com.lixb.mysms.entity.RepeatTaskInfo;
import com.lixb.mysms.entity.Task;
import com.lixb.mysms.entity.enums.PriorityConverter;
import com.lixb.mysms.entity.enums.RepeatModeConverter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";
    private static final int MSG_UPDATE_TASK_START_DATE = 1;
    private static final int MSG_UPDATE_TASK_DEADLINE = 2;
    private static final int MSG_UPDATE_REPEAT_YEAR = 3;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.tv_save)
    TextView mTvSave;
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
    @BindView(R.id.tv_start_date)
    TextView mTvStartDate;
    @BindView(R.id.btn_select_start_date)
    Button mBtnSelectStartDate;
    @BindView(R.id.tv_deadline)
    TextView mTvDeadline;
    @BindView(R.id.btn_select_deadline)
    Button mBtnSelectDeadline;
    @BindView(R.id.et_repeat_day)
    EditText mEtRepeatDay;
    @BindView(R.id.ll_repeat_day)
    LinearLayout mLlRepeatDay;
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
    @BindView(R.id.ll_repeat_no)
    LinearLayout mLlRepeatNo;
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
    private DatePicker mDatePicker;
    private Task mTask;
    private AlertDialog mDatePickerDialog;
    private Date mDateAndTimeFromPicker;
    private AlertDialog mTimePickerDialog;
    private TimePicker mTimePicker;
    private RepeatTaskInfo mRepeatTaskInfo;
    private RepeatModeConverter mRepeatModeConverter;

    private int timePickerFlag = 0;
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
                    mEtRepeatYear.setText(String.valueOf(mDateAndTimeFromPicker.getMonth()+1).concat("月").concat(String.valueOf(mDateAndTimeFromPicker.getDate())).concat("日"));
                    mRepeatTaskInfo.setRepeatStrategy(String.valueOf(mDateAndTimeFromPicker.getMonth()).concat("-").concat(String.valueOf(mDateAndTimeFromPicker.getDay())));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        mTask = new Task();
        mTask.setUserId(CurrentUser.getInstance().getUserId());
        mRepeatTaskInfo = new RepeatTaskInfo();
        mRepeatModeConverter = new RepeatModeConverter();
        initUI();
    }


    /**
     * 初始化ui相关事件
     */
    private void initUI() {


        /*
         *选择重复模式
         */
        mSpinnerRepeatmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("onItemSelected="+position);
                mRepeatTaskInfo.setRepeatMode(mRepeatModeConverter.convertToEntityProperty(position));
                switch (position) {
                    default:
                        mLlRepeatNo.setVisibility(View.VISIBLE);
                        mLlRepeatDay.setVisibility(View.GONE);
                        mLlRepeatWeek.setVisibility(View.GONE);
                        mLlRepeatMonth.setVisibility(View.GONE);
                        mLlRepeatYear.setVisibility(View.GONE);
                        break;
                    case 1:
                        mLlRepeatNo.setVisibility(View.GONE);
                        mLlRepeatDay.setVisibility(View.VISIBLE);
                        mLlRepeatWeek.setVisibility(View.GONE);
                        mLlRepeatMonth.setVisibility(View.GONE);
                        mLlRepeatYear.setVisibility(View.GONE);
                        break;
                    case 2:
                        mLlRepeatNo.setVisibility(View.GONE);
                        mLlRepeatDay.setVisibility(View.GONE);
                        mLlRepeatWeek.setVisibility(View.VISIBLE);
                        mLlRepeatMonth.setVisibility(View.GONE);
                        mLlRepeatYear.setVisibility(View.GONE);
                        break;
                    case 3:
                        mLlRepeatNo.setVisibility(View.GONE);
                        mLlRepeatDay.setVisibility(View.GONE);
                        mLlRepeatWeek.setVisibility(View.GONE);
                        mLlRepeatMonth.setVisibility(View.VISIBLE);
                        mLlRepeatYear.setVisibility(View.GONE);
                        break;
                    case 4:
                        mLlRepeatNo.setVisibility(View.GONE);
                        mLlRepeatDay.setVisibility(View.GONE);
                        mLlRepeatWeek.setVisibility(View.GONE);
                        mLlRepeatMonth.setVisibility(View.GONE);
                        mLlRepeatYear.setVisibility(View.VISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @OnClick({R.id.tv_cancel, R.id.tv_save, R.id.btn_select_start_date, R.id.btn_select_deadline, R.id.btn_select_repeat_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                Log.d(TAG, "onViewClicked: cancel");
                finish();
                break;
            case R.id.tv_save:
                Log.d(TAG, "onViewClicked: save");
                saveTask();
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
        }
    }

    private AlertDialog getDatePickerDialog() {
        if (null == mDatePicker) {
            mDatePicker = new DatePicker(AddTaskActivity.this);
        }
        if (null == mDatePickerDialog) {
            AlertDialog.Builder mDatePickerDialogBuilder = new AlertDialog.Builder(AddTaskActivity.this);
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
            mTimePicker = new TimePicker(AddTaskActivity.this);
        }
        if (null == mTimePickerDialog) {
            AlertDialog.Builder timePickerBuilder = new AlertDialog.Builder(AddTaskActivity.this);
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

    private void saveTask() {

        //设置taskIdStr
        String taskIdStr = CurrentUser.getInstance().getUserId().toString().concat("-").concat(String.valueOf(System.currentTimeMillis()));
        mTask.setTaskIdStr(taskIdStr);
        mRepeatTaskInfo.setTaskIdStr(taskIdStr);

        //标题
        String taskTitle = mEtTaskTitle.getText().toString().trim();
        if (!TextUtils.isEmpty(taskTitle)) {
            mTask.setTitle(taskTitle);
        } else {
            Toast.makeText(this, "标题无效", Toast.LENGTH_SHORT).show();
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
        LogUtils.d( "saveTask: "+mSpinnerRepeatmode.getSelectedItemPosition());
        switch (mSpinnerRepeatmode.getSelectedItemPosition()) {
            case 0:
                break;
            case 1://日
                String offsetDayStr = mEtRepeatDay.getText().toString().trim();
                int offsetDay = Integer.parseInt(offsetDayStr);
                if (offsetDay >= 0 && offsetDay <= 365) {
                    mRepeatTaskInfo.setRepeatStrategy(offsetDayStr);
                } else {
                    Toast.makeText(this, "间隔天数不合法,无法保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case 2://周
                String weekStr = "";
                if (mRepeatMonday.isChecked()) {
                    weekStr=weekStr.concat("1,");
                }
                if (mRepeatTuesday.isChecked()) {
                    weekStr=weekStr.concat("2,");
                }
                if (mRepeatWednesday.isChecked()) {
                    weekStr=weekStr.concat("3,");
                }
                if (mRepeatThursday.isChecked()) {
                    weekStr=weekStr.concat("4,");
                }
                if (mRepeatFriday.isChecked()) {
                    weekStr=weekStr.concat("5,");
                }
                if (mRepeatSaturday.isChecked()) {
                    weekStr=weekStr.concat("6,");
                }
                if (mRepeatSunday.isChecked()) {
                    weekStr=weekStr.concat("7");
                }
                if (weekStr.length() > 1) {
                    mRepeatTaskInfo.setRepeatStrategy(weekStr);
                } else {
                    Toast.makeText(this, "没有选择重复规则，无法保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case 3://月
                String monthStr = mEtRepeatMonth.getText().toString().trim();
                int month = Integer.parseInt(monthStr);
                if (month >= 0 && month <= 31) {
                    mRepeatTaskInfo.setRepeatStrategy(monthStr);
                } else {
                    Toast.makeText(this, "间隔天数不合法,无法保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case 4://年
                break;

        }
        LogUtils.d(mTask.toString());
        long insertTask = App.getApplication().getDaoSession().getTaskDao().insert(mTask);
        long insertRepeatTask = App.getApplication().getDaoSession().getRepeatTaskInfoDao().insert(mRepeatTaskInfo);
        mRepeatTaskInfo.setId(null);
        mTask.setId(null);
        if (insertRepeatTask > 0 && insertTask > 0) {
            Toast.makeText(this, "添加任务成功!", Toast.LENGTH_LONG).show();
        }


    }

}
