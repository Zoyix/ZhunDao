package com.zhaohe.app.utils;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Map;

/**
 * @Description:JSON 数据处理
 * @Author:邹苏隆
 * @Since:2016/12/2 0:26
 */
public class JSONUtils {
    //将json格式数据转化为String类型数据
    public static <T> T parseObject(String result, Class<T> clsss) {
        return JSON.parseObject(result, clsss);
    }

    public static boolean parseBoolean(String text) {

        return true;
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder url = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            url.append(entry.getKey()).append("=");
            url.append((entry.getValue()));
            url.append("&");
        }
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    public static String addComma(String[] list) {
        StringBuilder url = new StringBuilder();
        for (String item : list) {
            url.append(item).append(",");
        }
        url.deleteCharAt(url.length() - 1);

        return url.toString();
    }

    public static String addComma(ArrayList list) {
        StringBuilder url = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            url.append(list.get(i)).append(",");
        }
        if (url.length() != 0) {
            url.deleteCharAt(url.length() - 1);
        }

        return url.toString();
    }
}
