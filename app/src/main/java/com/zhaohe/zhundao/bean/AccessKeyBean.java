package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/2 0:55
 */
public class AccessKeyBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 2734959547231039170L;
    private String AccessKey;

    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }


    /**
     * 结果：0成功，1失败
     */
    private int Res = -1;

    /**
     * 附加消息
     */
    private String Msg;

    @Override
    public String toString() {
        return "AccessKeyBean{" +
                "AccessKey='" + AccessKey + '\'' +
                ", Res=" + Res +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
