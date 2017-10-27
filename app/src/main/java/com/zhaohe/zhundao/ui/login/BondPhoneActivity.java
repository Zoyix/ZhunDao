package com.zhaohe.zhundao.ui.login;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhaohe.app.utils.CountDownTimerUtils;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.MakeRoundUntils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.login.AsyncBondPhone;
import com.zhaohe.zhundao.asynctask.login.AsyncGetCode;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.home.HomeActivity;

public class BondPhoneActivity extends ToolBarActivity implements View.OnClickListener {
    public static final int MESSAGE_GET_CODE = 94;
    public static final int MESSAGE_BOND_PHONE = 95;


    private Handler mHandler;
    private String code;
    private Button btn_send_code, btn_bond_phone;
    private EditText et_phone_bond, et_code;
    private ImageView img_ico1;
    //需要传sendcode方法的参数
    private String mParam;
    //倒计时按钮工具类
    private CountDownTimerUtils mCountDownTimerUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        initToolBarNew("绑定手机",R.layout.activity_wxentry);
        initView();
        initHandler();
    }

        private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_GET_CODE:
                        String result2 = (String) msg.obj;
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
                        Toast.makeText(getApplicationContext(), bean.getMsg(), Toast.LENGTH_LONG).show();
                        break;
                    case MESSAGE_BOND_PHONE:
                        String result3 = (String) msg.obj;
                        ToolUserBean bean3 = (ToolUserBean) JSON.parseObject(result3, ToolUserBean.class);
                        bean3.toString();
                        if (bean3.getRes() == 0) {
                            Toast.makeText(BondPhoneActivity.this, "手机号绑定成功！", Toast.LENGTH_LONG).show();

                            SPUtils.put(getApplicationContext(), "islogin", true);
                            IntentUtils.startActivity(BondPhoneActivity.this, HomeActivity.class);

                        } else {
                            Toast.makeText(BondPhoneActivity.this, "验证码或手机错误，请核对后重试！", Toast.LENGTH_LONG).show();
                            return;
                        }



                    default:
                        break;
                }
            }
        };     }
        public void initView() {
                Button btn_get_access_token = (Button) findViewById(R.id.btn_get_access_token);
                btn_get_access_token.setOnClickListener(this);
                btn_bond_phone = (Button) findViewById(R.id.btn_bond_phone);
                btn_bond_phone.setOnClickListener(this);
                btn_send_code = (Button) findViewById(R.id.btn_send_code);
                btn_send_code.setOnClickListener(this);
                et_code = (EditText) findViewById(R.id.et_code);
                et_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);

                et_phone_bond = (EditText) findViewById(R.id.et_phone_bond);
                et_phone_bond.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                mCountDownTimerUtils = new CountDownTimerUtils(btn_send_code, 60000, 1000);
                img_ico1 = (ImageView) findViewById(R.id.img_ico1);
                Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.logo_login);
//        img_ico.setImageBitmap(toRoundBitmap(bmp));
                img_ico1.setImageBitmap(MakeRoundUntils.makeRoundCorner(bmp));
            }




            private void sendCode() {
                String mPhone = et_phone_bond.getText().toString();

                if (((mPhone == null)) || (mPhone.length() != 11)) {
                    Toast.makeText(BondPhoneActivity.this, "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NetworkUtils.checkNetState(this)) {
                    Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
                    AsyncGetCode getCode = new AsyncGetCode(this, mHandler,dialog, MESSAGE_GET_CODE, mPhone);
                    getCode.execute();


                } else {
                    ToastUtil.makeText(this, R.string.app_serviceError);
                }

            }
        private void bondPhone() {
        //手机号
        String mPhone = et_phone_bond.getText().toString();
        //验证码
        String mCode = et_code.getText().toString();
        if (((mCode == null)) || (mCode.length() != 6)) {
            Toast.makeText(BondPhoneActivity.this, "您输入的验证码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((mPhone == null)) || (mPhone.length() != 11)) {
            Toast.makeText(BondPhoneActivity.this, "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));


            AsyncBondPhone bondPhone = new AsyncBondPhone(this, mHandler, dialog, MESSAGE_BOND_PHONE, mPhone, mCode);
            bondPhone.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }

   public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_code:
                String mPhone = et_phone_bond.getText().toString();

                if (((mPhone == null)) || (mPhone.length() != 11)) {
                    Toast.makeText(BondPhoneActivity.this, "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCountDownTimerUtils.start();
                sendCode();
            case R.id.btn_bond_phone:
                bondPhone();

                break;
            default:
                break;
        }
    }

}
