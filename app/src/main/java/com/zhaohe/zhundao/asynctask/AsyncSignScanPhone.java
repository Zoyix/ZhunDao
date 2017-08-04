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
 * @Description:管理员手机代签
 * @Author:邹苏隆
 * @Since:2017/2/24 16:05
 */
public class AsyncSignScanPhone extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mmobile;
    private String mAccesskey;
    private String mcheckInId;



    public AsyncSignScanPhone(Context context, Handler handler, int request, String mobile,String checkId) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mmobile = mobile;
        this.mcheckInId=checkId;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");

    }

    @Override
    protected String doInBackground(String... params) {
        String path = Constant.HOST + Constant.Url.AddCheckInListByPhone;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", mmobile);
        map.put("accessKey", mAccesskey);
        map.put("checkInId", mcheckInId);
        map.put("checkInWay","12");


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
            System.out.println("添加或修改联系人" + result);

            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}
