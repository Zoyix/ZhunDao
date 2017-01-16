package com.zhaohe.app.utils;

import android.content.Context;
import android.widget.Toast;

/**
 *@Description: 吐丝 工具类
 *@Author:杨攀
 *@Since:2014年11月12日下午5:25:20
 */
public class ToastUtil {

    /**
     *@Description: 
     *@Author:杨攀
     *@Since: 2014年11月12日下午5:25:51
     *@param context  上下文
     *@param content  内容
     */
    public static void makeText(Context context,String content){
        Toast.makeText (context, content, Toast.LENGTH_SHORT).show ();
    }

    /**
     *@Description: 
     *@Author:杨攀
     *@Since: 2014年11月12日下午5:27:25
     *@param context 上下文
     *@param content 内容
     */
    public static void makeText(Context context,int content){
        Toast.makeText (context, content, Toast.LENGTH_SHORT).show ();
    }
}
