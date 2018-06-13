package com.shushijuhe.shushijuheread.bean;

/**
 * Created by 47314 on 2018/6/7.
 */

public class VideoUrl {

    /**
     * code : 200
     * url : https://stsws.qq.com/Aqh-q8Kut17nvdYjxvW5b3GOt8iQIeEN3Ibhwoy5rnTU/akrwUT7WM3k_8iOsaKSoa7IXEInF6MiZKOn57d3kT-KHzyZO8nf0cJGOHOIKdfAwyy5fHtvXR__RCN0fe5bCvga7TiTFVO_-JhdJExPi6QZqW_HJIDnLjDzcUEXCSdr68puKLEor6cVfEBXXBn9T6J0Ox_DbdCqy/a0546eq5l1m.321003.ts.m3u8?ver=4&parse_sign=qipacao
     * type : m3u8
     * t : 2018-06-07 18:41:26
     * ip : 113.246.107.57
     * version : v2.2.8.520180528
     */

    private int code;
    private String url;
    private String type;
    private String t;
    private String ip;
    private String version;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
