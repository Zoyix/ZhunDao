package com.zhaohe.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Description:日期工具类
 * @Author:杨攀
 * @Since:2015年1月28日上午11:25:30
 */
public class DateUtils {

    /**
     * @param stimeStr
     * @param etimeStr
     * @return
     * @Description: 检测开始结束时间（格式必须是 YY:mm）
     * @Author:张文男
     * @Since: 2015年10月31日下午4:24:16
     */
    public static boolean checkDateForHm(String stimeStr, String etimeStr) {
        try {
            if (StringUtils.isNotEmpty(stimeStr) && StringUtils.isNotEmpty(etimeStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date dateStime = sdf.parse("2014-01-01 " + stimeStr);
                Date dateEtime = sdf.parse("2014-01-01 " + etimeStr);
                if (dateStime.before(dateEtime)) {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param time （格式必须为yyyyMMdd）
     * @return
     * @Description: 转化日期格式为 yyyy-MM-dd
     * @Author:张文男
     * @Since: 2015年10月20日上午11:13:44
     */
    public static String formatDate(String time) {
        return formatDate(time, "yyyyMMdd");
    }

    /**
     * @return
     * @Description: 获取当天 时间的 的格式  yyyy-MM-dd
     * @Author:杨攀
     * @Since: 2014年11月12日下午2:17:24
     */
    public static String getCurrentDateFormat() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * @param format 时间格式
     * @return
     * @Description: 获取当天 时间的 的格式
     * @Author:杨攀
     * @Since: 2014年11月12日下午2:16:44
     */
    public static String getCurrentDayFormat(String format) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 格式
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * @return
     * @Description: 获取当前时间的 的格式 yyyyMMddhhmmssSSS
     * @Author:杨攀
     * @Since: 2014年10月15日下午7:23:45
     */
    public static String getCurrentTimeFormat() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.getDefault());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * @return
     * @Description: 获取当天 时间的 的格式  yyyyMMdd
     * @Author:杨攀
     * @Since: 2014年10月15日下午7:23:45
     */
    public static String getCurrentDayFormat() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * @return
     * @Description: 获取当前年月日时分 的格式 yyyy-MM-dd HH:mm
     * @Author:邹苏启
     * @Since: 2015-3-5上午11:00:26
     */
    public static String getCurrentDayTimeFormat() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = df.format(calendar.getTime());
        return date;
    }

    /**
     * @param time
     * @param template 当前传入日期的格式, 如：20150907 格式为：yyyyMMdd
     * @return
     * @Description: 日期格式化 为 yyyyMMdd
     * @Author:杨攀
     * @Since: 2015年10月27日下午8:24:53
     */
    public static String formatDateYMD(String time, String template) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
            Date date = sdf.parse(time);

            SimpleDateFormat format_YMD = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            return format_YMD.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param time
     * @return
     * @Description: 将yyyy-MM-dd日期格式化 为 yyyyMMdd
     * @Author:邹苏启
     * @Since: 2015-10-29上午11:13:26
     */
    public static String formatDateYMD(String time) {
        return formatDateYMD(time, "yyyy-MM-dd");
    }

    /**
     * @param time
     * @param template 当前传入日期的格式, 如：20150907 格式为：yyyyMMdd
     * @return
     * @Description: 日期格式化为   时分秒  HHmmss
     * @Author:杨攀
     * @Since: 2015年10月27日下午8:13:43
     */
    public static String formatDateHHmmss(String time, String template) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
            Date date = sdf.parse(time);

            SimpleDateFormat sdfhm = new SimpleDateFormat("HHmmss", Locale.getDefault());
            return sdfhm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param time
     * @param template 当前传入日期的格式, 如：20150907 格式为：yyyyMMdd
     * @return
     * @Description: 日期格式化为   时分秒  yyyy-MM-dd HH:mm
     * @Author:杨攀
     * @Since: 2015年10月27日下午8:13:43
     */
    public static String formatDateYMDHM(String time, String template) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
            Date date = sdf.parse(time);

            SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return sdfhm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String formatDateHM(String time, String template) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + template, Locale.getDefault());
            Date date = sdf.parse("2014-01-01 " + time);

            SimpleDateFormat sdfhm = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdfhm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * @param time
     * @param template 当前传入日期的格式, 如：20150907 格式为：yyyyMMdd
     * @return
     * @Description: 日期格式化 为  yyyy-MM-dd
     * @Author:杨攀
     * @Since: 2015年10月28日下午7:47:01
     */
    public static String formatDate(String time, String template) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
            Date date = sdf.parse(time);

            SimpleDateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdfhm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param time
     * @param template 当前传入日期的格式, 如：20150907 格式为：yyyyMMdd
     *                 *@param targetTemplate 目标日期的格式, 如：20150907 格式为：yyyyMMdd
     * @return
     * @Description: 日期格式化 为  yyyy-MM-dd
     * @Author:杨攀
     * @Since: 2015年10月28日下午7:47:01
     */
    public static String formatDate(String time, String template, String targetTemplate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
            Date date = sdf.parse(time);

            SimpleDateFormat sdfhm = new SimpleDateFormat(targetTemplate, Locale.getDefault());
            return sdfhm.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
