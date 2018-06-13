package com.shushijuhe.shushijuheread.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 47314 on 2018/6/7.
 */

public class VideoDataManager {
    private static String url="http://47.97.207.187/";
    public static String getVideBean(String api,String key) {
        String str;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"json.php?api="+api+"&key="+key)
                .build();
        Call call = mOkHttpClient.newCall(request);
        try {
            str = call.execute().body().string();//4.获得返回结果
            //请求成功
            if(str.isEmpty()&&str==" "){
                str = "-1";
            }
        } catch (IOException e) {
            //请求失败
            e.printStackTrace();
            str = "-1";
        }
        return str;
    }
    public static String getVideList(String api,String id) {
        String str;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"json.php?api="+api+"&id="+id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        try {
            str = call.execute().body().string();//4.获得返回结果
            //请求成功
            if(str.isEmpty()&&str==" "){
                str = "-1";
            }
        } catch (IOException e) {
            //请求失败
            e.printStackTrace();
            str = "-1";
        }
        return str;
    }
    //获取视频播放地址
    public static String getVideUrl(String id) {
        String str;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"/api/jiexi/api.php?url="+id)
                .build();
        Call call = mOkHttpClient.newCall(request);
        try {
            str = call.execute().body().string();//4.获得返回结果
            //请求成功
            if(str.isEmpty()&&str==" "){
                str = "-1";
            }
        } catch (IOException e) {
            //请求失败
            e.printStackTrace();
            str = "-1";
        }
        return str;
    }
}
