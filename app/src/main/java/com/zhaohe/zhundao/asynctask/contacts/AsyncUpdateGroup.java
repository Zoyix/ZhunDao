package com.zhaohe.zhundao.asynctask.contacts;

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
 * @Description:添加或修改分组
 * @Author:邹苏隆
 * @Since:2017/5/24 15:52
 */
public class AsyncUpdateGroup extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String mAccesskey;
    private String mParam;

    public AsyncUpdateGroup(Context context, Handler handler, int request, String Param) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
        this.mParam=Param;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST + Constant.Url.UpdateOrAddContactGroup;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accessKey", mAccesskey);
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
            System.out.println("wtf" + result);
            mHandler.sendMessage(msg);
        } else {
            DialogUtils.showDialog(mContext, R.string.app_serviceError);
        }

    }
}
