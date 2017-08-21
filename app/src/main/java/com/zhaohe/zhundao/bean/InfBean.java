package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/8/15 10:08
 */
public class InfBean implements Serializable {
    private static final long serialVersionUID = -5985446405207293118L;
    private String Title;
    private String Detail;
    private String AddTime;
    private String SortName;
    private String mID;
    private boolean read;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getSortName() {
        return SortName;
    }

    public void setSortName(String sortName) {
        SortName = sortName;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }


}
