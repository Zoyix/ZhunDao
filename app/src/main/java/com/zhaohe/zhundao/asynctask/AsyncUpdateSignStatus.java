package com.zhaohe.zhundao.asynctask;

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
 * @Since:2016/12/23 14:57
 */
public class AsyncUpdateSignStatus extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int     mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;

    public AsyncUpdateSignStatus(Context context, Handler handler, int request,String mParam){
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam=mParam;
        this.mAccesskey= (String) SPUtils.get(mContext,"accessKey","");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST + Constant.Url.UpdateCheckIn;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey",mAccesskey);
        map.put("CheckInID",mParam);
        String result = HttpUtil.sendGETRequest(path,map,"utf-8");
        return result;
    }
    @Override
    protected void onPostExecute(String result){

        if (result != null) {
            Message msg = mHandler.obtainMessage (mRequest);
            msg.obj = result;
            System.out.println("wtf"+result);
            mHandler.sendMessage (msg);
        } else {
            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}
