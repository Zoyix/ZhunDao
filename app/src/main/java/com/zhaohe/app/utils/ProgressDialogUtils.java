package com.zhaohe.app.utils;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * @Description: 进度 提示框
 * @Author:杨攀
 * @Since:2014年11月12日下午5:23:05
 */
public class ProgressDialogUtils {

    /**
     * @param title
     * @param msg
     * @Description: 点击按钮后，显示的 进度条  遮罩
     * @Author:杨攀
     * @Since: 2014年8月12日下午4:36:30
     * //     *@param ctx
     */
    public static ProgressDialog showProgressDialog(Context context, String title, String msg) {
        ProgressDialog dialog = ProgressDialog.show(context, title, msg, true, true);
        dialog.setCanceledOnTouchOutside(false);// 允许取消
        return dialog;
    }

    /**
     * @param title
     * @param msg
     * @Description: 点击按钮后，显示的 进度条  遮罩
     * @Author:杨攀
     * @Since: 2014年8月12日下午4:36:30
     * //     *@param ctx
     */
    public static ProgressDialog showProgressDialog(Context context, int title, int msg) {
        ProgressDialog dialog = ProgressDialog.show(context, context.getString(title), context.getString(msg), true, true);
        dialog.setCanceledOnTouchOutside(false);// 允许取消
        return dialog;
    }

    /**
     * @param context
     * @param title   标题
     * @param msg     提示信息
     * @param max     最大进度值
     * @return
     * @Description: 点击按钮后， 横向 进度条的 遮罩
     * @Author:杨攀
     * @Since: 2015年4月21日下午1:40:15
     */
    public static ProgressDialog showHorizontalProgressDialog(Context context, int title, int msg, int max) {
        //实例化
        ProgressDialog dialog = new ProgressDialog(context);
        //设置进度条风格，风格为长形，有刻度的
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置ProgressDialog 标题
        dialog.setTitle(title);
        //设置ProgressDialog 提示信息
        dialog.setMessage(context.getString(msg));
        //设置ProgressDialog 标题图标
        //dialog.setIcon(R.drawable.android);
        //设置ProgressDialog 的进度条是否不明确
        dialog.setIndeterminate(false);
        //设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(true);
        //初始化的进度值
        dialog.setProgress(0);
        //最大进度值
        dialog.setMax(max);
        dialog.show();

        return dialog;
    }


}
