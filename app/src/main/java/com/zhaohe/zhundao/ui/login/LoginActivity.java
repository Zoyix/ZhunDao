package com.zhaohe.zhundao.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncLogin;
import com.zhaohe.zhundao.bean.AccessKeyBean;
import com.zhaohe.zhundao.constant.Constant;
import com.zhaohe.zhundao.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //登录动作（暂时）
    public static final int MESSAGE_WX_ENTY = 100;
    public static String uuid = null;
    private IWXAPI api;
    private Button btn_login;
    private ImageView btn_login_wechat;
    private Handler mHandler;
    private String mmobile;
    private String mpassWord;
    private EditText et_phone;
    private EditText et_password;
    private ImageView img_ico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isLogin();
        initHandler();
        initView();
        regToWechat();
    }

    private void isLogin() {
        if ((boolean) SPUtils.get(this, "islogin", false) == true)
        {
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
    //执行异步耗时信息
    private void init() {
        if (NetworkUtils.checkNetState(this)) {
            mmobile=et_phone.getText().toString();
            mpassWord=et_password.getText().toString();
            Dialog dialog = ProgressDialogUtils.showProgressDialog(this, getString(R.string.progress_title), getString(R.string.progress_message));
            AsyncLogin asyncLogin = new AsyncLogin(this, mHandler, dialog, MESSAGE_WX_ENTY, mmobile, mpassWord);
            asyncLogin.execute();


        } else {
            ToastUtil.makeText(this, R.string.app_serviceError);
        }
    }
    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_login_wechat = (ImageView) findViewById(R.id.btn_login_wechat);
        btn_login_wechat.setOnClickListener(this);
        et_phone= (EditText) findViewById(R.id.et_phone);
        et_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_password= (EditText) findViewById(R.id.et_password);
        img_ico= (ImageView) findViewById(R.id.img_ico);
        Resources res = getResources();
        Bitmap    bmp = BitmapFactory.decodeResource(res, R.mipmap.logo_login);
//        img_ico.setImageBitmap(toRoundBitmap(bmp));
       img_ico.setImageBitmap(makeRoundCorner(bmp));
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_WX_ENTY:
                        String result = (String) msg.obj;
//                        DialogUtils.showDialog(LoginActivity.this, R.string.app_serviceReturnrRigth);
//                        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        System.out.println("code"+result);
                        AccessKeyBean accessKeyBean= JSONUtils.parseObject(result,AccessKeyBean.class);
                        System.out.println("parseBeanObject()方法：accessKeyBean=="+"accessKeyBean"+accessKeyBean.getAccessKey()+"getMsg"+accessKeyBean.getMsg()+"getRes"+accessKeyBean.getRes());
                        Log.i("result",""+accessKeyBean.toString());
                        if(accessKeyBean.getRes()==0)
                        {
                            ToastUtil.makeText(LoginActivity.this,"登录成功");
//                            Constant.ACCESSKEY=accessKeyBean.getAccessKey();
                            SPUtils.put(getApplicationContext(),"accessKey",accessKeyBean.getAccessKey());
                            SPUtils.put(getApplicationContext(),"islogin",true);
                            IntentUtils.startActivity(LoginActivity.this, HomeActivity.class);
                        }
                        else
                        {
                            ToastUtil.makeText(LoginActivity.this,"账号密码有误，请重新输入。");

                        }


//                        AccessKeyBean bean = JSONUtils.parseObject(result,AccessKeyBean.class);
//                        Log.i("test",""+bean.toString());
//                        NoticeBean bean = JSONUtils.parseObject (result, NoticeBean.class);
//                        if (bean != null) {
//                            showData (bean);
//                        } else {
//                            DialogUtils.showDialog (NoticeViewActivity.this, R.string.app_serviceReturnError);
//                        }
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

    public  Bitmap makeRoundCorner(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
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
                    sentToWechat();
                    Toast.makeText(LoginActivity.this, "发送登录微信请求，请稍等。", Toast.LENGTH_LONG).show();

                }
                break;

            default:
                break;
        }
    }
}
