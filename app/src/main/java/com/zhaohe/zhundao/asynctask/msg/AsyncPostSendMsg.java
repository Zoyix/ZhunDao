package com.zhaohe.zhundao.asynctask.msg;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/11/15 10:38
 */

public class AsyncPostSendMsg extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;
    private String mID;
    private String mContent;

    public AsyncPostSendMsg(Context context, Handler handler, Dialog dialog, int request, String ID, String content, String param) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mDialog = dialog;
        this.mParam = param;
        this.mID = ID;
        this.mContent = content;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST_MSG + Constant.Url.BatchSendSms;
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", mID);
        map.put("content", mContent);
        map.put("from", Constant.Url.Device);
        String result = HttpUtil.sendPostNew2request(path, map, "utf-8", mParam);
        ToastUtil.print(mParam);
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
            System.out.println("发送短信" + result);
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }

}