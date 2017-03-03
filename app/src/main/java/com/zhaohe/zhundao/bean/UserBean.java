package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:用户基本信息
 * @Author:邹苏隆
 * @Since:2016/12/1 23:47
 */
public class UserBean implements Serializable {

    private static final long serialVersionUID = 637298174007187013L;

    private int ID;
    private String TrueName;
    private String NickName;
    private String HeadImgurl;
    private int Sex;
    private String Country;
    private String Province;
    private String City;
    private String Address;
    private int GradeId;
    private String Company;
    private String Industry;
    private String Duty;
    private String Mobile;
    private String IDcard;
    private String Email;
    private String PassWord;
    private String Openid;
    private String QQid;
    private String Alipayid;
    private int InviteID;
    private String LastTime;
    private int Groupid;
    private String Unionid;
    private String Remark;
    private int Subscribe;
    private String VipTime;
    private int Balance;
    private String FactorageRate;
    private String AuthenticationId;
    private String Authentication;
    private String AddTime;

    public boolean isDeleted() {
        return IsDeleted;
    }

    public void setDeleted(boolean deleted) {
        IsDeleted = deleted;
    }

    private boolean IsDeleted;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTrueName() {
        return TrueName;
    }

    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadImgurl() {
        return HeadImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        HeadImgurl = headImgurl;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getGradeId() {
        return GradeId;
    }

    public void setGradeId(int gradeId) {
        GradeId = gradeId;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getDuty() {
        return Duty;
    }

    public void setDuty(String duty) {
        Duty = duty;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getIDcard() {
        return IDcard;
    }

    public void setIDcard(String IDcard) {
        this.IDcard = IDcard;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getOpenid() {
        return Openid;
    }

    public void setOpenid(String openid) {
        Openid = openid;
    }

    public String getQQid() {
        return QQid;
    }

    public void setQQid(String QQid) {
        this.QQid = QQid;
    }

    public String getAlipayid() {
        return Alipayid;
    }

    public void setAlipayid(String alipayid) {
        Alipayid = alipayid;
    }

    public int getInviteID() {
        return InviteID;
    }

    public void setInviteID(int inviteID) {
        InviteID = inviteID;
    }

    public String getLastTime() {
        return LastTime;
    }

    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }

    public int getGroupid() {
        return Groupid;
    }

    public void setGroupid(int groupid) {
        Groupid = groupid;
    }

    public String getUnionid() {
        return Unionid;
    }

    public void setUnionid(String unionid) {
        Unionid = unionid;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getSubscribe() {
        return Subscribe;
    }

    public void setSubscribe(int subscribe) {
        Subscribe = subscribe;
    }

    public String getVipTime() {
        return VipTime;
    }

    public void setVipTime(String vipTime) {
        VipTime = vipTime;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public String getFactorageRate() {
        return FactorageRate;
    }

    public void setFactorageRate(String factorageRate) {
        FactorageRate = factorageRate;
    }

    public String getAuthenticationId() {
        return AuthenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        AuthenticationId = authenticationId;
    }

    public String getAuthentication() {
        return Authentication;
    }

    public void setAuthentication(String authentication) {
        Authentication = authentication;
    }

    public String getAddTime() {
        return AddTime;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "Company='" + Company + '\'' +
                ", ID=" + ID +
                ", TrueName='" + TrueName + '\'' +
                ", NickName='" + NickName + '\'' +
                ", HeadImgurl='" + HeadImgurl + '\'' +
                ", Sex=" + Sex +
                ", Country='" + Country + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", Address='" + Address + '\'' +
                ", GradeId=" + GradeId +
                ", Industry='" + Industry + '\'' +
                ", Duty='" + Duty + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", IDcard='" + IDcard + '\'' +
                ", Email='" + Email + '\'' +
                ", PassWord='" + PassWord + '\'' +
                ", Openid='" + Openid + '\'' +
                ", QQid='" + QQid + '\'' +
                ", Alipayid='" + Alipayid + '\'' +
                ", InviteID=" + InviteID +
                ", LastTime='" + LastTime + '\'' +
                ", Groupid=" + Groupid +
                ", Unionid='" + Unionid + '\'' +
                ", Remark='" + Remark + '\'' +
                ", Subscribe=" + Subscribe +
                ", VipTime='" + VipTime + '\'' +
                ", Balance=" + Balance +
                ", FactorageRate='" + FactorageRate + '\'' +
                ", AuthenticationId='" + AuthenticationId + '\'' +
                ", Authentication='" + Authentication + '\'' +
                ", AddTime='" + AddTime + '\'' +
                ", IsDeleted=" + IsDeleted +
                '}';
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }


}
