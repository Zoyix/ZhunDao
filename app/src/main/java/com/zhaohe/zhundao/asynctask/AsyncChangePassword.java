package com.zhaohe.zhundao.asynctask;

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
 * @Description:修改密码
 * @Author:邹苏隆
 * @Since:2016/12/29 16:50
 */
public class AsyncChangePassword extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private String mAccesskey;
    private String mParam;

    public AsyncChangePassword(Context context, Handler handler, int request, String param) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam = param;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.UpdatePassWord;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey", mAccesskey);
        map.put("passWord", mParam);
        String result = HttpUtil.sendPostNewrequest(path, map, "utf-8");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            Message msg = mHandler.obtainMessage(mRequest);
            msg.obj = result;
            System.out.println("修改用户密码" + result);
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}

