package com.zhaohe.app.utils;


import com.alibaba.fastjson.JSON;

/**
 * @Description:JSON 数据处理
 * @Author:邹苏隆
 * @Since:2016/12/2 0:26
 */
public class JSONUtils {
//将json格式数据转化为String类型数据
    public static <T> T parseObject(String result,Class<T> clsss){
        return JSON.parseObject(result,clsss);
    }

    public static boolean parseBoolean(String text){

        return true;
    }
}
