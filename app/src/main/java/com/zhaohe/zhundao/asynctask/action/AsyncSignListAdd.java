package com.zhaohe.zhundao.asynctask.action;

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
 * @Description:添加报名人员
 * @Author:邹苏隆
 * @Since:2017/6/20 16:05
 */
public class AsyncSignListAdd extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;
    private String activityFeeid;

    public AsyncSignListAdd(Context context, Handler handler, int request, String activityFeeid,String Param) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
        this.mParam=Param;
        this.activityFeeid=activityFeeid;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.AddActivityList;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey", mAccesskey);
        map.put("activityFeeid", activityFeeid);

        String result = HttpUtil.sendPostNew2request(path, map, "utf-8",mParam);
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
            System.out.println("添加报名人员" + result);
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}