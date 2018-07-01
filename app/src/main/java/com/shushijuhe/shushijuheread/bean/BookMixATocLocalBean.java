package com.shushijuhe.shushijuheread.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BookMixATocLocalBean {
    @Id(autoincrement = true)
    private long uid;
    public String title;
    public String link;
    public String id;
    public boolean isOnline = true; //是否为在线,默认为在线
    @Generated(hash = 1748069219)
    public BookMixATocLocalBean(long uid, String title, String link, String id,
            boolean isOnline) {
        this.uid = uid;
        this.title = title;
        this.link = link;
        this.id = id;
        this.isOnline = isOnline;
    }
    @Generated(hash = 211240346)
    public BookMixATocLocalBean() {
    }
    public long getUid() {
        return this.uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
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
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean getIsOnline() {
        return this.isOnline;
    }
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
