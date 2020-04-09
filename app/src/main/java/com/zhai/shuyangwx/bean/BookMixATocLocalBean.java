package com.zhai.shuyangwx.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BookMixATocLocalBean {
    @Id(autoincrement = true)
    private Long id;
    public String title;
    public String link;
    public String bookid;
    public boolean isOnline = true; //是否为在线,默认为在线
    @Generated(hash = 187692798)
    public BookMixATocLocalBean(Long id, String title, String link, String bookid,
            boolean isOnline) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.bookid = bookid;
        this.isOnline = isOnline;
    }
    @Generated(hash = 211240346)
    public BookMixATocLocalBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getBookid() {
        return this.bookid;
    }
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
    public boolean getIsOnline() {
        return this.isOnline;
    }
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
