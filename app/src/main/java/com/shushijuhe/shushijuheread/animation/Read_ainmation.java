package com.shushijuhe.shushijuheread.animation;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.shushijuhe.shushijuheread.utils.Tool;

/**
 * 阅读部分动画
 */
public class Read_ainmation {
    public static boolean caidan = false;
    public static void showMenuAinm(Activity context,View menuBody,View viewHead,View viewCancle) {
        Window window = context.getWindow();
        menuBody.setVisibility(View.VISIBLE);
        viewHead.setVisibility(View.VISIBLE);
        viewCancle.setVisibility(View.VISIBLE);
        caidan = false;
        window.setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHead.getLayoutParams());
            lp.height = 135 + Tool.getZTheight(context);
            viewHead.setLayoutParams(lp);
            viewHead.setPadding(0,Tool.getZTheight(context),0,0);
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1f,
                Animation.RELATIVE_TO_SELF,
                0);
        //动画播放的时间长度
        ta.setDuration(500);
        TranslateAnimation ta_2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                -1f,
                Animation.RELATIVE_TO_SELF,
                0);
        //动画播放的时间长度
        ta_2.setDuration(500);
        //让iv播放aa动画
        menuBody.startAnimation(ta);
        viewHead.startAnimation(ta_2);
    }

    public static void disMenuAinm(Activity context,View menuBody,View viewHead,View viewCancle) {
        Window window = context.getWindow();
        menuBody.setVisibility(View.INVISIBLE);
        viewHead.setVisibility(View.INVISIBLE);
        viewCancle.setVisibility(View.INVISIBLE);
        caidan = true;
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                1f);
        //动画播放的时间长度
        ta.setDuration(500);
        TranslateAnimation ta_2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                -1f);
        //动画播放的时间长度
        ta_2.setDuration(500);
        //让iv播放aa动画
        menuBody.startAnimation(ta);
        viewHead.startAnimation(ta_2);
    }
}
