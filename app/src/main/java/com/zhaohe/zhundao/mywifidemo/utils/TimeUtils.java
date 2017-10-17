package com.zhaohe.zhundao.mywifidemo.utils;

import java.text.SimpleDateFormat;

public class TimeUtils {
    /**
     * 获取当前的日期
     *
     * @return
     */
    public static String getNowDate2() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(System.currentTimeMillis()));
        return date;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }


}
