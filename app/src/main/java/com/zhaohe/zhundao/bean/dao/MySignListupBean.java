package com.zhaohe.zhundao.bean.dao;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/26 16:25
 */
public class MySignListupBean implements Serializable {
    private static final long serialVersionUID = -2463756553320502348L;
    private String VCode;
    private String CheckInID;
    private String Status;
    private String UpdateStatus;
    private String Name;
    private String Phone;
    private String AdminRemark;
    private String FeeName;
    private String Fee;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAdminRemark() {
        return AdminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        AdminRemark = adminRemark;
    }

    public String getFeeName() {
        return FeeName;
    }

    public void setFeeName(String feeName) {
        FeeName = feeName;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }


    public String getVCode() {
        return VCode;
    }

    public void setVCode(String VCode) {
        this.VCode = VCode;
    }

    public String getCheckInID() {
        return CheckInID;
    }

    public void setCheckInID(String checkInID) {
        CheckInID = checkInID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUpdateStatus() {
        return UpdateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        UpdateStatus = updateStatus;
    }

}
