package com.zhaohe.app.version;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Xml;
import android.widget.Toast;

import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.service.UpdateAppService;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Description: 版本信息 解析 工具类
 * @Author:杨攀
 * @Since:2014年11月17日下午4:07:50
 */
public class VersionXmlUtils {

    private static StringBuilder handSetInfo = new StringBuilder();

    /**
     * @param xml
     * @return
     * @throws Exception
     * @Description: 解析 版本信息 XML
     * @Author:杨攀
     * @Since: 2014年11月17日下午4:37:17
     */
    public static VersionBean parserVersionXml(String xml) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        return parserVersionXml(inputStream);
    }

    /**
     * @param xml
     * @return
     * @throws Exception
     * @Description: 解析 版本信息 XML
     * @Author:杨攀
     * @Since: 2014年8月6日下午12:45:34
     */
    public static VersionBean parserVersionXml(InputStream xml) throws Exception {
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8");// 为Pull解析器设置要解析的XML数据
        int event = pullParser.getEventType();

        VersionBean bean = null;
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    bean = new VersionBean();
                    break;
                case XmlPullParser.START_TAG:
                    if ("versioncode".equals(pullParser.getName())) {
                        String code = pullParser.nextText();// 获取版本号
                        bean.setVersionCode(Integer.parseInt(code));
                    } else if ("apkname".equals(pullParser.getName())) {
                        String apkname = pullParser.nextText();// 获取apk名称
                        bean.setApkName(apkname);
                    } else if ("url".equals(pullParser.getName())) {
                        String url = pullParser.nextText();// 获取url路径
                        bean.setUrl(url);
                    } else if ("description".equals(pullParser.getName())) {
                        String description = pullParser.nextText();// 获取该文件的信息
                        bean.setDescription(description);
                    } else if ("synccode".equals(pullParser.getName())) {
                        String synccode = pullParser.nextText();// 获取是否需要同步code
                        bean.setSynccode(synccode);
                    }
                    break;
            }
            event = pullParser.next();
        }
        return bean;
    }

    /**
     * @param context
     * @param xml     版本的xml
     * @return
     * @Description: 判断是否需要更新App
     * @Author:杨攀
     * @Since: 2014年11月17日下午4:16:31
     */
    public static boolean isUpdateApp(Context context, VersionBean bean) {

        try {
            if (bean.getVersionCode() > getVersionCode(context)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param activity
     * @param bean
     * @Description: 开始更新 App
     * @Author:杨攀
     * @Since: 2014年11月17日下午4:46:06
     */
    public static void startUpdateApp(Activity activity, VersionBean bean) {
        // 更新App
        Intent updateIntent = new Intent(activity, com.zhaohe.zhundao.service.UpdateAppService.class);
        if (bean.getApkName()==null){
            return;
        }
        updateIntent.putExtra("app_name", bean.getApkName());
        updateIntent.putExtra("app_path", bean.getUrl());
        activity.startService(updateIntent);
    }

    /**
     * @return
     * @Description: 获取手机信息
     * @Author:杨攀
     * @Since: 2014年6月20日下午5:35:44
     */
    public static final String getHandSetInfo() {
        if (handSetInfo.length() == 0) {
            handSetInfo.append("手机型号:").append(android.os.Build.MODEL);
            handSetInfo.append("SDK版本:").append(android.os.Build.VERSION.SDK);
            handSetInfo.append("系统版本:").append(android.os.Build.VERSION.RELEASE);
        }
        return handSetInfo.toString();
    }

    /**
     * @param context
     * @return
     * @throws Exception
     * @Description: 获取当前程序的版本名称
     * @Author:杨攀
     * @Since: 2014年8月6日下午12:35:04
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     * @Description: 获取当前程序的版本号(内部识别号)
     * @Author:杨攀
     * @Since: 2014年8月6日下午12:35:04
     */
    public static int getVersionCode(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionCode;
    }

    /**
     * @param VersionCode
     * @return
     * @Description: 判断当前版本是否兼容目标版本的方法
     * @Author:杨攀
     * @Since: 2014年6月17日下午2:23:08
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
}
