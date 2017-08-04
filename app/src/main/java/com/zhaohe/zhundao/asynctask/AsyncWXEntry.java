package com.zhaohe.zhundao.asynctask;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:发送微信code到服务端，获取
 * @Author:邹苏隆
 * @Since:2016/12/1 10:31
 */
public class AsyncWXEntry extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mcode;

    public AsyncWXEntry(Context context, Handler handler,int request, String code) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
//        this.mDialog = dialog;
        this.mcode = code;


    }

    @Override
    protected String doInBackground(String... params) {
        String path = Constant.HOST_MOBLIE + Constant.Url.GetAccessToken;
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", mcode);
        map.put("type", "android");
        String result = HttpUtil.sendPostNewrequest(path, map, "utf-8");
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
            System.out.println("获取微信登录权限" + result);

            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }


}

