package com.zhaohe.zhundao.mywifidemo.bean;

/**
 * Created by caojun on 2017/9/11.
 */

public class WifiBean {
    private int id;
    private String name;

    public WifiBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
