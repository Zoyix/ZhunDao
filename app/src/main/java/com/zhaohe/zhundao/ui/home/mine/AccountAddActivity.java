package com.zhaohe.zhundao.ui.home.mine;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncGetUserInf;
import com.zhaohe.zhundao.asynctask.login.AsyncLogin;
import com.zhaohe.zhundao.bean.AccessKeyBean;
import com.zhaohe.zhundao.bean.AccountBean;
import com.zhaohe.zhundao.dao.MyAccountDao;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_GET_USERINF;
import static com.zhaohe.zhundao.ui.login.LoginActivity.MESSAGE_PHONE_ENTY;

public class AccountAddActivity extends ToolBarActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private MyAccountDao dao;
    private String accesskey, name, head, phone, passwrod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarNew("新增账号", R.layout.activity_account_add);
        ButterKnife.bind(this);
        initView();
        initHandler();

    }

    private void initView() {
        dao = new MyAccountDao(this);
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PHONE_ENTY:
                        String result = (String) msg.obj;
//                        DialogUtils.showDialog(LoginActivity.this, R.string.app_serviceReturnrRigth);
//                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        System.out.println("code" + result);
                        AccessKeyBean accessKeyBean = JSONUtils.parseObject(result, AccessKeyBean.class);
                        System.out.println("parseBeanObject()方法：accessKeyBean==" + "accessKeyBean" + accessKeyBean.getAccessKey() + "getMsg" + accessKeyBean.getMsg() + "getRes" + accessKeyBean.getRes());
                        Log.i("result", "" + accessKeyBean.toString());
                        if (accessKeyBean.getRes() == 0) {
//                            Constant.ACCESSKEY=accessKeyBean.getAccessKey();
                            accesskey = accessKeyBean.getAccessKey();
                            getUserInf();
//                            IntentUtils.startActivity(AccountAddActivity.this, HomeActivity.class);
                        } else {
                            ToastUtil.makeText(AccountAddActivity.this, "账号密码有误，请重新输入。");

                        }

                        break;
                    case MESSAGE_GET_USERINF:
                        String result3 = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result3);
                        JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));

                        name = jsonObject2.getString("NickName");
                        head = jsonObject2.getString("HeadImgurl");
                        AccountBean bean = new AccountBean();
                        bean.setPhone(phone);
                        bean.setStatus("false");
                        bean.setName(name);
                        bean.setHead(head);
                        bean.setAccessKey(passwrod);
                        dao.save(bean);
                        ToastUtil.makeText(AccountAddActivity.this, "添加成功");
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void getUserInf() {

        AsyncGetUserInf userInf = new AsyncGetUserInf(this, mHandler, MESSAGE_GET_USERINF, accesskey);
        userInf.execute();


    }

    private void init() {

        if (NetworkUtils.checkNetState(this)) {
            phone = etPhone.getText().toString();
            passwrod = etPassword.getText().toString();
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncLogin asyncLogin = new AsyncLogin(this, mHandler, dialog, MESSAGE_PHONE_ENTY, etPhone.getText().toString(), etPassword.getText().toString());
            asyncLogin.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        init();
    }
}
