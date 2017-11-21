package com.zhaohe.zhundao.bean.jsonbean;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/11/15 10:56
 */
public class AuthenticationBean {
    /**
     * Name : 邹苏隆
     * IDCard : 332522199411180355
     * Mobile : 18967092210
     * IdCardFront : https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105214313|
     * IdCardBack : https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105216970|
     * Status : 0
     * UserId : 44948
     * ID : 2503
     * AddTime : 2017-11-15 10:52:20
     * IsDeleted : false
     */

    private String Name;
    private String IDCard;
    private String Mobile;
    private String IdCardFront;
    private String IdCardBack;
    private int Status;
    private int UserId;
    private int ID;
    private String AddTime;
    private boolean IsDeleted;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getIdCardFront() {
        return IdCardFront;
    }

    public void setIdCardFront(String IdCardFront) {
        this.IdCardFront = IdCardFront;
    }

    public String getIdCardBack() {
        return IdCardBack;
    }

    public void setIdCardBack(String IdCardBack) {
        this.IdCardBack = IdCardBack;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public boolean isIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean IsDeleted) {
        this.IsDeleted = IsDeleted;
    }
}
