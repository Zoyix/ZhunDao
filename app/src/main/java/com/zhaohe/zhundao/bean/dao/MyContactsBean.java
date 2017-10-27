package com.zhaohe.zhundao.bean.dao;

import android.support.annotation.NonNull;

import com.zhaohe.zhundao.ui.home.mine.contacts.Cn2Spell;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/5/22 13:34
 */
public class MyContactsBean implements Comparable<MyContactsBean>, Serializable {
    private static final long serialVersionUID = -5700383370917985503L;
    private String name; // 姓名
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母
    private String Phone;//电话
    private String Address;//地址
    private String Email; //邮箱
    private String GroupName; //分组名称
    private String GroupID; //分组ID
    private String ID; //用户ID
    private String Sex;//性别
    private String HeadImgurl;//头像
    private String Company;//单位
    private String Duty;//职务
    private String IDcard;//身份证
    private String SerialNo;//编号
    private String Remark;//备注

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getHeadImgurl() {
        return HeadImgurl;
    }

    public void setHeadImgurl(String headImgurl) {
        HeadImgurl = headImgurl;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDuty() {
        return Duty;
    }

    public void setDuty(String duty) {
        Duty = duty;
    }

    public String getIDcard() {
        return IDcard;
    }

    public void setIDcard(String IDcard) {
        this.IDcard = IDcard;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    private String UpdateStatus = "false";

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public MyContactsBean() {
    }

    public MyContactsBean(String name) {
        this.name = name;
        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUpdateStatus() {
        return UpdateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        UpdateStatus = updateStatus;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;

    }

    public String getPinyin() {
        return pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setName(String name) {
        this.name = name;
        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }


    @Override
    public int compareTo(@NonNull MyContactsBean another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.getPinyin());
        }
    }
}
