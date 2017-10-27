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
 * @Since:2017/8/15 11:06
 */
public class AsyncInf extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mmobile;
    private String mpassWord;

    public AsyncInf(Context context, Handler handler, int request) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;

    }

    @Override
    protected String doInBackground(String... params) {
        String path = (String) SPUtils.get(mContext, "HOST", (String) SPUtils.get(mContext, "HOST", Constant.HOST)) + Constant.Url.GetNoticeList;

        Map<String, String> map = new HashMap<String, String>();


        String mParam = "pageSize=" + "2000";
        String result = HttpUtil.sendPostNew2request(path, map, "utf-8", mParam);
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
            System.out.println("获取准到通知列表" + result);

            mHandler.sendMessage(msg);
        } else {
//            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}
