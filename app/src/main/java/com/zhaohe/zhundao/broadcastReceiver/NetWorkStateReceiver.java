package com.zhaohe.zhundao.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.asynctask.AsyncUpLoadSignupStatus;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.dao.MySignupListDao;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/7/26 14:27
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    private Handler mHandler;
    public static final int MESSAGE_UPLOAD_SIGNUPSTATUS = 88;
    private MySignupListDao dao;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("网络状态发生变化");
        init(context);
        initHandler(context);

        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
                upload(context);

            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
                upload(context);

            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
                upload(context);

            } else {
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
//API大于23时使用下面的方式进行网络监听
        }else {

            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i=0; i < networks.length; i++){
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
            }
            upload(context);
//            Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void init(Context context) {
        dao = new MySignupListDao(context);

    }

    private void upload(Context context) {

        if (NetworkUtils.checkNetState(context)) {
            List<MySignListupBean> list = dao.queryUpdateStatus();
            String jsonString = JSON.toJSONString(list);
            if (list.size() == 0) {
                ToastUtil.print("已是最新数据");
                return;
            }

            AsyncUpLoadSignupStatus async = new AsyncUpLoadSignupStatus(context, mHandler, MESSAGE_UPLOAD_SIGNUPSTATUS, jsonString);
            async.execute();

    }}
    private void initHandler(final Context context) {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case MESSAGE_UPLOAD_SIGNUPSTATUS:
                        String result4 = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result4);
                        if (jsonObj.getString("Res") == "0") {
                            changeStatus();
                            ToastUtil.makeText(context, "数据上传成功");
                        }
                        break;


                    default:
                        break;
                }
            }
        };
    }
    private void changeStatus() {
        List<MySignListupBean> list = dao.queryUpdateStatus();
        for (int i = 0; i < list.size(); i++) {
            MySignListupBean bean2 = (MySignListupBean) list.get(i);
            MySignListupBean bean = new MySignListupBean();
            bean.setVCode(bean2.getVCode());
            bean.setStatus("true");
            bean.setUpdateStatus("false");
            bean.setCheckInID(bean2.getCheckInID());
            dao.update(bean);
        }

    }
}