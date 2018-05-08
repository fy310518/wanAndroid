package com.fy.baselibrary.application;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 暴力屏幕适配
 * Created by fangs on 2018/5/4.
 */
public class RudenessScreenAdapter {

    public float designWidth = 720;

    public RudenessScreenAdapter(float designWidth) {
        this.designWidth = designWidth;
    }

    /**
     * 重新计算displayMetrics.xhdpi, 使单位 pt重定义为设计稿的相对长度
     *
     * @param context
     * @param designWidth 设计稿的宽度
     */
    public static void resetDensity(Context context, float designWidth) {

        Point size = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(size);

        Resources resources = context.getResources();

        resources.getDisplayMetrics().xdpi = size.x / designWidth * 72f;

        DisplayMetrics metrics = getMetricsOnMiui(context.getResources());
        if (metrics != null) metrics.xdpi = size.x / designWidth * 72f;
    }

    /**
     * 恢复displayMetrics为系统原生状态，单位pt恢复为长度单位磅
     *
     * @see #inactivate()
     */
    public static void restoreDensity() {
        Context context = BaseApp.getAppCtx();
        context.getResources().getDisplayMetrics().setToDefaults();

        DisplayMetrics metrics = getMetricsOnMiui(context.getResources());
        if (metrics != null)
            metrics.setToDefaults();
    }

    //解决MIUI更改框架导致的MIUI7+Android5.1.1上出现的失效问题(以及极少数基于这部分miui去掉art然后置入xposed的手机)
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {

        if ("MiuiResources".equals(resources.getClass().getSimpleName()) ||
                "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }


    /**
     * 激活本方案
     */
    public void activate(Context context) {
        resetDensity(context, designWidth);
    }

    /**
     * 恢复系统原生方案
     */
    public void inactivate() {
        restoreDensity();
    }
}
