package com.zhaohe.zhundao.asynctask.testScan;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/11/30 10:52
 */
public class AsyncMultiLogin extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int     mRequest;
    private Dialog mDialog;
    private String mmobile;
    private String mpassWord;
    private String mSignId;

    public AsyncMultiLogin(Context context, Handler handler, int request, String signId, String mobile, String passWord) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mSignId=signId;
        this.mmobile=mobile;
        this.mpassWord=passWord;
    }
    @Override
    protected String doInBackground(String... params){
//        修改
        String path = Constant.HOST + Constant.Url.LoginMulti;
        Map<String, String> map = new HashMap<String, String>();
//        需要修改
        map.put("checkInId",mSignId);
        map.put("pwd",mpassWord);
        map.put ("phone", mmobile);

        String result = HttpUtil.sendGETRequest(path,map,"utf-8");
        return result;
    }
    @Override
    protected void onPostExecute(String result){

        if (result != null) {
            Message msg = mHandler.obtainMessage (mRequest);
            msg.obj = result;
            System.out.println("登录获取多点签到Accesskey" + result);

            mHandler.sendMessage (msg);
        } else {
//            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}
