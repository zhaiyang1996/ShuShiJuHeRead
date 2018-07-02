package com.shushijuhe.shushijuheread.view;

import android.content.Context;
import android.util.AttributeSet;

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
    }
}