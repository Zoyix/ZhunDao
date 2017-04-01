package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/13 13:48
 */
public class SignBean implements Serializable {
    public String getSign_id() {
        return sign_id;
    }

    public void setSign_id(String sign_id) {
        this.sign_id = sign_id;
    }

    private String sign_id;
    private String  signObject;

    public String getSignObject() {
        return signObject;
    }

    public void setSignObject(String signObject) {
        this.signObject = signObject;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    private static final long serialVersionUID = 3086925930172677488L;
    private String sign_status;
    private String act_id;

    public String getSign_status() {
        return sign_status;
    }

    public void setSign_status(String sign_status) {
        this.sign_status = sign_status;
    }

    //报名标题
    private String sign_title;
    //    活动标题
    private String act_title;

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    //报名方式
    private String sign_type;
    //    截止时间
    private String stoptime;
    //    签到人数
    private String sign_num;
    //    报名人数
    private String signup_num;

    public String getSign_title() {
        return sign_title;
    }

    public void setSign_title(String sign_title) {
        this.sign_title = sign_title;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    public String getSign_num() {
        return sign_num;
    }

    public void setSign_num(String sign_num) {
        this.sign_num = sign_num;
    }

    public String getSignup_num() {
        return signup_num;
    }

    public void setSignup_num(String signup_num) {
        this.signup_num = signup_num;
    }


}
