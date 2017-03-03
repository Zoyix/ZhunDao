package com.zhaohe.zhundao.wxapi;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhaohe.app.utils.CountDownTimerUtils;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.MakeRoundUntils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncBondPhone;
import com.zhaohe.zhundao.asynctask.AsyncGetCode;
import com.zhaohe.zhundao.asynctask.AsyncWXEntry;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.ToolBarActivity;
import com.zhaohe.zhundao.ui.ToolBarHelper;
import com.zhaohe.zhundao.ui.home.HomeActivity;

import static com.zhaohe.zhundao.ui.home.action.ActionOnFragment.MESSAGE_ACT_ALL;


public class WXEntryActivity extends ToolBarActivity implements IWXAPIEventHandler, View.OnClickListener {
    private IWXAPI api;
    public static final int MESSAGE_GET_ACCESS_TOKE = 100;
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
        initHandler();
        initToolBar("绑定手机", R.layout.activity_wxentry);
        initView();
        initWx();
        init();
        isLogin();
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

    //判断是否绑定过手机
    private void isBondPhone(ToolUserBean bean) {
//    JSONObject jsonObj = JSON.parseObject(result);
//    JSONArray jsonArray = jsonObj.getJSONArray("Data");

        if ((bean.getData().getMobile()) == null) {

        } else
            SPUtils.put(this, "accessKey", bean.getData().getUnionid());
        SPUtils.put(getApplicationContext(), "islogin", true);
        IntentUtils.startActivity(this, HomeActivity.class);

    }

    private void isLogin() {
        if ((boolean) SPUtils.get(this, "islogin", false) == true) {
            IntentUtils.startActivity(this, HomeActivity.class);
        }
    }

    //初始化微信信息
    private void initWx() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);

    }

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

    //发送code给异步类，获取access_taken
    private void init() {
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncWXEntry asyncWXEntry = new AsyncWXEntry(this, mHandler, dialog, MESSAGE_GET_ACCESS_TOKE, code);
            asyncWXEntry.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }

    private void sendCode() {
        String mPhone = et_phone_bond.getText().toString();

        if (((mPhone == null)) || (mPhone.length() != 11)) {
            Toast.makeText(WXEntryActivity.this, "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NetworkUtils.checkNetState(this)) {
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncGetCode getCode = new AsyncGetCode(this, mHandler, dialog, MESSAGE_GET_CODE, mPhone);
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
            Toast.makeText(WXEntryActivity.this, "您输入的验证码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (((mPhone == null)) || (mPhone.length() != 11)) {
            Toast.makeText(WXEntryActivity.this, "您输入的手机号码有误，请确认后重新输入~", Toast.LENGTH_SHORT).show();
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

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_GET_ACCESS_TOKE:
                        String result = (String) msg.obj;
//                        Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                        System.out.println("user message" + result);
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result, ToolUserBean.class);
                        SPUtils.put(getApplicationContext(), "accessKey", bean.getData().getUnionid());
                        Log.i("result", "" + bean.toString());
                        isBondPhone(bean);
//                        SPUtils.put(getApplicationContext(),"wx_result",result);
//                        isBondPhone((String) SPUtils.get(getApplicationContext(),"wx_result",""));
                    case MESSAGE_GET_CODE:
                        String result2 = (String) msg.obj;
                        ToolUserBean bean2 = (ToolUserBean) JSON.parseObject(result2, ToolUserBean.class);
//                        Toast.makeText(WXEntryActivity.this, bean2.getMsg(), Toast.LENGTH_LONG).show();
                        break;
                    case MESSAGE_BOND_PHONE:
                        String result3 = (String) msg.obj;
                        ToolUserBean bean3 = (ToolUserBean) JSON.parseObject(result3, ToolUserBean.class);
                        bean3.toString();
                        if (bean3.getRes() == 0) {
                            Toast.makeText(WXEntryActivity.this, "手机号绑定成功！", Toast.LENGTH_LONG).show();

                            SPUtils.put(getApplicationContext(), "islogin", true);
                            IntentUtils.startActivity(WXEntryActivity.this, HomeActivity.class);

                        } else {
                            Toast.makeText(WXEntryActivity.this, "验证码或手机错误，请核对后重试！", Toast.LENGTH_LONG).show();
                            return;
                        }
                    case MESSAGE_ACT_ALL:
                        String result4 = (String) msg.obj;

                        System.out.println("Activity result:  " + result4);
                        //活动列表结果
                        if (NetworkUtils.checkNetState(getApplicationContext())) {
                            SPUtils.put(getApplicationContext(), "act_result", result4);
                        }


                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            code = newResp.code.toString();
            System.out.println("WXcode："+code);
        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                break;

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_access_token:
//                init();
            case R.id.btn_send_code:
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
