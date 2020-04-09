package com.zhai.shuyangwx.http;

import android.util.Log;

import com.zhai.shuyangwx.application.app;
import com.zhai.shuyangwx.bean.KMBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 47314 on 2018/6/7.
 */

public class VideoDataManager {
    private static String url="http://yangzimeng.vip:8080/VodJson/servlet/";
    public static String  getVideBean(String name) {
        String str;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"getVodSeekJson?name="+name)
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
    public static String getVideList(String vod_id,String type) {
        String str;
        String path;
        if(type!=null&&!type.equals("-1")){
            path = app.xxUrl+"/VodJson/servlet/getVodJson?vod_id="+vod_id;
        }else{
            path = url+"getVodJson?vod_id="+vod_id;
        }
        Log.d("url：", path);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
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
        Log.d("视频播放数据!:", str);
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

    /**
     * 用户留言
     * @return
     */
    public static String setMsg(String name,String msg,String time) {
        String str;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"setMsg?name="+name+"&msg="+msg+"&time="+time)
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
