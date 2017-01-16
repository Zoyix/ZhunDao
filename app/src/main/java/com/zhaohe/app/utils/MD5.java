package com.zhaohe.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *@Description: MD5 工具类
 *@Author:杨攀
 *@Since:2014年6月1日上午8:16:36
 */
public class MD5 {

    public static String getMD5(String content){
        try {
            MessageDigest digest = MessageDigest.getInstance ("MD5");
            digest.update (content.getBytes ());
            return getHashString (digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest){
        StringBuilder builder = new StringBuilder ();
        for ( byte b : digest.digest () ) {
            builder.append (Integer.toHexString ((b >> 4) & 0xf));
            builder.append (Integer.toHexString (b & 0xf));
        }
        return builder.toString ();
    }
}
