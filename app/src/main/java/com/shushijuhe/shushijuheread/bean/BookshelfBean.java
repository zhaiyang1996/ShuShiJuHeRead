package com.shushijuhe.shushijuheread.bean;

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
    private String name;//书名
    private String cover;//封面图
    private String date;//日期
    private String time;//时间
    private String chapter;//最新章节
    public String getChapter() {
        return this.chapter;
    }
    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCover() {
        return this.cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1348266877)
    public BookshelfBean(Long id, @NotNull String name, String cover, String date,
            String time, String chapter) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.date = date;
        this.time = time;
        this.chapter = chapter;
    }
    @Generated(hash = 1774987038)
    public BookshelfBean() {
    }
}
