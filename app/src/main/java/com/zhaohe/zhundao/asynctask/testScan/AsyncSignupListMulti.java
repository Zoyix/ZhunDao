package com.zhaohe.zhundao.asynctask.testScan;

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
 * @Since:2016/12/21 13:59
 */
public class AsyncSignupListMulti extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int     mRequest;
    private Dialog mDialog;
    private String mAccesskey;
//    签到ID
    private String mParam;

    public AsyncSignupListMulti(Context context, Handler handler, int request, String param){
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam=param;
        this.mAccesskey= (String) SPUtils.get(mContext,"accessKey_multi","");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST + Constant.Url.PostCheckInList;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey",mAccesskey);
        String result = HttpUtil.sendPostNew2request(path,map,"utf-8",mParam);
        return result;
    }
    @Override
    protected void onPostExecute(String result){
        if (result != null) {
            Message msg = mHandler.obtainMessage (mRequest);
            msg.obj = result;
            System.out.println("获取签到人员列表"+result);
            mHandler.sendMessage (msg);
        } else {
//            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}
