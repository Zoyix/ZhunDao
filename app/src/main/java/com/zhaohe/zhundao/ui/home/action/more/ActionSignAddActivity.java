package com.zhaohe.zhundao.ui.home.action.more;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncSignAdd;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.mine.UpgradedActivity;


public class ActionSignAddActivity extends ToolBarActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private String act_id,act_title;
    private TextView tv_act_title;
    private EditText et_sign_add_name;
    private Spinner sp_sign_add_type,sp_sign_add_object;
    private Button btn_sign_add;
private Handler mHandler;
    public static final int MESSAGE_SIGN_ADD = 99;
    private String param,CheckInType,Name,SignObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
        setContentView(R.layout.activity_action_sign_add);
        initIntent();
        initToolBar("发起签到", R.layout.activity_action_sign_add);
        initView();
    }

    private void initView() {
        tv_act_title= (TextView) findViewById(R.id.tv_sign_add_actname2);
        tv_act_title.setText(act_title);
        et_sign_add_name= (EditText) findViewById(R.id.et_signlist_user_name);
        et_sign_add_name.setText(act_title+"[签到]");
        sp_sign_add_type= (Spinner) findViewById(R.id.sp_sign_add_type);
        sp_sign_add_type.setOnItemSelectedListener(this);
        sp_sign_add_object= (Spinner) findViewById(R.id.sp_people_add_group);
        sp_sign_add_object.setOnItemSelectedListener(this);

        btn_sign_add= (Button) findViewById(R.id.btn_people_add);
        btn_sign_add.setOnClickListener(this);
    }

    public void initToolBar(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }
    private void initIntent() {
        Intent intent = getIntent();
        act_id = intent.getStringExtra("act_id");
        act_title = intent.getStringExtra("act_title");
    }
    private void addSign(String mParam){
        AsyncSignAdd async =new AsyncSignAdd(this,mHandler,MESSAGE_SIGN_ADD,mParam);
        async.execute();
    }
    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGN_ADD:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        String message = jsonObj.getString("Res");
                        System.out.println("sign_add_result:"+result);
                        if (message.equals("0"))
                        //添加或修改请求结果
                        {
                            ToastUtil.makeText(getApplicationContext(), "签到发起成功！");
                            finish();
                        }
                        if ("201".equals(jsonObj.getString("Url"))){
                            UpgradedDialog(ActionSignAddActivity.this);
                        }

                        break;

                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_people_add:
Name=et_sign_add_name.getText().toString();
                if(Name.length()==0){
                    ToastUtil.makeText(getApplicationContext(), "签到名称不得为空！");

                    return;
                }
param="CheckInType=" + CheckInType + "&SignObject=" + SignObject+"&Name="+Name+"&ActivityID="+act_id;

                addSign(param);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(sp_sign_add_type.getSelectedItem().toString().equals("到场签到")){
    CheckInType="0";
}
        if(sp_sign_add_type.getSelectedItem().toString().equals("离场签退")){
            CheckInType="1";
        }
        if(sp_sign_add_type.getSelectedItem().toString().equals("集合签到")){
            CheckInType="2";
        }

        if(sp_sign_add_object.getSelectedItem().toString().equals("限报名人员")){
            SignObject="0";
        }
        if(sp_sign_add_object.getSelectedItem().toString().equals("不限报名人员")){
            SignObject="1";
        }

}


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        CheckInType="0";
        SignObject="0";
    }
    public void UpgradedDialog(final Activity activity) {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化

        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("对不起,您的权限不够！")
                //设定显示的View
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        IntentUtils.startActivity(activity, UpgradedActivity.class);
                    }

                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }
}
