package com.zhaohe.zhundao.ui.home.action;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhaohe.app.camera.Camera;
import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncEditAction;
import com.zhaohe.zhundao.bean.ToolUserBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblDate;
    private TextView lblTime;
    private TextView lblDate2;
    private TextView lblTime2;
    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private Calendar calendar2;
    private DateFormat dateFormat2;
    private SimpleDateFormat timeFormat2;
    private static final String TIME_PATTERN = "HH:mm";
    private Handler mHandler;
    private EditText et_edit_title;
    private String mParam;
    private String mID;
    private Button btn_edit_submit;
    private GridLayout gl_camara;
    private Camera camera;
    public static final int MESSAGE_EDIT_ACT = 97;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_act);
        getBundleContent();
        initHandler();
        initView();
        update();
        update2();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void getBundleContent() {
        //新页面接收act_id的值
        Bundle bundle = this.getIntent().getExtras();
        mID = bundle.getString("act_id");

    }

    private void initView() {
        gl_camara = (GridLayout) findViewById(R.id.camera_gridview);
        camera = new Camera(EditActActivity.this, gl_camara, false);
        et_edit_title = (EditText) findViewById(R.id.et_edit_title);
        btn_edit_submit = (Button) findViewById(R.id.btn_edit_submit);
        btn_edit_submit.setOnClickListener(this);
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        calendar2 = Calendar.getInstance();
        dateFormat2 = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat2 = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        lblDate = (TextView) findViewById(R.id.tv_starttime_year);
        lblDate.setOnClickListener(this);
        lblTime = (TextView) findViewById(R.id.tv_starttime_clock);
        lblTime.setOnClickListener(this);
        lblDate2 = (TextView) findViewById(R.id.tv_stoptime_year);
        lblDate2.setOnClickListener(this);
        lblTime2 = (TextView) findViewById(R.id.tv_stoptime_clock);
        lblTime2.setOnClickListener(this);

    }

    private void init() {
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            String mTitle = et_edit_title.getText().toString();
            mParam = "ID=" + mID + "&Title=" + mTitle;
            AsyncEditAction editAction = new AsyncEditAction(this, mHandler, dialog, MESSAGE_EDIT_ACT, mParam);
            editAction.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_EDIT_ACT:
                        String result = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result, ToolUserBean.class);
                        if (bean.getRes() == 0) {
                            DialogUtils.showDialog(EditActActivity.this, R.string.app_dialog_sumbit_success);
                        } else {
                            DialogUtils.showDialog(EditActActivity.this, R.string.app_dialog_sumbit_failed);

                        }

                        System.out.println("Activity result:  " + result);

                        break;


                    default:
                        break;
                }
            }
        };
    }

    //更新活动开始时间
    private void update() {
        lblDate.setText(dateFormat.format(calendar.getTime()));
        lblTime.setText(timeFormat.format(calendar.getTime()));

    }

    //更新活动截止时间
    private void update2() {
        lblDate2.setText(dateFormat2.format(calendar2.getTime()));
        lblTime2.setText(timeFormat2.format(calendar2.getTime()));
    }

    //活动开始日期监听器
    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            update();
        }
    };
    //活动开始时间监听器
    TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            update();
        }
    };
    //时间截止日期监听器
    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
            calendar2.set(year, monthOfYear, dayOfMonth);
            update2();
        }
    };
    //时间截止时间监听器
    TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
            calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar2.set(Calendar.MINUTE, minute);
            update2();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        camera.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_starttime_year:
                DatePickerDialog.newInstance(startDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.tv_starttime_clock:
                TimePickerDialog.newInstance(startTimeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
            case R.id.tv_stoptime_year:
                DatePickerDialog.newInstance(endDateListener, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.tv_stoptime_clock:
                TimePickerDialog.newInstance(endTimeListener, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            case R.id.btn_edit_submit:
                init();
                break;
        }
    }
}
