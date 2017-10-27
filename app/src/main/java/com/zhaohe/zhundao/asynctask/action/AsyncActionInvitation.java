package com.zhaohe.zhundao.asynctask.action;

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
 * @Since:2017/2/21 10:49
 */
public class AsyncActionInvitation extends AsyncTask<String, Integer, String> {
    private Context mContext;
    private Handler mHandler;
    private int mRequest;
    private Dialog mDialog;
    private String act_id;
    private String mAccesskey;


    public AsyncActionInvitation(Context context, Handler handler, int request, String act_id) {
        this.mContext = context;
        this.mHandler = handler;
        this.mRequest = request;
        this.mAccesskey = (String) SPUtils.get(mContext, "accessKey", "");
        this.act_id = act_id;
    }

    @Override
    protected String doInBackground(String... strings) {
        String path = Constant.HOST_MOBLIE + Constant.Url.InvitationUrl;
        Map<String, String> map = new HashMap<String, String>();
//        map.put("accessKey", mAccesskey);
        map.put("activityId", act_id);

        String result = HttpUtil.sendGETRequest(path, map, "utf-8");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            Message msg = mHandler.obtainMessage(mRequest);
            msg.obj = result;
            System.out.println("invitation_result" + result);
            mHandler.sendMessage(msg);
        } else {
//            DialogUtils.showDialog (mContext, R.string.app_serviceError);
        }

    }
}
