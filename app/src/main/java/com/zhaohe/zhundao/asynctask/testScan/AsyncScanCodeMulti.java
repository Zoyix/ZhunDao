package com.zhaohe.zhundao.asynctask.testScan;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/23 9:35
 */
public class AsyncScanCodeMulti extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int     mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;

private String mCheckinId;
    public AsyncScanCodeMulti(Context context, Handler handler, int request, String vCode){
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mParam=vCode;
        this.mCheckinId=(String) SPUtils.get(mContext,"sign_id","");
        this.mAccesskey= (String) SPUtils.get(mContext,"accessKey_multi","");
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = (String) SPUtils.get(mContext,"HOST",Constant.HOST) + Constant.Url.AddCheckInListByQrcode;
//        修改
        Map<String, String> map = new HashMap<String, String>();
        map.put("vCode",mParam);
        map.put("checkInId",mCheckinId);
        map.put("accessKey",mAccesskey);
        String result = HttpUtil.sendGETRequest(path,map,"utf-8");
        return result;
    }
    @Override
    protected void onPostExecute(String result){

        if (result != null) {
            Message msg = mHandler.obtainMessage (mRequest);
            msg.obj = result;
            System.out.println("扫码"+result);
            mHandler.sendMessage (msg);
        } else {
            ToastUtil.makeText(mContext,"网络不稳定，请切换为离线模式");
        }

    }
}
