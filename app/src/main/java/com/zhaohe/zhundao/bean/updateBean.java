package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/10/12 11:40
 */
public class updateBean implements Serializable {
    private static final long serialVersionUID = 100L;
    private String CheckInTime;
    private String VCode;
    private String CheckInID;

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
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
}
