package com.zhai.shuyangwx.application;

import android.app.Application;
import android.util.Log;
import com.zhai.shuyangwx.bean.BookDetailBean;
import com.zhai.shuyangwx.bean.BookMixAToc;
import com.zhai.shuyangwx.bean.BookMixATocLocalBean;
import com.tencent.smtt.sdk.QbSdk;

import java.util.List;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhaiyang on 2018/6/6.
 */

public class app extends Application{
    public static BookMixAToc bookMixAToc;
    public static List<BookMixATocLocalBean> bookMixATocLocalBean;
    public static BookDetailBean bookDetailBean;
    //视频静态资源地址
    public static String url = "http://106.14.82.67/";
    //动态XX视频URL地址
    public static String xxUrl= "";
    //动态XX视频卡密
    public static String xxKM= "";
    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
        //初始化极光推送
        JPushInterface.init(this);
        //初始化极光统计
        JAnalyticsInterface.init(this);
    }
     /**
     *初始化X5
     */
    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " x5内核： " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.d("app", " x5内核： ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
