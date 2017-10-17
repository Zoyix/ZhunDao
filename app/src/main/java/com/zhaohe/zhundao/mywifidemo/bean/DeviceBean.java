package com.zhaohe.zhundao.mywifidemo.bean;

/**
 * Created by caojun on 2017/9/11.
 * 设备列表Bean
 */

public class DeviceBean {
    private int id;
    private String deviceKey;

    public DeviceBean(int id, String deviceKey) {
        this.id = id;
        this.deviceKey = deviceKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
