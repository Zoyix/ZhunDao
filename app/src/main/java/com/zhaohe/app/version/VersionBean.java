package com.zhaohe.app.version;

import java.io.Serializable;

/**
 *@Description: 版本信息 实体
 *@Author:杨攀
 *@Since:2014年8月6日下午12:44:01
 */
public class VersionBean implements Serializable {


    private static final long serialVersionUID = -9180175245335235423L;
    private int               versionCode;
    private String            apkName;
    private String            url;
    private String            description;
    private String            synccode;

    public int getVersionCode(){
        return versionCode;
    }

    public void setVersionCode(int versionCode){
        this.versionCode = versionCode;
    }

    public String getApkName(){
        return apkName;
    }

    public void setApkName(String apkName){
        this.apkName = apkName;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getSynccode(){
        return synccode;
    }

    public void setSynccode(String synccode){
        this.synccode = synccode;
    }

}
