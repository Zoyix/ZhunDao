package com.zhaohe.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncScanCode;
import com.zhaohe.zhundao.zxing.controller.MipcaActivityCapture;

import static android.app.Activity.RESULT_OK;
import static com.zhaohe.zhundao.ui.home.sign.SignOnFragment.MESSAGE_SCAN_CODE;

/**
 * @Description:二维码扫描工具类
 * @Author:邹苏隆
 * @Since:2016/12/4 15:07
 */
public class QueryCodeUtils {
    private Context mContext;
    private String mSignID;
private Handler mHandler;
    private final static int SCANNIN_GREQUEST_CODE = 1000;

    public void zxingQrCode(Context context,Handler mHandler,String mSignID){
        this.mHandler=mHandler;
        this.mContext=context;
        this.mSignID=mSignID;
        Activity activity = (Activity) context;
        Intent intent = new Intent ();
        intent.setClass (context, MipcaActivityCapture.class);
        intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult (intent, SCANNIN_GREQUEST_CODE);
        activity.overridePendingTransition (R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras ();
                    // 显示扫描到的内容
                    String result = bundle.getString ("result");
                    System.out.println("scancode:"+result);
                    ToastUtil.makeText(mContext,result);
                    scanCode(result);
                }
                break;
        }
    }
    public void scanCode(String result){

        AsyncScanCode asyncScanCode = new AsyncScanCode(mContext, mHandler,MESSAGE_SCAN_CODE,result,mSignID);
        asyncScanCode.execute();
    }
}
