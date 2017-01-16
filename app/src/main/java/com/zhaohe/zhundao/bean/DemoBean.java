package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/2 15:48
 */
public class DemoBean implements Serializable {
    private static final long serialVersionUID = 1428473821613094413L;
    private String edit;
    private String list;
    private String share;
    private String more;
    public String getList() {
        return list;
    }

    @Override
    public String toString() {
        return "DemoBean{" +
                "edit='" + edit + '\'' +
                ", list='" + list + '\'' +
                ", share='" + share + '\'' +
                ", more='" + more + '\'' +
                '}';
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

}
