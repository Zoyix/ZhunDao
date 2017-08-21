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
 * @Since:2016/12/16 16:03
 */
public class AsyncSign extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mSize = "200000";


    public AsyncSign(Context context, Handler handler, Dialog dialog, int request) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mDialog = dialog;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
    }
    public AsyncSign(Context context, Handler handler,int request) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.GetSignList;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey", mAccesskey);
        String param = new String();
        param = "pageSize=" + mSize;
        String result = HttpUtil.sendPostNew2request(path, map, "utf-8", param);
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
            System.out.println("获取签到列表" + result);
            mHandler.sendMessage(msg);
        } else {
//            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}
