package com.zhaohe.zhundao.mywifidemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by caojun on 2017/9/11.
 */

public class WifiUtils {
    public static boolean isWiFiActive(Context context) {
        //判断当前wifi是否打开
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取WiFi列表
     *
     * @param context ScanResult   Math.abs(scanResult.level) 表示信号强度
     * @return
     */
    public static List<ScanResult> getScanWifiInfo(Context context) {
       /* if (Build.VERSION.SDK_INT >= 23) {
            try {
                Settings.Secure.putInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        return wifiManager.getScanResults();
    }

    /**
     * 连接到指定的WiFi
     *
     * @param context
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    public static WifiConfiguration createWifiConfiguration(Context context, String SSID, String Password, int Type) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.priority = 1;
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = IsExsits(context, SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) //WIFICIPHER_NOPASS
        {
            //config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //config.wepTxKeyIndex = 0;
        }
        if (Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }


    /**
     * 判断当前是否存在该WiFi
     *
     * @param context
     * @param SSID
     * @return
     */
    private static WifiConfiguration IsExsits(Context context, String SSID) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        try {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("/" + SSID + "/")) {
                    return existingConfig;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static boolean connect2Wifi(Context context, WifiConfiguration configuration) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int netId = wifiManager.addNetwork(configuration);
        return wifiManager.enableNetwork(netId, true);
    }

    public static String getNowSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }


}
