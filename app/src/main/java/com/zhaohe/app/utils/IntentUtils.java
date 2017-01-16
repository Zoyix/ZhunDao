package com.zhaohe.app.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhaohe.zhundao.R;

import org.apache.http.message.BasicNameValuePair;


/**
 *@Description: 意图 工具类
 *@Author:杨攀
 *@Since:2014年6月6日下午2:21:03
 */
public class IntentUtils {

    /**
     *@Description:  activity 跳转
     *@Author:杨攀
     *@Since: 2014年6月6日下午2:23:31
     *@param activity 当前Activity对象  this
     *@param cls  目的地 Activity.class
     */
    public static void startActivity(Activity activity,Class<?> cls){
        startActivity (activity, cls, null);
    }
    
    /**
     *@Description: activity 跳转工具类
     *@Author:杨攀
     *@Since: 2014年7月2日上午10:20:06
     *@param activity 当前 Activity对象  this
     *@param cls 目的地 Activity.class
     *@param bundle 传递的参数
     */
    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle){
        Intent intent = new Intent ();
        intent.setClass (activity, cls);
        if (null != bundle) {
            intent.putExtras (bundle);
        }
        activity.startActivity (intent);
        activity.overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     *@Description: activity 跳转
     *@Author:杨攀
     *@Since: 2014年11月12日下午2:26:22
     *@param activity 当前Activity对象  this
     *@param cls 目的地 Activity.class
     *@param requestCode 请求code

     */
    public static void startActivityForResult(Activity activity,Class<?> cls,int requestCode){
        startActivityForResult (activity, cls, requestCode, null);
    }

    /**
     *@Description: activity 跳转
     *@Author:杨攀
     *@Since: 2014年11月12日下午2:26:22
     *@param activity 当前Activity对象  this
     *@param cls 目的地 Activity.class
     *@param requestCode 请求code
     *@param bundle 传递参数
     */
    public static void startActivityForResult(Activity activity,Class<?> cls,int requestCode,Bundle bundle){
        Intent intent = new Intent ();
        intent.setClass (activity, cls);
        if (null != bundle) {
            intent.putExtras (bundle);
        }
        activity.startActivityForResult (intent, requestCode);
        activity.overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     *@Description: fragment 跳转
     *@Author:杨攀
     *@Since: 2014年11月12日下午2:27:52
     *@param fragment 当前fragment对象  this
     *@param activity 目的地 Activity.class
     *@param cls 请求code
     *@param requestCode 传递参数
     */
    public static void startActivityForResult(Fragment fragment,Activity activity,Class<?> cls,int requestCode){
        startActivityForResult (fragment, activity, cls, requestCode, null);
    }

    /**
     *@Description: fragment 跳转
     *@Author:杨攀
     *@Since: 2014年11月12日下午2:27:52
     *@param fragment 当前fragment对象  this
     *@param activity 目的地 Activity.class
     *@param cls 请求code
     *@param requestCode 传递参数
     */
    public static void startActivityForResult(Fragment fragment,Activity activity,Class<?> cls,int requestCode,Bundle bundle){
        Intent intent = new Intent ();
        intent.setClass (activity, cls);
        if (null != bundle) {
            intent.putExtras (bundle);
        }
        fragment.startActivityForResult (intent, requestCode);
        activity.overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }



    /**
     *@Description:  activity 跳转工具类
     *@Author:杨攀
     *@Since: 2014年6月6日下午2:23:31
     *@param activity 当然 Activity对象  this
     *@param cls  目的地 Activity.class
     *@param name 传递的参数
     */
    public static void startActivityAddFlags(Activity activity, Class<?> cls,int[] flags, BasicNameValuePair... name){
        Intent intent = new Intent ();
        intent.setClass (activity, cls);
        if (flags != null) {
            for ( int i = 0 ; i < flags.length ; i++ ) {
                intent.addFlags (flags[i]);
            }
        }
        if (null != name) {
            for ( int i = 0 ; i < name.length ; i++ ) {
                intent.putExtra (name[i].getName (), name[i].getValue ());
            }
        }
        activity.startActivity (intent);
        activity.overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }
    
}
