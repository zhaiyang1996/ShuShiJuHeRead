package com.zhai.shuyangwx.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 书架数据库
 */
/*
 @Entity：告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
 @Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
 @Property：可以自定义字段名，注意外键不能使用该属性
 @NotNull：属性不能为空
 @Transient：使用该注释的属性不会被存入数据库的字段中
 @Unique：该属性值必须在数据库中是唯一值
 @Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
 */
@Entity
public class BookshelfBean {
    @Id(autoincrement = true)
    private Long id;  //id
    @NotNull
    private String bookId; //书籍ID
    private String name;//书名
    private String cover;//封面图
    private String time;//最新打开时间
    private long timeMillis; //最新打开的时间轴，用来进行书架排序
    private boolean isUpdate = false;//书籍是否有更新、默认否
    private boolean isChecked = false; //书籍是否被选中、默认否
    private boolean isEnd = false; //是否完结、默认连载
    @Generated(hash = 1973757242)
    public BookshelfBean(Long id, @NotNull String bookId, String name, String cover,
            String time, long timeMillis, boolean isUpdate, boolean isChecked,
            boolean isEnd) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.cover = cover;
        this.time = time;
        this.timeMillis = timeMillis;
        this.isUpdate = isUpdate;
        this.isChecked = isChecked;
        this.isEnd = isEnd;
    }
    @Generated(hash = 1774987038)
    public BookshelfBean() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCover() {
        return this.cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getTimeMillis() {
        return this.timeMillis;
    }
    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
    public boolean getIsUpdate() {
        return this.isUpdate;
    }
    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }
    public boolean getIsChecked() {
        return this.isChecked;
    }
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public boolean getIsEnd() {
        return this.isEnd;
    }
    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
}
