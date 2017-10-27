package com.zhaohe.zhundao.ui.home.find;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.testScan.AsyncMultiLogin;
import com.zhaohe.zhundao.asynctask.testScan.AsyncUpLoadSignupStatusMulti;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.bean.updateBean;
import com.zhaohe.zhundao.dao.MySignupListMultiDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;

import java.util.List;

public class MultiLoginActivity extends ToolBarActivity implements View.OnClickListener {
    private Handler mHandler;
    private String mmobile;
    private String mpassWord;
    private String msign_id;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_sign_id;
    private TextView tv_multi_introduce;
    private Button btn_login;
    private String sp_sign_id;
    private MySignupListMultiDao dao;
    public static final int MESSAGE_UPLOAD_SIGNUPSTATUS = 88;

    public static final int MESSAGE_LOGIN_ENTY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_login);
        initToolBar("多点签到登录", R.layout.activity_multi_login);
        initHandler();
        initView();
        upload();

    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login_multi);
        btn_login.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone_multi);
        et_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_password = (EditText) findViewById(R.id.et_password_multi);

        et_sign_id = (EditText) findViewById(R.id.et_sign_id_multi);
        et_sign_id.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        tv_multi_introduce = (TextView) findViewById(R.id.tv_multi_introduce);
        tv_multi_introduce.setOnClickListener(this);
        dao = new MySignupListMultiDao(this);

        if (SPUtils.contains(this, "sign_id")) {
            sp_sign_id = (String) SPUtils.get(this, "sign_id", "");

        }
        if (SPUtils.contains(this, "msign_id")) {
            et_sign_id.setText((String) SPUtils.get(this, "msign_id", ""));
        }
        if (SPUtils.contains(this, "mmobile")) {
            et_phone.setText((String) SPUtils.get(this, "mmobile", ""));

        }
        if (SPUtils.contains(this, "mpassWord")) {
            et_password.setText((String) SPUtils.get(this, "mpassWord", ""));

        }
    }

    private void initToolBar(String text, int layoutResID) {
        ToolBarHelper mToolBarHelper;
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolBarHelper.setTvTitle(text);
        super.setTitle("");
        setContentView(mToolBarHelper.getContentView());
        toolbar = mToolBarHelper.getToolBar();
  /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
    }

    private void upload() {
        if (NetworkUtils.checkNetState(this)) {
            List<updateBean> list = dao.queryUpdateStatusNew();
            String jsonString = JSON.toJSONString(list);
            if (list.size() == 0) {
                return;
            }
//        System.out.println(jsonString);
//ToastUtil.makeText(getActivity(),jsonString);
//        还要上传数据，现在获得了数据

            AsyncUpLoadSignupStatusMulti async = new AsyncUpLoadSignupStatusMulti(this, mHandler, MESSAGE_UPLOAD_SIGNUPSTATUS, jsonString);
            async.execute();
        } else {
            ToastUtil.makeText(getApplicationContext(), "暂无网络");

        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_LOGIN_ENTY:
                        String result = (String) msg.obj;
                        System.out.println("result:" + result);
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject.getString("Res").equals("0")) {
                            SPUtils.put(getApplicationContext(), "accessKey_multi", jsonObject.getString("AccessToken"));
                            SPUtils.put(getApplicationContext(), "sign_id", msign_id);
                            SPUtils.put(getApplicationContext(), "result_multi", result);
                            JSONObject jsonObject2 = JSON.parseObject(jsonObject.getString("Data"));
                            String UserName = jsonObject2.getString("UserName");
                            SPUtils.put(getApplicationContext(), "UserName", UserName);
                            JSONObject jsonObject3 = JSON.parseObject(jsonObject2.getString("CheckInDto"));
                            String sign_title = jsonObject3.getString("Name");
                            if (sign_title.equals(null)) {
                                sign_title = jsonObject3.getString("ActivityName") + "（签到）";
                            }
                            {
                                SPUtils.put(getApplicationContext(), "sign_title", sign_title);
                            }
                            IntentUtils.startActivity(MultiLoginActivity.this, MultipointActivity.class);
                        }
                        ToastUtil.makeText(getApplicationContext(), jsonObject.getString("Msg"));
                        break;
                    case MESSAGE_UPLOAD_SIGNUPSTATUS:
                        String result4 = (String) msg.obj;
                        JSONObject jsonObj2 = JSON.parseObject(result4);
                        if (jsonObj2.getString("Res").equals("0")) {
                            changeStatus();
                            ToastUtil.makeText(getApplicationContext(), "数据上传成功");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    //    上传成功后 修改已上传数据更新状态
    private void changeStatus() {
        List<MySignListupBean> list = dao.queryUpdateStatus();
        for (int i = 0; i < list.size(); i++) {
            MySignListupBean bean2 = (MySignListupBean) list.get(i);
            MySignListupBean bean = new MySignListupBean();
            bean.setVCode(bean2.getVCode());
            bean.setStatus("true");
            bean.setUpdateStatus("false");
            bean.setCheckInID(bean2.getCheckInID());
            bean.setCheckInTime(bean2.getCheckInTime());
            dao.update(bean);
        }

    }

    private void init() {
        if (NetworkUtils.checkNetState(this)) {
            msign_id = et_sign_id.getText().toString();
            SPUtils.put(this, "msign_id", msign_id);
            mmobile = et_phone.getText().toString();
            SPUtils.put(this, "mmobile", mmobile);

            if (mmobile.length() != 11) {
                ToastUtil.makeText(this, "手机号输入有误，请确认后再次输入。");
                return;
            }
            mpassWord = et_password.getText().toString();
            SPUtils.put(this, "mpassWord", mpassWord);
            AsyncMultiLogin asyncLogin = new AsyncMultiLogin(this, mHandler, MESSAGE_LOGIN_ENTY, msign_id, mmobile, mpassWord);
            asyncLogin.execute();


        } else if (SPUtils.contains(this, "sign_id")) {
            msign_id = et_sign_id.getText().toString();
            if (msign_id.equals(sp_sign_id)) {
                IntentUtils.startActivity(MultiLoginActivity.this, MultipointActivity.class);
            } else {
                ToastUtil.makeText(this, "暂无网络，请联网后再试。");

            }
        } else {
            ToastUtil.makeText(this, "暂无网络，请联网后再试。");

        }
    }

    public void showDialog() {

        //将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(this)
                //对话框的标题
                //设定显示的View
                .setMessage(R.string.multi_introduce)                //对话框中的“登陆”按钮的点击事件
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                    }

                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(true)
                //对话框的创建、显示
                .create().show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_multi:
                init();
                break;
            case R.id.tv_multi_introduce:
                showDialog();
                break;
        }
//        IntentUtils.startActivity(this,SignListMoreActivity.class);

    }
}
