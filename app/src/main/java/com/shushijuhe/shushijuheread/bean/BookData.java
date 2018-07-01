package com.shushijuhe.shushijuheread.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 书籍数据表
 */
@Entity
public class BookData {
    @Id(autoincrement = true)
    private long id;
    private String uid;
    private String title;
    private String body;
    @Generated(hash = 726107698)
    public BookData(long id, String uid, String title, String body) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.body = body;
    }
    @Generated(hash = 687480960)
    public BookData() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return this.body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
