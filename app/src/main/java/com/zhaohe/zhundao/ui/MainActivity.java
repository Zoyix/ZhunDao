package com.zhaohe.zhundao.ui;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.utils.IntentUtils;
import com.zhaohe.app.utils.NetHelper;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.version.VersionBean;
import com.zhaohe.app.version.VersionXmlUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.home.HomeActivity;
import com.zhaohe.zhundao.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SPUtils.clear(this);

//        goAsyncUpdate();
        isLogin();
        ;
    }

    private void isLogin() {
        //判断是islogin是否有值
        if (SPUtils.contains(this, "islogin")) {
        } else SPUtils.put(this, "islogin", false);
//判断是否已经登录
        if ((boolean) SPUtils.get(this, "islogin", true) == false)
            IntentUtils.startActivity(this, LoginActivity.class);
        else IntentUtils.startActivity(this, HomeActivity.class);

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

    /**
     * @Description: 检查App 是否需要更新
     * @Author:杨攀
     * @Since:2014年11月17日下午4:31:31
     */
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
            VersionBean bean = null;

            if (result != null) {
                try {

                    bean = VersionXmlUtils.parserVersionXml(result);

//              Synccode=0 忽略更新 1 用户选择 2 强制更新
                    if (bean.getSynccode().equals("0")) {
                        isLogin();

                    }
//                    VersionXmlUtils.isUpdateApp (SignListMoreActivity.this, bean)
                    if (VersionXmlUtils.isUpdateApp(MainActivity.this, bean)) {// 更新App
                        DialogUtils.showDialog(MainActivity.this, R.string.app_updateApp_message, new UpdateAppPositiveButtonListener(bean),
                                new UpdateAppNegativeButtonListener());
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //其他情况，这进行登录
            isLogin();
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
            VersionXmlUtils.startUpdateApp(MainActivity.this, bean);
            isLogin();
//            SignListMoreActivity.this.finish ();
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
            isLogin();
//            SignListMoreActivity.this.finish ();
//            System.exit (0);
        }

    }
}


