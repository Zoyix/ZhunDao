package com.zhaohe.zhundao.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncSentUserInfGetPhoneBond;
import com.zhaohe.zhundao.asynctask.login.AsyncLogin;
import com.zhaohe.zhundao.bean.AccessKeyBean;
import com.zhaohe.zhundao.bean.ToolUserBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.home.HomeActivity;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //登录动作（暂时）
    public static final int MESSAGE_PHONE_ENTY = 100;
    public static final int MESSAGE_WX_ENTY = 99;

    public static String uuid = null;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_reg)
    TextView tvReg;
    private IWXAPI api;
    private Button btn_login;
    private ImageView btn_login_wechat;
    private Handler mHandler;
    private String mmobile;
    private String mpassWord;
    private EditText et_phone;
    private EditText et_password;
    private ImageView img_ico;
    private UMShareAPI mShareAPI = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        isLogin();
        initHandler();
        initView();
        regToWechat();
    }

    private void isLogin() {
        if ((boolean) SPUtils.get(this, "islogin", false) == true) {
            IntentUtils.startActivity(this, HomeActivity.class);
        }
    }


    //注册到微信
    private void regToWechat() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
    }

    private void sentToWechat() {
        SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信息
        req.scope = "snsapi_userinfo";
        //自定义信息
        req.state = "wechat_sdk_demo_test";
        //向微信发送请求
        api.sendReq(req);
    }

    private void checkUser(String access_token, String openid) {
        AsyncSentUserInfGetPhoneBond asyncLogin = new AsyncSentUserInfGetPhoneBond(this, mHandler, MESSAGE_WX_ENTY, access_token, openid);
        asyncLogin.execute();

    }

    //执行异步耗时信息
    private void init() {

        if (NetworkUtils.checkNetState(this)) {
            mmobile = et_phone.getText().toString();
            mpassWord = et_password.getText().toString();
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncLogin asyncLogin = new AsyncLogin(this, mHandler, dialog, MESSAGE_PHONE_ENTY, mmobile, mpassWord);
            asyncLogin.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }

    private void initView() {
        mShareAPI = UMShareAPI.get(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_login_wechat = (ImageView) findViewById(R.id.btn_login_wechat);
        btn_login_wechat.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_password = (EditText) findViewById(R.id.et_password);
//        et_password.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        img_ico = (ImageView) findViewById(R.id.img_ico);
//        Resources res = getResources();
//        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.logo_login);
////        img_ico.setImageBitmap(toRoundBitmap(bmp));
//        img_ico.setImageBitmap(makeRoundCorner(bmp));
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
                            ToastUtil.makeText(LoginActivity.this, "登录成功");
//                            Constant.ACCESSKEY=accessKeyBean.getAccessKey();
                            SPUtils.put(getApplicationContext(), "accessKey", accessKeyBean.getAccessKey());
                            SPUtils.put(getApplicationContext(), "islogin", true);
                            IntentUtils.startActivity(LoginActivity.this, HomeActivity.class);
                        } else {
                            ToastUtil.makeText(LoginActivity.this, "账号密码有误，请重新输入。");

                        }

                        break;
                    case MESSAGE_WX_ENTY:
                        result = (String) msg.obj;
//                        Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                        System.out.println("user message" + result);
                        ToolUserBean bean = (ToolUserBean) JSON.parseObject(result, ToolUserBean.class);
                        SPUtils.put(getApplicationContext(), "accessKey", bean.getData().getUnionid());
                        Log.i("result", "" + bean.toString());
                        isBondPhone(bean);
//                        SPUtils.put(getApplicationContext(),"wx_result",result);
//                        isBondPhone((String) SPUtils.get(getApplicationContext(),"wx_result",""));
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "微信登录成功！", Toast.LENGTH_SHORT).show();
//           String nickname =data.get("screen_name");
//            int sex;
//            String province =data.get("province");
//String city=data.get("city");
//            String country =data.get("country ");
//
//            if (data.get("gender").equals("男")){
//   sex=1;
//}
//            if (data.get("gender").equals("女")){
//                sex=2;
//            }
//            else{
//                sex=0;
//            }
            checkUser(data.get("accessToken"), data.get("openid"));
//            ToastUtil.makeText(getApplicationContext(),name);
//            Set set = data.entrySet();
//
//            for(Iterator iter = set.iterator(); iter.hasNext();)
//            {
//                Map.Entry entry = (Map.Entry)iter.next();
//
//                String key = (String)entry.getKey();
//                String value = (String)entry.getValue();
//                System.out.println(key +" :" + value);
//            }
//            map.get accessToken
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "微信授权失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "取消微信授权！", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    //判断是否绑定过手机
    private void isBondPhone(ToolUserBean bean) {
//    JSONObject jsonObj = JSON.parseObject(result);
//    JSONArray jsonArray = jsonObj.getJSONArray("Data");
        System.out.println("手机号码" + bean.getData().getMobile());
//        if (null==(bean.getData().getMobile()) ) {
//            IntentUtils.startActivity(this, BondPhoneActivity.class);
//
//        } else
        SPUtils.put(this, "accessKey", bean.getData().getUnionid());
        SPUtils.put(getApplicationContext(), "islogin", true);
        IntentUtils.startActivity(this, HomeActivity.class);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //测试密码
//                mmobile = "18368179002";
//                mpassWord = "111111";
                init();
                break;
            case R.id.btn_login_wechat:
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(LoginActivity.this, "未安装微信客户端，请先下载。", Toast.LENGTH_LONG).show();

                    return;
                } else {
//                    sentToWechat();
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, umAuthListener);
//                    mShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, umAuthListener);

                    Toast.makeText(LoginActivity.this, "发送登录微信请求，请稍等。", Toast.LENGTH_LONG).show();

                }
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_reg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                break;
            case R.id.tv_reg:
                IntentUtils.startActivity(this,RegisterActivity.class);
                break;
        }
    }
}
