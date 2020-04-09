package com.zhai.shuyangwx.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 书籍数据表
 */
@Entity
public class BookData {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String bookId;
    private String title;
    private String body;
    @Generated(hash = 1657522534)
    public BookData(Long id, @NotNull String bookId, String title, String body) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.body = body;
    }
    @Generated(hash = 687480960)
    public BookData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
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
