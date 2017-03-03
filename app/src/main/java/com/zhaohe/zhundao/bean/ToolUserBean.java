package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:tool UserBean
 * @Author:邹苏隆
 * @Since:2016/12/2 0:21
 */
public class ToolUserBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 6545923951088426477L;

    private UserBean Data;

    private String Url;

    @Override
    public String toString() {
        return "ToolUserBean{" +
                "Data=" + Data +
                ", Url='" + Url + '\'' +
                '}';
    }

    public UserBean getData() {
        return Data;
    }

    public void setData(UserBean data) {
        Data = data;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


}
