package com.shushijuhe.shushijuheread.view;

import android.content.Context;
import android.util.AttributeSet;

import com.shushijuhe.shushijuheread.utils.ReadPageUtils;
import com.shushijuhe.shushijuheread.utils.Tool;

public class ReadingTextView extends android.support.v7.widget.AppCompatTextView {
    public Context context;
    public ReadingTextView(Context context) {
        super(context);
        this.context = context;
    }

    public ReadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReadingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ReadPageUtils readPageUtils = new ReadPageUtils();
        if(context!=null){
            readPageUtils.setToolWh(context,w,h);
        }

    }
}