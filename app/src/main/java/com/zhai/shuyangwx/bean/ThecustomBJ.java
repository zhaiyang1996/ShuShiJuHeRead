package com.zhai.shuyangwx.bean;

/**
 * 自定义阅读背景实体类
 */
public class ThecustomBJ {
    private int is = -1;
    private int isImg = 1;
    private String bjColor ="-1";
    private int textColor=-1;

    public int getIs() {
        return is;
    }

    public void setIs(int is) {
        this.is = is;
    }

    public int getIsImg() {
        return isImg;
    }

    public void setIsImg(int isImg) {
        this.isImg = isImg;
    }

    public String getBjColor() {
        return bjColor;
    }

    public void setBjColor(String bjColor) {
        this.bjColor = bjColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
