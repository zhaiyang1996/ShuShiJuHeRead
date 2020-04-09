package com.zhai.shuyangwx.bean;

import java.util.List;

/**
 * Created by zhaiyang on 2018/5/31.
 */

public class CategoriesBean {

    /**
     * male : [{"name":"玄幻","bookCount":188244},{"name":"奇幻","bookCount":24183}]
     * ok : true
     */

    public List<MaleBean> male;
    /**
     * name : 古代言情
     * bookCount : 125103
     */

    public List<MaleBean> female;

    public static class MaleBean {
        public String name;
        public int bookCount;
    }
}
