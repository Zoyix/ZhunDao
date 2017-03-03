package com.zhaohe.zhundao.bean;

import java.io.Serializable;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/2/9 15:24
 */
public class CustomSelectBean implements Serializable {
    private static final long serialVersionUID = 1597825067833169728L;
    //    选项的ID
    String ID;
    //    选项的内容
    String Content;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
