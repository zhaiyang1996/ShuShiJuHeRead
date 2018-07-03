package com.shushijuhe.shushijuheread.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;

/**
 * 刘鹏
 * 滑动监听
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
    private Activity activity;

    public MyGestureListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 251) {
            Intent intent = new Intent(activity, BookCategoryActivity.class);
            activity.startActivity(intent);
        }
//        else if(e2.getX()-e1.getX()>50){
//            Toast.makeText(MainActivity.this,"从左往右滑动",Toast.LENGTH_LONG).show();
//        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

}
