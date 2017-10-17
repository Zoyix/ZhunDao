package com.zhaohe.zhundao.mywifidemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtis {
    private static Toast sToast;
    private static Dialog loadingDialog;

    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            sToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void showLoading(Context context, String msg) {
        if (loadingDialog == null) {
            loadingDialog = com.zhaohe.zhundao.mywifidemo.utils.ProgressDialog.createLoadingDialog(context, msg);
            loadingDialog.setCancelable(true);
        }
        loadingDialog.show();
    }

    public static void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
