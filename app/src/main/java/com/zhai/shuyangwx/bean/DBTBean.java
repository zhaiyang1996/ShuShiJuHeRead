package com.zhai.shuyangwx.bean;

import java.util.List;

public class DBTBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * room_id : 1
         * name : 萌大人
         * vod_url : https://v2.szjal.cn/20190401/mGF3y0B1/index.m3u8
         * vod_name : 喜羊羊
         * vod_dq_time : 121000
         * vod_time : 1000000
         */

        private String id;
        private String room_id;
        private String name;
        private String vod_url;
        private String vod_name;
        private String vod_dq_time;
        private String vod_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVod_url() {
            return vod_url;
        }

        public void setVod_url(String vod_url) {
            this.vod_url = vod_url;
        }

        public String getVod_name() {
            return vod_name;
        }

        public void setVod_name(String vod_name) {
            this.vod_name = vod_name;
        }

        public String getVod_dq_time() {
            return vod_dq_time;
        }

        public void setVod_dq_time(String vod_dq_time) {
            this.vod_dq_time = vod_dq_time;
        }

        public String getVod_time() {
            return vod_time;
        }

        public void setVod_time(String vod_time) {
            this.vod_time = vod_time;
        }
    }
}
