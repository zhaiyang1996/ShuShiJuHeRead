package com.zhai.shuyangwx.bean;

import java.util.List;

/**
 * 留言板实体类
 */
public class MsgBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 游客
         * msg : 萌萌好可爱~
         * msg_admin : 嘻嘻，我也觉得
         * time : 1581261488
         */

        private String name;
        private String msg;
        private String msg_admin;
        private String time;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg_admin() {
            return msg_admin;
        }

        public void setMsg_admin(String msg_admin) {
            this.msg_admin = msg_admin;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
