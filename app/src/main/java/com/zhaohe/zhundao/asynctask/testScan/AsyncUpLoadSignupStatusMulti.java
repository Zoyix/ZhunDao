package com.zhaohe.zhundao.asynctask.testScan;

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
 * @Description:上传扫码签到用户状态
 * @Author:邹苏隆
 * @Since:2016/12/29 11:12
 */
public class AsyncUpLoadSignupStatusMulti extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private Handler mHandler;
    private int     mRequest;
    private String mAccesskey;
    private String mParam;//json数据包VCODE和checkinID
    public AsyncUpLoadSignupStatusMulti(Context context, Handler handler, int request, String param){
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam=param;
        this.mAccesskey= (String) SPUtils.get(mContext,"accessKey_multi","");
}

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST + Constant.Url.BatchCheckIn;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey",mAccesskey);
        map.put("checkJson",mParam);
        String result = HttpUtil.sendPostNewrequest(path,map,"utf-8");
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
