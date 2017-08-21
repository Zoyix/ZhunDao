package com.zhaohe.zhundao.asynctask;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:绑定摇一摇设备
 * @Author:邹苏隆
 * @Since:2017/3/17 11:41
 */
public class AsyncBeaconBond extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;//deviceId 扫码获得beaconID
    private String mType;//	0绑定摇一摇设备 1解除绑定摇一摇


    public AsyncBeaconBond(Context context, Handler handler,  int request, String param,String Type) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
//        this.mDialog = dialog;
        this.mParam = param;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
        this.mType=Type;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.AddBeacon;
        Map<String, String> map = new HashMap<String, String>();
        map.put("deviceId", mParam);
        map.put("accessKey", mAccesskey);
        map.put("type",mType);
        String result = HttpUtil.sendGETRequest(path, map, "utf-8");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (result != null) {
            Message msg = mHandler.obtainMessage(mRequest);
            msg.obj = result;
            System.out.println("绑定/解除摇一摇设备" + result);
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}

