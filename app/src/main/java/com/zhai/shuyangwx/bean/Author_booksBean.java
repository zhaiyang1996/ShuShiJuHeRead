package com.zhai.shuyangwx.bean;

import java.util.List;

/**
 * Created by zhaiyang on 2018/6/1.
 */

public class Author_booksBean {

    /**
     * _id : 559b29837a0f8b65521464a7
     * title : 申公豹传承
     * author : 第九天命
     * shortIntro : 本书主角玉独秀获得应灾劫大道而生的申公豹传承，然后又在无意间融合了一丝诸天劫难本源，有了执掌、引动大劫之力量，为众生带来劫难，可以借助大劫，来加快自己的修炼速，...
     * cover : /agent/http://image.cmfu.com/books/3533952/3533952.jpg
     * cat : 仙侠
     * majorCate : 仙侠
     * minorCate : 洪荒封神
     * latelyFollower : 776
     * retentionRatio : 69.68
     * lastChapter : 第1337章 狐媚子
     * tags : ["洪荒封神","仙侠"]
     */

    public List<TagBook> books;

    public static class TagBook {
        public String _id;
        public String title;
        public String author;
        public String shortIntro;
        public String cover;
        public String site;
        public String cat;
        public String majorCate;
        public String minorCate;
        public int latelyFollower;
        public String retentionRatio;
        public String lastChapter;
        public List<String> tags;
    }
}
