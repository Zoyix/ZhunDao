package com.zhaohe.app.commons.dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaohe.zhundao.R;

public class DialogUtils {

    private static final String TAG = "DialogUtils";

    /**
     * @param context
     * @param title   标题
     *                //     *@param items 编辑、删除
     *                //     *@param itemClickListener list_item_sign_list_more 对于的事件
     * @return
     * @Description: 显示 Edit 对话框(列表对话框形式 )
     * @Author:杨攀
     * @Since: 2015年6月23日上午9:37:36
     */
    public static AlertDialog.Builder createEditDialog(Context context, int title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);// 标题
        return builder;
    }


    public static AlertDialog.Builder createEditDialog(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        return builder;
    }

    /**
     * @param ctx
     * @param msg
     * @Description: 定义一个显示消息的对话框
     * @Author:杨攀
     * @Since: 2014年6月6日下午3:24:24
     */
    public static void showDialog(final Context ctx, String msg) {
        // 创建一个AlertDialog.Builder对象
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
            builder.setTitle(R.string.app_dialog_title);
            builder.setPositiveButton(R.string.app_dialog_ok, null);
            builder.create().show();

        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }
    }

    /**
     * @param ctx
     * @param msg //     *@param goHome
     * @Description: 定义一个显示消息的对话框
     * @Author:杨攀
     * @Since: 2014年6月6日下午3:24:24
     */
    public static void showDialog(final Context ctx, int msg) {
        // 创建一个AlertDialog.Builder对象
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
            builder.setTitle(R.string.app_dialog_title);
            builder.setPositiveButton(R.string.app_dialog_ok, null);
            builder.create().show();
        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }

    }

    /**
     * @param ctx
     * @param view
     * @Description: 定义一个显示指定组件的对话框
     * @Author:杨攀
     * @Since: 2014年6月6日下午3:24:01
     */
    public static void showDialog(Context ctx, View view) {
        try {
            new AlertDialog.Builder(ctx).setView(view).setCancelable(false).setPositiveButton(R.string.app_dialog_ok, null).create().show();
        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }

    }

    /**
     * @param activity
     * @param msg
     * @Description: 定义一个显示消息的对话框, 点击按钮后，关闭(finish)当前 Activity
     * @Author:杨攀
     * @Since: 2014年6月12日上午10:55:58
     */
    public static void showDialogViewFinish(final Activity activity, int msg) {
        // 创建一个AlertDialog.Builder对象
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage(msg);
            builder.setTitle(R.string.app_dialog_title);
            builder.setPositiveButton(R.string.app_dialog_ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                }
            }).show();
        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }

    }

    /**
     * @param activity
     * @param msg
     * @param positiveButtonListener 确定按钮的事件
     * @param negativeButtonListener 取消按钮的事件
     * @Description: 显示对话框
     * @Author:杨攀
     * @Since: 2014年8月20日上午11:27:08
     */
    public static void showDialog(Activity activity, int msg, OnClickListener positiveButtonListener, OnClickListener negativeButtonListener) {
        // 创建一个AlertDialog.Builder对象
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage(msg);
            builder.setTitle(R.string.app_dialog_title);
            builder.setPositiveButton(R.string.app_dialog_ok, positiveButtonListener);
            builder.setNegativeButton(R.string.app_dialog_cancel, negativeButtonListener);
            builder.setCancelable(false);
            builder.show();

        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }

    }

    /**
     * @param activity
     * @param msg
     * @param positiveButtonListener 确定按钮的事件
     * @Description: 显示对话框
     * @Author: 杨攀
     * @Since: 2014年8月15日下午1:31:44
     */
    public static void showDialog(Activity activity, int msg, OnClickListener positiveButtonListener) {
        // 创建一个AlertDialog.Builder对象
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setMessage(msg);
            builder.setTitle(R.string.app_dialog_title);
            builder.setPositiveButton(R.string.app_dialog_ok, positiveButtonListener);
            builder.setNegativeButton(R.string.app_dialog_cancel, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.i(TAG, "对话框所在界面不在栈顶！");
        }

    }

    /**
     * @param activity
     * @param dialogTitle
     * @param editHint
     * @param editText
     * @param view
     * @Description: 输入文本框的 对话框
     * @Author:杨攀
     * @Since: 2015年7月10日上午10:19:41
     */
    public static void showEditDialog(Activity activity, int dialogTitle, int editHint, String editText, final TextView view) {

        final EditText eText = new EditText(activity);
        eText.setHint(editHint);
        eText.setText(editText);
        eText.setSelection(eText.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(dialogTitle);
        builder.setView(eText);
        builder.setPositiveButton(R.string.app_dialog_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.setText(eText.getText().toString());
            }
        }).show();
    }

    /**
     * @param activity
     * @param dialogTitle
     * @param editHint
     * @param editText
     * @param view
     * @Description: 显示数字编辑对话框
     * @Author:邹苏启
     * @Since: 2015-8-18下午7:04:32
     */
    public static void showNumerEditDialog(Activity activity, int dialogTitle, int editHint, String editText, final TextView view) {

        final EditText eText = new EditText(activity);
        eText.setHint(editHint);
        eText.setInputType(InputType.TYPE_CLASS_NUMBER);
        eText.setText(editText);
        eText.setSelection(eText.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(dialogTitle);
        builder.setView(eText);
        builder.setPositiveButton(R.string.app_dialog_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.setText(eText.getText().toString());
            }
        }).show();
    }

}
