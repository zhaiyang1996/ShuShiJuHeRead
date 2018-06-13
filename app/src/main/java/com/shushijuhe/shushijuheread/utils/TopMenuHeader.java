package com.shushijuhe.shushijuheread.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;


/**
 * Created by Poison on 2018/6/9.
 * 公共标题栏
 */

public class TopMenuHeader {
    public ImageView topIvLeft, topIvRight, topIvRight2;
    public TextView topTextTitle;

    public TopMenuHeader(View v) {
        // 左边按钮图片
        topIvLeft = v.findViewById(R.id.top_iv_left);
        // 右边按钮图片
        topIvRight = v.findViewById(R.id.top_iv_right);
        // 右边按钮第二个图片
        topIvRight2 = v.findViewById(R.id.top_iv_right2);
        // 顶部中间文字
        topTextTitle = v.findViewById(R.id.top_tv_title);
    }
    public View getView(int i){
        View view = null;
        switch (i){
            case 0:
                view = topIvLeft;
                break;
            case 1:
                view = topIvRight;
                break;
            case 2:
                view = topIvRight2;
                break;
            case 3:
                view = topTextTitle;
                break;
            default:
                break;
        }
        return view;
    }

    public void setTopMenuHeader(int topImageViewLeft, int topImageViewRight,
                                 int topImageViewRight2, String topTextViewTitle) {
        if (topImageViewLeft > 0) {
            topIvLeft.setImageResource(topImageViewLeft);
        } else {
            topIvLeft.setVisibility(View.GONE);
        }
        if (topImageViewRight > 0) {
            topIvRight.setImageResource(topImageViewRight);
        } else {
            topIvRight.setVisibility(View.GONE);
        }
        if (topImageViewRight2 > 0) {
            topIvRight2.setImageResource(topImageViewRight2);
        } else {
            topIvRight2.setVisibility(View.GONE);
        }
        if (!topTextViewTitle.equals("")) {
            topTextTitle.setText(topTextViewTitle);
        } else {
            topTextTitle.setVisibility(View.GONE);
        }


    }
}
