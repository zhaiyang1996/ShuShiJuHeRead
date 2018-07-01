package com.shushijuhe.shushijuheread.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 书籍历史记录表
 */
@Entity
public class BookReadHistory {
    @Id(autoincrement = true)
    private long id;
    private String uid;
    private int mix;
    private int paga;
    @Generated(hash = 1850189690)
    public BookReadHistory(long id, String uid, int mix, int paga) {
        this.id = id;
        this.uid = uid;
        this.mix = mix;
        this.paga = paga;
    }
    @Generated(hash = 820687143)
    public BookReadHistory() {
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
