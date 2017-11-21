package com.zhaohe.zhundao.bean.jsonbean;

import java.util.List;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/11/20 11:00
 */
public class MsgBean {
    /**
     * Res : 0
     * Msg : succeed
     * Data : [{"es_user":"44948","es_type":0,"es_pay":0,"es_date":"2017-11-20T10:38:18.327","es_dl":1,"JH_Remark":"【准到】"}]
     * Url : null
     */

    private int Res;
    private String Msg;
    private Object Url;
    private List<DataBean> Data;

    public int getRes() {
        return Res;
    }

    public void setRes(int Res) {
        this.Res = Res;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public Object getUrl() {
        return Url;
    }

    public void setUrl(Object Url) {
        this.Url = Url;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * es_user : 44948
         * es_type : 0
         * es_pay : 0
         * es_date : 2017-11-20T10:38:18.327
         * es_dl : 1
         * JH_Remark : 【准到】
         */

        private String es_user;
        private int es_type;
        private int es_pay;
        private String es_date;
        private int es_dl;
        private String JH_Remark;

        public String getEs_user() {
            return es_user;
        }

        public void setEs_user(String es_user) {
            this.es_user = es_user;
        }

        public int getEs_type() {
            return es_type;
        }

        public void setEs_type(int es_type) {
            this.es_type = es_type;
        }

        public int getEs_pay() {
            return es_pay;
        }

        public void setEs_pay(int es_pay) {
            this.es_pay = es_pay;
        }

        public String getEs_date() {
            return es_date;
        }

        public void setEs_date(String es_date) {
            this.es_date = es_date;
        }

        public int getEs_dl() {
            return es_dl;
        }

        public void setEs_dl(int es_dl) {
            this.es_dl = es_dl;
        }

        public String getJH_Remark() {
            return JH_Remark;
        }

        public void setJH_Remark(String JH_Remark) {
            this.JH_Remark = JH_Remark;
        }
    }
}
