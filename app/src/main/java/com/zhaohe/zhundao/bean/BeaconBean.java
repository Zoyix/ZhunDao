package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/2/15 10:34
 */
public class BeaconBean implements Serializable {
    private static final long serialVersionUID = 2164018431591873323L;
    private String Title;
    private String BeaconID;
    private String BeaconName;
    private String DeviceID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String BeaconNickName;
    private String Url;
    private String NickName;
    private String ID;


    private String AddTime;

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBeaconID() {
        return BeaconID;
    }

    public void setBeaconID(String beaconID) {
        BeaconID = beaconID;
    }

    public String getBeaconName() {
        return BeaconName;
    }

    public void setBeaconName(String beaconName) {
        BeaconName = beaconName;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getBeaconNickName() {
        return BeaconNickName;
    }

    public void setBeaconNickName(String beaconNickName) {
        BeaconNickName = beaconNickName;
    }
}
