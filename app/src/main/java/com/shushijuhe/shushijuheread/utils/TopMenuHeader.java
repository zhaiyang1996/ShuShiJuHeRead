package com.shushijuhe.shushijuheread.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;


/**
 * Created by Poison on 2018/6/9.
 * 公共标题栏
 */

public class TopMenuHeader {
    private ImageView topIvLeft, topIvRight, topIvRight2;
    private TextView topTextcentre, topTextLeft;
    private View topViewFill;

    public TopMenuHeader(View v, Context context) {
        initView(v);
        fillBar(context);
    }

    //初始化控件
    private void initView(View v) {
        // top左边图片
        topIvLeft = v.findViewById(R.id.top_iv_left);
        //top左边文字
        topTextLeft = v.findViewById(R.id.top_tv_left);
        // 右边按钮图片
        topIvRight = v.findViewById(R.id.top_iv_right);
        // 右边按钮第二个图片
        topIvRight2 = v.findViewById(R.id.top_iv_right2);
        // 顶部中间文字
        topTextcentre = v.findViewById(R.id.top_tv_centre);
        //top填充沉浸式状态栏view
        topViewFill = v.findViewById(R.id.top_view_fill);
    }

    //填充沉浸式状态高度
    private void fillBar(Context context) {
        //获取状态栏高
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }

        //填充沉浸式状态栏
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topViewFill.getLayoutParams();
        //获取当前控件的布局对象
        params.height = statusBarHeight1;//设置当前控件布局的高度
        topViewFill.setLayoutParams(params);//将设置好的布局参数应用到控件中
    }


    //设置公共标题栏参数
    public void setTopMenuHeader(boolean topImageViewLeft, String topTextViewLeft, String topTextViewCentre,
                                 boolean topImageViewRight2, boolean topImageViewRight) {
        if (topImageViewLeft) {
            topIvLeft.setImageResource(R.mipmap.title_backtrack);
        } else {
            topIvLeft.setVisibility(View.INVISIBLE);
        }
        if (topImageViewRight) {
            topIvRight.setImageResource(R.mipmap.title_menu);
        } else {
            topIvRight.setVisibility(View.INVISIBLE);
        }
        if (topImageViewRight2) {
            topIvRight2.setImageResource(R.mipmap.title_seek);
        } else {
            topIvRight2.setVisibility(View.INVISIBLE);
        }
        if (!topTextViewCentre.equals("")) {
            topTextcentre.setText(topTextViewCentre);
        } else {
            topTextcentre.setVisibility(View.INVISIBLE);
        }
        if (!topTextViewLeft.equals("")) {
            topTextLeft.setText(topTextViewLeft);
        } else {
            topTextLeft.setVisibility(View.INVISIBLE);
        }
    }

    public ImageView getTopIvLeft() {
        return topIvLeft;
    }

    public ImageView getTopIvRight() {
        return topIvRight;
    }

    public ImageView getTopIvRight2() {
        return topIvRight2;
    }

    public TextView getTopTextTitle() {
        return topTextcentre;
    }

    public TextView getTopTextLeft() {
        return topTextLeft;
    }
}
