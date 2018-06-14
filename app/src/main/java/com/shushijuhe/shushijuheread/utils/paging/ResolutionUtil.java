package com.shushijuhe.shushijuheread.utils.paging;

/**
 * Created by Administrator on 2017/3/21.
 */

import android.content.Context;
import android.content.res.Configuration;

/**
 * Android各种屏幕分辨率适配，单位转换工具类。
 */
public class ResolutionUtil {

    // 所有UI界面标准参数
    public static final int STANDARD_SCREEN_WIDTH = 720;
    public static final int STANDARD_SCREEN_HEIGHT = 1280;
    public static final int STANDARD_SCREEN_DENSITY = 320; // DPI

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip。
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 将PX转化为标准屏幕的dip值
     *
     * @param px
     *
     * @return 运行设备上的dip值
     */
    private static int px2dipWithStand(int px) {
        return (px * 160) / STANDARD_SCREEN_DENSITY;
    }

    /**
     * 将输入的标准屏幕的数据转化成本机的数据
     *
     * @param px
     * @return
     */
    public static int pxStand2Local(Context context, int px) {
        int localPx = 0;
        int standDip = px2dipWithStand(px);
        localPx = dip2px(context, standDip);
        return localPx;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = 2;
        if (context != null) {
            fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        }

        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    //针对部分低分辨率手机。
    public static boolean isLowPixel(Context context){
        int height = context.getResources().getDisplayMetrics().heightPixels;
        if (height < 1000) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取屏幕方向是否竖屏方向
     *
     * @return
     */
    public static boolean isOrientationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 计算约束比例下短边的适应长度
     */
    public static int getAdaptiveResult(Context context, int x1) {
        float r;

        // 计算缩放因子
        if (isOrientationPortrait(context)) {
            // 如果是竖屏
            r = (float) STANDARD_SCREEN_WIDTH / (float) getScreenWidth(context);
        } else {
            // 横屏
            r = (float) STANDARD_SCREEN_WIDTH / (float) getScreenHeight(context);
        }

        int i = (int) (x1 / r + 0.5f * (x1 >= 0 ? 1 : -1));
        return i;
    }

    /**
     * 获取子容器相对于父容器居中时，父容器和子容器平行边之间的距离。
     *
     * @param fatherLength
     * @param sonLength
     * @return
     */
    public static int calculatePointForCentered(int fatherLength, int sonLength) {
        int difference = fatherLength - sonLength;
        return (int) (difference / 2f + 0.5f * (difference >= 0 ? 1 : -1));
    }
}