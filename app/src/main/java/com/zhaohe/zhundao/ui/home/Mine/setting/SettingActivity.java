package com.zhaohe.zhundao.ui.home.mine.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetHelper;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.app.version.VersionBean;
import com.zhaohe.app.version.VersionXmlUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncChangePassword;
import com.zhaohe.zhundao.dao.MySignupListMultiDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.mine.FeedbackActivity;
import com.zhaohe.zhundao.ui.login.LoginActivity;

public class SettingActivity extends ToolBarActivity implements View.OnClickListener {
    private TextView tv_setting_exit, tv_setting_about_us, tv_setting_feedback, tv_setting_password,tv_setting_version,tv_setting_version_show;
    public static final int MESSAGE_CHANGE_PASSWORD = 86;
    private Handler mHandler;
    private VersionBean bean;
    private MySignupListMultiDao dao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initHandler();
        initToolBar("设置");
        initView();
        goAsyncUpdate();

        /*自定义的一些操作*/
//        onCreateCustomToolBar(toolbar) ;

//        super.toolbar.
    }

    private void initView() {
        tv_setting_exit = (TextView) findViewById(R.id.tv_setting_exit);
        tv_setting_exit.setOnClickListener(this);
        tv_setting_about_us = (TextView) findViewById(R.id.tv_setting_about_us);
        tv_setting_about_us.setOnClickListener(this);
        tv_setting_feedback = (TextView) findViewById(R.id.tv_setting_feedback);
        tv_setting_feedback.setOnClickListener(this);
        tv_setting_password = (TextView) findViewById(R.id.tv_setting_password);
        tv_setting_password.setOnClickListener(this);
        tv_setting_version = (TextView) findViewById(R.id.tv_setting_version);
        tv_setting_version.setOnClickListener(this);
        tv_setting_version_show= (TextView) findViewById(R.id.tv_setting_version_show);
        tv_setting_version_show.setText("APP版本号："+getVersion(this));
        dao=new MySignupListMultiDao(this);

    }

    private void initToolBar(String text) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, R.layout.activity_setting);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
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
                        if (jsonObj.getString("Res") == "0") {

                            ToastUtil.makeText(getApplicationContext(), "密码修改成功！");
                        }
                    default:
                        break;
                }
            }
        };
    }

    private void changePassword(String password) {
        AsyncChangePassword async = new AsyncChangePassword(this, mHandler, MESSAGE_CHANGE_PASSWORD, password);
        async.execute();
    }
    /**
     * @Description: 判断是否需要更新
     * @Author:杨攀
     * @Since: 2015年4月13日下午12:26:33
     */
    private void goAsyncUpdate() {
        AsyncUpdateVersion task = new AsyncUpdateVersion();
        task.execute();
    }
    public static String getVersion(Context context)//获取版本号
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
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
                        final EditText etPassword = (EditText) textEntryView.findViewById(R.id.et_dialog_password);
                        final EditText etPassword2 = (EditText) textEntryView.findViewById(R.id.et_dialog_password2);

                        //将页面输入框中获得的“用户名”，“密码”转为字符串
                        String password = etPassword.getText().toString();
                        String password2 = etPassword2.getText().toString();

                        //现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
                        //这里做一个简单的测试，假定输入的用户名和密码都是1则进入其他操作页面（OperationActivity）
                        if ((password.length() < 6)) {
                            ToastUtil.makeText(getApplicationContext(), "密码应大于等于6位，请确认后重试");
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
                        else {
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
        switch (view.getId()) {
            case R.id.tv_setting_exit:
                dao.deleteTable();
                SPUtils.clear(this);
                IntentUtils.startActivity(this, LoginActivity.class);
                break;
            case R.id.tv_setting_about_us:
                IntentUtils.startActivity(this, AboutUsActivity.class);
                break;
            case R.id.tv_setting_feedback:
                IntentUtils.startActivity(this, FeedbackActivity.class);
                break;
            case R.id.tv_setting_password:
                showWaiterAuthorizationDialog();
                break;
                case R.id.tv_setting_version:
//                    ToastUtil.makeText(this,"APP版本号："+getVersion(this));
                    if (bean.getSynccode().equals("0")) {
return;
                    }
                    if (VersionXmlUtils.isUpdateApp(SettingActivity.this, bean)) {// 更新App
                        DialogUtils.showDialog(SettingActivity.this, R.string.app_updateApp_message, new UpdateAppPositiveButtonListener(bean),
                                new UpdateAppNegativeButtonListener());
                        return;
                    }
                    else
                {
                    ToastUtil.makeText(this,"已是最新版本");
                }
                    break;
                default:break;

        }
    }
    private final class AsyncUpdateVersion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // 发送请求去判断是否需要更新
            String path = "http://www.zhundao.net/Android/version.xml";
//            http://agent.joinhead.com/Android/version.xml
//            http://122.225.101.94/dqaj/app/version.xml
//            http://www.zhundao.net/Android/2.txt
//            http://www.zhundao.net/Android/version.xml
//            return HttpUtil.sendGET2Request (path, null,"UTF-8");
            try {
                return NetHelper.httpStringGet(path, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            bean = null;

            if (result != null) {
                try {

                    bean = VersionXmlUtils.parserVersionXml(result);

//              Synccode=0 忽略更新 1 用户选择 2 强制更新
                    if (bean.getSynccode().equals("0")) {

                    }
//                    VersionXmlUtils.isUpdateApp (MainActivity.this, bean)
                    if (VersionXmlUtils.isUpdateApp(SettingActivity.this, bean)) {// 更新App
//                        DialogUtils.showDialog(SettingActivity.this, R.string.app_updateApp_message, new UpdateAppPositiveButtonListener(bean),
//                                new UpdateAppNegativeButtonListener());
                        Drawable drawable= getResources().getDrawable(R.mipmap.unread);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_setting_version .setCompoundDrawables(null,null,drawable,null);

                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //其他情况，这进行登录
        }
    }

    /**
     * @Description: 确定更新
     * @Author:杨攀
     * @Since:2014年11月20日下午3:48:17
     */
    private final class UpdateAppPositiveButtonListener implements DialogInterface.OnClickListener {

        private VersionBean bean;

        public UpdateAppPositiveButtonListener(VersionBean bean) {
            this.bean = bean;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            // 开始 更新App
            VersionXmlUtils.startUpdateApp(SettingActivity.this, bean);

//            MainActivity.this.finish ();
        }

    }

    /**
     * @Description: 取消更新
     * @Author:杨攀
     * @Since:2014年11月20日下午3:50:43
     */
    private final class UpdateAppNegativeButtonListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
//            MainActivity.this.finish ();
//            System.exit (0);
        }

    }
}
