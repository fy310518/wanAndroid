package com.fy.baselibrary.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import com.fy.baselibrary.application.ConfigUtils;

/**
 * 单位转换 和 测量 工具类
 * Created by fangs on 2017/3/1.
 */
public class DensityUtils {

    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * pt转 px
     * @param ptValue
     * @return px值
     */
    public static float pt2px(float ptValue){
        Context context = ConfigUtils.getAppCtx();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptValue, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转 px
     * @param dpVal
     */
    public static int dp2px(float dpVal) {
        Context context = ConfigUtils.getAppCtx();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param spVal
     */
    public static int sp2px(float spVal) {
        Context context = ConfigUtils.getAppCtx();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param pxVal
     */
    public static float px2dp(float pxVal) {
        Context context = ConfigUtils.getAppCtx();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     * @param pxVal
     */
    public static float px2sp(float pxVal) {
        Context context = ConfigUtils.getAppCtx();
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * px 转 pt （1pt＝1/72英寸）
     * @param pxVal
     */
    public static float px2pt(float pxVal){
        Context context = ConfigUtils.getAppCtx();
        return (pxVal / context.getResources().getDisplayMetrics().xdpi) * 72;
    }


    /**
     * 测量View的宽高
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }
}
