package com.zhaohe.zhundao.bean.dao;

import java.io.Serializable;

/**
 * @Description:a\群组实体类
 * @Author:邹苏隆
 * @Since:2017/5/23 14:00
 */
public class MyGroupBean    implements Serializable{
    private static final long serialVersionUID = -2827325796947416176L;
    private String Name;//群组名称
    private String Sequence;//序列
    private String TotalCount;//人数
    private String ID;//群组ID
    private String AdminUserID;//用户ID
    private String UpdateStatus="false";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        Sequence = sequence;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAdminUserID() {
        return AdminUserID;
    }

    public void setAdminUserID(String adminUserID) {
        AdminUserID = adminUserID;
    }

    public String getUpdateStatus() {
        return UpdateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        UpdateStatus = updateStatus;
    }
}
