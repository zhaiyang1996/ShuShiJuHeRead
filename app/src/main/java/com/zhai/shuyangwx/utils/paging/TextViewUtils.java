package com.zhai.shuyangwx.utils.paging;


import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/21.
 */

public class TextViewUtils {
    /**
     * Get the character offset closest to the specified absolute position. A typical use case is to
     * pass the result of {@link android.view.MotionEvent#getX()} and {@link android.view.MotionEvent#getY()} to this method.
     *
     * @param x The horizontal absolute position of a point on screen
     * @param y The vertical absolute position of a point on screen
     * @return the character offset for the character whose position is closest to the specified
     * position. Returns -1 if there is no layout.
     */
    public static int getOffsetForPosition(TextView textView, float x, float y) {
        if (textView.getLayout() == null) return -1;
        final int line = getLineAtCoordinate(textView, y);
        final int offset = getOffsetAtCoordinate(textView, line, x);
        System.out.println("x："+x+"y："+y);
        System.out.println("line：" + line + "offset：" + offset);
        return offset;
    }

    static int getLineAtCoordinate(TextView textView, float y) {
        y -= textView.getTotalPaddingTop();
        // Clamp the position to inside of the view.
        y = Math.max(0.0f, y);
        y = Math.min(textView.getHeight() - textView.getTotalPaddingBottom() - 1, y);
        y += textView.getScrollY();
        return textView.getLayout().getLineForVertical((int) y);
    }

    public static int getLineTop(TextView textView, float y) {
        if (textView.getLayout() == null) return -1;
        final int line = getLineAtCoordinate(textView, y);
        return textView.getLayout().getLineTop(line);
    }

    public static float getLeftInLine(TextView textView, float y, float x) {
        if (textView.getLayout() == null) return -1;
        final int line = getLineAtCoordinate(textView, y);
        final int offset = getOffsetAtCoordinate(textView, line, x);
        return textView.getLayout().getPrimaryHorizontal(offset);
    }

    private static int getOffsetAtCoordinate(TextView textView, int line, float x) {
        x = convertToLocalHorizontalCoordinate(textView, x);
        return textView.getLayout().getOffsetForHorizontal(line, x);
    }

    static float convertToLocalHorizontalCoordinate(TextView textView, float x) {
        x -= textView.getTotalPaddingLeft();
        // Clamp the position to inside of the view.
        x = Math.max(0.0f, x);
        x = Math.min(textView.getWidth() - textView.getTotalPaddingRight() - 1, x);
        x += textView.getScrollX();
        return x;
    }
}