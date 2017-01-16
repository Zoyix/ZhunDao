package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/14 10:35
 */
public class SignListBean implements Serializable{
    private static final long serialVersionUID = -215544916760608473L;
    private String sign_list_id;
    private String sign_list_name;
    private String sign_list_time;
    private String sign_list_phone;
    private String sign_list_status;

    public String getSign_list_id() {
        return sign_list_id;
    }

    public void setSign_list_id(String sign_list_id) {
        this.sign_list_id = sign_list_id;
    }

    public String getSign_list_name() {
        return sign_list_name;
    }

    public void setSign_list_name(String sign_list_name) {
        this.sign_list_name = sign_list_name;
    }

    public String getSign_list_time() {
        return sign_list_time;
    }

    public void setSign_list_time(String sign_list_time) {
        this.sign_list_time = sign_list_time;
    }

    public String getSign_list_phone() {
        return sign_list_phone;
    }

    public void setSign_list_phone(String sign_list_phone) {
        this.sign_list_phone = sign_list_phone;
    }

    public String getSign_list_status() {
        return sign_list_status;
    }

    public void setSign_list_status(String sign_list_status) {
        this.sign_list_status = sign_list_status;
    }
}
