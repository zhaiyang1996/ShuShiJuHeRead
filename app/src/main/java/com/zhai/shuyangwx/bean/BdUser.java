package com.zhai.shuyangwx.bean;

/**
 * 本地聊天室用户实体
 */
public class BdUser {
    private String name;
    private String sex;
    private String isQd = "-1"; //是否为首启动页

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsQd() {
        return isQd;
    }

    public void setIsQd(String isQd) {
        this.isQd = isQd;
    }
}
