package com.zhaohe.zhundao.bean;

/**
 * @Description:基础 实体
 * @Author:邹苏隆
 * @Since:2016/12/1 23:40
 */
public abstract class BaseBean {

    public int getRes() {
        return Res;
    }

    public void setRes(int res) {
        Res = res;
    }

    public String getMsg() {
        return Msg;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Res=" + Res +
                ", Msg='" + Msg + '\'' +
                '}';
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    /**
     * 结果：0成功，1失败
     */
    private int Res = -1;

    /**
     * 附加消息
     */
    private String Msg;


    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSucess() {
        return Res == 0;
    }
}
