package com.shushijuhe.shushijuheread.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 书籍历史记录表
 */
@Entity
public class BookReadHistory {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String bookid;
    private int mix;
    private int paga;
    @Generated(hash = 2048069191)
    public BookReadHistory(Long id, @NotNull String bookid, int mix, int paga) {
        this.id = id;
        this.bookid = bookid;
        this.mix = mix;
        this.paga = paga;
    }
    @Generated(hash = 820687143)
    public BookReadHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookid() {
        return this.bookid;
    }
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }
    public int getMix() {
        return this.mix;
    }
    public void setMix(int mix) {
        this.mix = mix;
    }
    public int getPaga() {
        return this.paga;
    }
    public void setPaga(int paga) {
        this.paga = paga;
    }
}
