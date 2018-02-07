package com.zhaohe.zhundao.ui;

import android.app.Application;
import android.content.Context;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yolanda.nohttp.NoHttp;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/3/1 14:05
 */
public class App extends Application {
    public static App _instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        UMShareAPI.get(this);
        _instance = this;
        //以下是nohttp配置
        NoHttp.init(this);
        context = getApplicationContext();

    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wxfe2a9da163481ba9", "ace26a762813528cc2dbb65b4279398e");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("587996696", "04da42626f03af7a63eac3e8a01a8d23", "https://sns.whalecloud.com/sina2/callback");
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
        PlatformConfig.setQQZone("1105950214", "GAFeY0k6OGdPe1nb");


    }

    public static App getInstance() {
        return _instance;
    }

    public static Context getContext() {
        return context;
    }
}
