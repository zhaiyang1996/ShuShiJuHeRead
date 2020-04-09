package com.zhai.shuyangwx.bean;

import java.util.List;

public class UserChatBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * isOnline : 1
         * msg : 嘻嘻
         * time : 2020-4-719:52:58
         * name : 萌大人
         * isServer : 0
         * serverMsg :
         * onLineS : 1
         */

        private String isOnline;
        private String msg;
        private String time;
        private String name;
        private String isServer;
        private String serverMsg;
        private String onLineS;

        public String getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(String isOnline) {
            this.isOnline = isOnline;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsServer() {
            return isServer;
        }

        public void setIsServer(String isServer) {
            this.isServer = isServer;
        }

        public String getServerMsg() {
            return serverMsg;
        }

        public void setServerMsg(String serverMsg) {
            this.serverMsg = serverMsg;
        }

        public String getOnLineS() {
            return onLineS;
        }

        public void setOnLineS(String onLineS) {
            this.onLineS = onLineS;
        }
    }
}
