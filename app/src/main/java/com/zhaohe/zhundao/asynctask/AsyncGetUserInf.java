package com.zhaohe.zhundao.asynctask;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/22 15:ic_launcher
 */
public class AsyncGetUserInf extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;

    public AsyncGetUserInf(Context context, Handler handler, int request) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
//        this.mDialog = dialog;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.GetUserInf;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey", mAccesskey);
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
            System.out.println("获用户信息" + result);
            mHandler.sendMessage(msg);
        } else {
//            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}
