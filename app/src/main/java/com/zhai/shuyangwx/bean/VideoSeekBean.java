package com.zhai.shuyangwx.bean;

import java.util.List;

/**
 * Created by zhaiyang on 2018/6/7.
 */

public class VideoSeekBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * vod_id : 178
         * name : 庆余年
         * img : //puui.qpic.cn/vcover_vt_pic/0/rjae621myqca41h1574838571/220
         * msg : 某大学文学史专业的学生张庆熟读古典名著，但他用现代观念剖析古代文学史的论文命题不被叶教授所认可为了让叶教授成为自己的研究生导师，张庆决定通过写小说的方式，进一步阐述自己想要表达的观点。
         在他的小
         */

        private String vod_id;
        private String name;
        private String img;
        private String msg;

        public String getVod_id() {
            return vod_id;
        }

        public void setVod_id(String vod_id) {
            this.vod_id = vod_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
