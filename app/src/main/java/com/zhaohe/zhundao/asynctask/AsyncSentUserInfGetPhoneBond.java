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
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/4/1 10:10
 */
public class AsyncSentUserInfGetPhoneBond extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String access_token;
    private String openid;
    public AsyncSentUserInfGetPhoneBond(Context context, Handler handler, int request, String Param,String openid) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
//        this.mDialog = dialog;
        this.access_token = Param;
this.openid=openid;

    }
    @Override
    protected String doInBackground(String... params) {
        String path = Constant.HOST_MOBLIE + Constant.Url.SentWxUserInf;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token",access_token);
        map.put("openid",openid);
        String result = HttpUtil.sendGET2Request(path, map, "utf-8");
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
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}