package com.zhaohe.zhundao.asynctask.login.register;

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
 * @Since:2017/10/23 16:39
 */
public class AsyncRegisterByPhone extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;


    public AsyncRegisterByPhone(Context context, Handler handler,Dialog dialog, int request, String param) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam = param;
        this.mDialog=dialog;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST", Constant.HOST) + Constant.Url.RegisterByPhone;
        Map<String, String> map = new HashMap<String, String>();


        String result = HttpUtil.sendPostNew2request(path, map, "utf-8", mParam);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            Message msg = mHandler.obtainMessage(mRequest);
            msg.obj = result;
            System.out.println("手机注册账号" + result);
            mHandler.sendMessage(msg);
        } else {
//            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}