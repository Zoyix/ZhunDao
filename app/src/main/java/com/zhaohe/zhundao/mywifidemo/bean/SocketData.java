package com.zhaohe.zhundao.mywifidemo.bean;

/**
 * Created by JokerFish on 2017/4/25.
 */


//{"userID": "54","SSID": "84E0F42000A600B0","PWD": "84E0F42000A600B0"}
public class SocketData {
    private String ssid;//WIFI名称
    private String pwd;//密码
    private String ipAddress;//ip
    private String gateWay;//网关
    private String dns;//DNS
    private Boolean isStatic;//是否是静态IP
    private Boolean isEthernet;//是否是以太网配置

    public SocketData(String ssid, String pwd, String ipAddress, String gateWay, String dns, Boolean isStatic, Boolean isEthernet) {
        this.ssid = ssid;
        this.pwd = pwd;
        this.ipAddress = ipAddress;
        this.gateWay = gateWay;
        this.dns = dns;
        this.isStatic = isStatic;
        this.isEthernet = isEthernet;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGateWay() {
        return gateWay;
    }

    public void setGateWay(String gateWay) {
        this.gateWay = gateWay;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public Boolean getEthernet() {
        return isEthernet;
    }

    public void setEthernet(Boolean ethernet) {
        isEthernet = ethernet;
    }
}
