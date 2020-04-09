package com.zhai.shuyangwx.bean;

import java.util.List;

/**
 * Created by zhaiyang on 2018/6/1.
 */

public class Rank_categoryBean {


    /**
     * female : [{"_id":"54d43437d47d13ff21cad58b","title":"追书最热榜 Top100",
     * "cover":"/ranking-cover/142319314350435","collapse":false,
     * "monthRank":"564d853484665f97662d0810","totalRank":"564d85b6dd2bd1ec660ea8e2"}]
     * ok : true
     */

    public List<MaleBean> female;
    /**
     * _id : 54d42d92321052167dfb75e3
     * title : 追书最热榜 Top100
     * cover : /ranking-cover/142319144267827
     * collapse : false
     * monthRank : 564d820bc319238a644fb408
     * totalRank : 564d8494fe996c25652644d2
     */

    public List<MaleBean> male;

    public static class MaleBean {
        public String _id;
        public String title;
        public String cover;
        public boolean collapse;
        public int custom_cover;
        public String monthRank;
        public String totalRank;

        public MaleBean() {
            custom_cover = -1;
        }

        public MaleBean(String title) {
            this.title = title;
        }
    }
}
