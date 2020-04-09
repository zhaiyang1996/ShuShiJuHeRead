package com.zhai.shuyangwx.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    Context context;

    public SpacesItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = Tool.dip2px(context, 10);
        outRect.bottom = Tool.dip2px(context, 10);
    }
}