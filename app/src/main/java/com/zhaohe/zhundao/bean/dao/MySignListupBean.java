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
