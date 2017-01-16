package com.zhaohe.app.commons.http.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 上传文件
 */
public class FormFile {

    /* 上传文件的数据 */
    private byte[]      data;
    private InputStream inStream;
    private File        file;
    /* 文件名称 */
    private String      filname;
    /* 文件路径 --用户本地保存时候存放路径 */
    private String      filpath;
    /* 请求参数名称 */
    private String      parameterName;
    /* 内容类型 */
    private String      contentType = "application/octet-stream";

    public FormFile(String filname, byte[] data, String parameterName, String contentType) {
        this.data = data;
        this.filname = filname;
        this.parameterName = parameterName;
        if (contentType != null) this.contentType = contentType;
    }

    public FormFile(File file, String parameterName, String contentType) {
        this.filname = file.getName ();
        this.parameterName = parameterName;
        this.file = file;
        try {
            this.inStream = new FileInputStream (file);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
        if (contentType != null) this.contentType = contentType;
    }

    public FormFile(File file, String parameterName,String filpath, String contentType) {
        this.filname = file.getName ();
        this.parameterName = parameterName;
        this.filpath = filpath;
        this.file = file;
        try {
            this.inStream = new FileInputStream (file);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        }
        if (contentType != null) this.contentType = contentType;
    }
    
    public File getFile(){
        return file;
    }

    public InputStream getInStream(){
        return inStream;
    }

    public byte[] getData(){
        return data;
    }

    public String getFilname(){
        return filname;
    }

    public void setFilname(String filname){
        this.filname = filname;
    }

    public String getParameterName(){
        return parameterName;
    }

    public void setParameterName(String parameterName){
        this.parameterName = parameterName;
    }

    public String getContentType(){
        return contentType;
    }

    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    public String getFilpath(){
        return filpath;
    }

    public void setFilpath(String filpath){
        this.filpath = filpath;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filpath == null) ? 0 : filpath.hashCode ());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass () != obj.getClass ()) return false;
        FormFile other = (FormFile) obj;
        if (filpath == null) {
            if (other.filpath != null) return false;
        } else if (!filpath.equals (other.filpath)) return false;
        return true;
    }

}
