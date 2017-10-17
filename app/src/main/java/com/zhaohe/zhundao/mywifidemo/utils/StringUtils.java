package com.zhaohe.zhundao.mywifidemo.utils;

/**
 * Created by caojun on 2017/9/12.
 */

public class StringUtils {
    /**
     * 返回 指定的 String 是否是 有效的 IP 地址.
     */
    public static boolean isValidIpAddress(String value) {

        int start = 0;
        int end = value.indexOf('.');
        int num = 0;

        while (start < value.length()) {

            if (-1 == end) {
                end = value.length();
            }

            try {
                int block = Integer.parseInt(value.substring(start, end));
                if ((block > 255) || (block < 0)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }

            num++;

            start = end + 1;
            end = value.indexOf('.', start);
        }

        return num == 4;
    }
}
