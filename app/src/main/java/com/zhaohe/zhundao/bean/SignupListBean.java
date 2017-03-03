package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/21 10:01
 */
public class SignupListBean implements Serializable {
    private static final long serialVersionUID = -1104796504482971389L;

    public String getSignup_list_status() {
        return signup_list_status;
    }

    public void setSignup_list_status(String signup_list_status) {
        this.signup_list_status = signup_list_status;
    }

    public String getSignup_list_name() {
        return signup_list_name;
    }

    public void setSignup_list_name(String signup_list_name) {
        this.signup_list_name = signup_list_name;
    }

    public String getSignup_list_phone() {
        return signup_list_phone;
    }

    public String getSignup_list_time() {
        return signup_list_time;
    }

    public void setSignup_list_time(String signup_list_time) {
        this.signup_list_time = signup_list_time;
    }

    public void setSignup_list_phone(String signup_list_phone) {
        this.signup_list_phone = signup_list_phone;
    }

    private String signup_list_status;
    private String signup_list_name;
    private String signup_list_phone;
    private String signup_list_time;


}
