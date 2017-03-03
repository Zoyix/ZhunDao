package com.zhaohe.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @Description: 网络工具类
 * @Author:杨攀
 * @Since:2014年11月12日上午10:51:53
 */
public class NetworkUtils {

    private static String LOG_TAG = "NetWorkHelper";

    public static Uri uri = Uri.parse("content://telephony/carriers");

    /**
     * @param context
     * @return
     * @Description: 判断是否有网络连接--有网络，但不一定是连接的
     * @Author:杨攀
     * @Since: 2014年7月24日下午3:08:11
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            Log.w(LOG_TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].isAvailable()) {
                        Log.d(LOG_TAG, "network is available");
                        return true;
                    }
                }
            }
        }
        Log.d(LOG_TAG, "network is not available");
        return false;
    }

    /**
     * @param context
     * @return
     * @Description: 判断是网络的状态
     * @Author:杨攀
     * @Since: 2014年7月24日下午3:07:41
     */
    public static boolean checkNetState(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }

    /**
     * @param context
     * @return
     * @Description: 判断网络是否为漫游
     * @Author:杨攀
     * @Since: 2014年7月24日下午3:08:33
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(LOG_TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming()) {
                    Log.d(LOG_TAG, "network is roaming");
                    return true;
                } else {
                    Log.d(LOG_TAG, "network is not roaming");
                }
            } else {
                Log.d(LOG_TAG, "not using mobile network");
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     * @Description: 判断 MOBILE网络（GPRS网络）是否可用
     * @Author:杨攀
     * @Since: 2014年7月24日下午3:08:46
     */
    public static boolean isMobileDataEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;

        isMobileDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return isMobileDataEnable;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     * @Description: 判断 wifi 是否可用
     * @Author:杨攀
     * @Since: 2014年7月24日下午3:09:20
     */
    public static boolean isWifiDataEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

}
