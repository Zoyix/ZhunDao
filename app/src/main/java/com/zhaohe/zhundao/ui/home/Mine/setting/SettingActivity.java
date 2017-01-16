package com.zhaohe.zhundao.ui.home.mine.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncChangePassword;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.mine.FeedbackActivity;
import com.zhaohe.zhundao.ui.login.LoginActivity;

public class SettingActivity extends ToolBarActivity implements View.OnClickListener {
private TextView tv_setting_exit,tv_setting_about_us,tv_setting_feedback,tv_setting_password;
    public static final int MESSAGE_CHANGE_PASSWORD = 86;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initHandler();
        initToolBar("设置");
        initView();

        /*自定义的一些操作*/
//        onCreateCustomToolBar(toolbar) ;

//        super.toolbar.
    }

    private void initView() {
        tv_setting_exit= (TextView) findViewById(R.id.tv_setting_exit);
        tv_setting_exit.setOnClickListener(this);
        tv_setting_about_us= (TextView) findViewById(R.id.tv_setting_about_us);
        tv_setting_about_us.setOnClickListener(this);
        tv_setting_feedback= (TextView) findViewById(R.id.tv_setting_feedback);
        tv_setting_feedback.setOnClickListener(this);
        tv_setting_password= (TextView) findViewById(R.id.tv_setting_password);
        tv_setting_password.setOnClickListener(this);

    }

    private void initToolBar(String text){
         ToolBarHelper mToolBarHelper ;
        mToolBarHelper = new ToolBarHelper(this,R.layout.activity_setting) ;
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar() ;
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_CHANGE_PASSWORD:
                        String result = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result);
                        if(jsonObj.getString("Res")=="0"){

                            ToastUtil.makeText(getApplicationContext(),"密码修改成功！");
                        }
                    default:
                        break;
                }
            }
        };
    }
    private void changePassword(String password){
        AsyncChangePassword async = new AsyncChangePassword(this, mHandler, MESSAGE_CHANGE_PASSWORD, password);
        async.execute();
    }

    public void showWaiterAuthorizationDialog() {

        //LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(this);
        //把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.dialog_password, null);

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                .setTitle("修改密码")
                //设定显示的View
                .setView(textEntryView)
                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //获取用户输入的“用户名”，“密码”
                        //注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login, null)将页面布局赋值给了textEntryView了
                        final EditText etPassword = (EditText)textEntryView.findViewById(R.id.et_dialog_password);
                        final EditText etPassword2 = (EditText)textEntryView.findViewById(R.id.et_dialog_password2);

                        //将页面输入框中获得的“用户名”，“密码”转为字符串
                        String password = etPassword.getText().toString();
                        String password2 = etPassword2.getText().toString();

                        //现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
                        //这里做一个简单的测试，假定输入的用户名和密码都是1则进入其他操作页面（OperationActivity）
                        if((password.length()<6)){
                            ToastUtil.makeText(getApplicationContext(),"密码应大于等于6位，请确认后重试");




                        }
//                        else if(password.equals(password2)==false){
//                            ToastUtil.makeText(getApplicationContext(),"二次密码输入不一致，请确认后重试");
//                            try {
//                                // 注意此处是通过反射，修改源代码类中的字段mShowing为true，系统会认为对话框打开
//                                // 从而调用dismiss()
//                                Field field = dialog.getClass().getSuperclass()
//                                        .getDeclaredField("mShowing");
//                                field.setAccessible(true);
//                                field.set(dialog, false);
//                                dialog.dismiss();
//
//                            } catch (Exception e) {
//
//                            }
//                        }
//                        else if (password.equals(password2)==true){
//                            changePassword(password);
//                        }
//                        else{
//                            ToastUtil.makeText(getApplicationContext(),"输入有误，请确认后重试");
//
//                        }
                        else{
                            changePassword(password);
                        }
                    }
                })
                //对话框的“退出”单击事件
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                //对话框的创建、显示
                .create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_setting_exit:
                SPUtils.clear(this);
                IntentUtils.startActivity(this, LoginActivity.class);
                break;
            case R.id.tv_setting_about_us:
                IntentUtils.startActivity(this, AboutUsActivity.class);
                break;
            case R.id.tv_setting_feedback:
                IntentUtils.startActivity(this, FeedbackActivity.class);
break;
            case  R.id.tv_setting_password:
                showWaiterAuthorizationDialog();
                break;
        }
    }
}
