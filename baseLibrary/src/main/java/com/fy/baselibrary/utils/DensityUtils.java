package com.fy.baselibrary.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import com.fy.baselibrary.application.BaseApp;

/**
 * 单位转换 和 测量 工具类
 * <p/>
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
        Context context = BaseApp.getAppCtx();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptValue, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转 px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        Context context = BaseApp.getAppCtx();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * px 转 pt （1pt＝1/72英寸）
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2pt(Context context, float pxVal){

        return (pxVal / context.getResources().getDisplayMetrics().xdpi) * 72;
    }


    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }
}
