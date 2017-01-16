package com.zhaohe.zhundao.bean;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/5 15:30
 */
public class ActionBean implements Serializable {
    private static final long serialVersionUID = -8764809360546296586L;
    private String act_id;
    private String url;
    private String act_content;

    public String getAct_content() {
        return act_content;
    }

    public void setAct_content(String act_content) {
        this.act_content = act_content;
    }
    //    活动图片

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAct_id() {
        return act_id;
    }

    public void setAct_id(String act_id) {
        this.act_id = act_id;
    }

    private ImageView img_act;
    //    活动标题
    private String act_title;
    //    活动状态
    private String act_status;
    //    活动报名人数
    private String act_sign_num;
    //    报名收入
    private String act_sign_income;
    //    活动报名截止时间
    private String act_endtime;
    //    活动报名截止剩余时间
    private String act_resttime;
    //    活动开始时间
    private String act_starttime;
    //    活动开始剩余时间
    private String act_resttime2;

    public String getAct_resttime2() {
        return act_resttime2;
    }

    public void setAct_resttime2(String act_resttime2) {
        this.act_resttime2 = act_resttime2;
    }

    public String getAct_starttime() {
        return act_starttime;
    }

    public void setAct_starttime(String act_starttime) {
        this.act_starttime = act_starttime;
    }

    public ImageView getImg_act() {
        return img_act;
    }

    public void setImg_act(ImageView img_act) {
        this.img_act = img_act;
    }

    public String getAct_title() {
        return act_title;
    }

    public void setAct_title(String act_title) {
        this.act_title = act_title;
    }

    public String getAct_status() {
        return act_status;
    }

    public void setAct_status(String act_status) {
        this.act_status = act_status;
    }

    public String getAct_sign_num() {
        return act_sign_num;
    }

    public void setAct_sign_num(String act_sign_num) {
        this.act_sign_num = act_sign_num;
    }

    public String getAct_sign_income() {
        return act_sign_income;
    }

    public void setAct_sign_income(String act_sign_income) {
        this.act_sign_income = act_sign_income;
    }

    public String getAct_endtime() {
        return act_endtime;
    }

    public void setAct_endtime(String act_endtime) {
        this.act_endtime = act_endtime;
    }

    public String getAct_resttime() {
        return act_resttime;
    }

    public void setAct_resttime(String act_resttime) {
        this.act_resttime = act_resttime;
    }
}
