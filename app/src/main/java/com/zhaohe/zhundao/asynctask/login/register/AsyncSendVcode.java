package com.zhaohe.zhundao.asynctask.login.register;

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
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/10/23 15:59
 */
public class AsyncSendVcode extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mmobile;
    private String mpassWord;

    public AsyncSendVcode(Context context, Handler handler, Dialog dialog, int request, String mobile) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mDialog = dialog;
        this.mmobile = mobile;
    }

    @Override
    protected String doInBackground(String... params) {
        String path = (String) SPUtils.get(mContext,"HOST", Constant.HOST) + Constant.Url.SendVcode;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", mmobile);
        map.put("from","Android");

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
            System.out.println("发送手机注册验证码\n" +result);

            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }

}