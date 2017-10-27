package com.zhaohe.zhundao.ui.login;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhaohe.app.utils.CountDownTimerUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.login.register.AsyncRegisterByPhone;
import com.zhaohe.zhundao.asynctask.login.register.AsyncSendVcode;
import com.zhaohe.zhundao.asynctask.login.register.AsyncVerifyPhone;
import com.zhaohe.zhundao.asynctask.login.register.AsyncVerifyPhoneAndCode;
import com.zhaohe.zhundao.asynctask.login.register.AysncUpdatePassWordByPhone;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends ToolBarActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private Handler mHandler;
    private CountDownTimerUtils mCountDownTimerUtils;
    public static final int MESSAGE_GET_CODE = 94;
    public static final int MESSAGE_VERIFY_PHONE = 95;
    public static final int MESSAGE_VERIFY_PHONE_CODE = 96;
    public static final int MESSAGE_REGISTER = 97;
    public static final int MESSAGE_FIND_BACK = 98;

    private boolean phone_exit;


    String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("注册账号和密码找回", R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initHandler();

    }

    private void initView() {
        mCountDownTimerUtils = new CountDownTimerUtils(btnSend, 60000, 1000);

    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_VERIFY_PHONE:
                        String result2 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        if (bean.isSucess()) {
                            Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();

                            phone_exit=false;
                        }
                        else {
                            phone_exit=true;
                            Toast.makeText(getApplicationContext(), "请发送验证码，找回密码", Toast.LENGTH_LONG).show();

                        }
                        btnLogin.setVisibility(View.GONE);
                        btnSend.setVisibility(View.VISIBLE);
                        break;
                    case MESSAGE_GET_CODE:
                        String result3 = (String) msg.obj;
                        ToolUserBean bean3 = (ToolUserBean) JSON.parseObject(result3, ToolUserBean.class);
                        if (bean3.isSucess()) {
                            etCode.setVisibility(View.VISIBLE);
                            btnSubmit.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MESSAGE_VERIFY_PHONE_CODE:
                        result2 = (String) msg.obj;
                        bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();

                        if (bean.isSucess()) {
                            btnSubmit.setVisibility(View.GONE);
                            btnSend.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                            etCode.setVisibility(View.GONE);
                            etPhone.setVisibility(View.GONE);
                            etPassword.setVisibility(View.VISIBLE);
                            if (!phone_exit){
                            etName.setVisibility(View.VISIBLE);}

                        }
                        break;
                    case MESSAGE_REGISTER:
                        result2 = (String) msg.obj;
                        bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();
                        if (bean.isSucess()) {
                   finish();

                        }
                        break;
                    case MESSAGE_FIND_BACK:
                        result2 = (String) msg.obj;
                        bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();
                        if (bean.isSucess()) {
                            finish();

                        }
                        break;
                    default:

                        break;
                }
            }
        };
    }

    private void sendCode() {

        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncSendVcode getCode = new AsyncSendVcode(this, mHandler, dialog, MESSAGE_GET_CODE, mPhone);
            getCode.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }

    }

    private void verifyCode() {
        String vcode = etCode.getText().toString();
        if (vcode.length() == 0) {
            Toast.makeText(getApplicationContext(), "验证码不得为空~", Toast.LENGTH_SHORT).show();

            return;
        }
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncVerifyPhoneAndCode getCode = new AsyncVerifyPhoneAndCode(this, mHandler, dialog, MESSAGE_VERIFY_PHONE_CODE, mPhone, vcode);
            getCode.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }

    }
    private void registe() {
        String name = etName.getText().toString();
        if (name.length() == 0) {
            Toast.makeText(getApplicationContext(), "姓名不得为空", Toast.LENGTH_SHORT).show();

            return;
        }
        String password = etPassword.getText().toString();
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "密码至少6位", Toast.LENGTH_SHORT).show();
            return;
        }
        String param="Name=" + name + "&Phone=" + mPhone+"&PassWord="+password;
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncRegisterByPhone getCode = new AsyncRegisterByPhone(this, mHandler, dialog, MESSAGE_REGISTER, param);
            getCode.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }

    }
    @OnClick({R.id.btn_login, R.id.btn_send, R.id.btn_submit,R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String Phone = etPhone.getText().toString();
                if (Phone.length() != 11) {
                    Toast.makeText(getApplicationContext(), "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
                    return;
                }

                Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
                AsyncVerifyPhone getCode = new AsyncVerifyPhone(this, mHandler, dialog, MESSAGE_VERIFY_PHONE, Phone);
                mPhone = Phone;
                getCode.execute();
                break;
            case R.id.btn_send:
                mCountDownTimerUtils.start();
                sendCode();
                break;
            case R.id.btn_submit:
                verifyCode();
                break;
            case R.id.btn_register:
                if(!phone_exit){registe();}
                else{
                    findback();
                }
                break;
        }
    }

    private void findback() {

        String password = etPassword.getText().toString();
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "密码至少6位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AysncUpdatePassWordByPhone getCode = new AysncUpdatePassWordByPhone(this, mHandler, dialog, MESSAGE_FIND_BACK, mPhone,password);
            getCode.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }


}