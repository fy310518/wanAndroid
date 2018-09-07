package com.fy.wanandroid.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;

import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.TintUtils;

import com.fy.wanandroid.R;

/**
 * 代码设置 选择器
 * Created by fangs on 2018/4/2.
 */
public class SelectUtils {

    private SelectUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取 指定 ID的 drawable，生成的 选择器
     * @param draId
     * @return
     */
    public static Drawable getSelector(@DrawableRes int draId, int drawableType){
        int[] colors = new int[]{ResUtils.getColor(R.color.button_pressed),
                ResUtils.getColor(R.color.button_pressed),
                ResUtils.getColor(R.color.button_pressed),
                ResUtils.getColor(R.color.button_normal)};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{android.R.attr.state_checked};
        states[3] = new int[]{};

        Drawable drawable = TintUtils.getDrawable(draId, drawableType);
        StateListDrawable stateListDrawable = TintUtils.getStateListDrawable(drawable, states);

        return TintUtils.tintSelector(stateListDrawable, colors, states);
    }

    /**
     * 根据 drawable ID ，生成 选择器
     * normal：白色；
     * pressed：灰色
     * @param draId
     * @return
     */
    public static Drawable getTagSelector(@DrawableRes int draId, int drawableType){
        int[] colors = new int[]{
                ResUtils.getColor(R.color.button_pressed2),
                ResUtils.getColor(R.color.button_normal2)};

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};

        Drawable drawable = TintUtils.getDrawable(draId, drawableType);
//        设置 着色器模式
//        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);
        return TintUtils.tintSelector(drawable, colors, states);
    }

    /**
     * 根据 drawable ID ，生成 选择器
     * normal：白色；
     * pressed：灰色
     * @param draId
     * @return
     */
    public static Drawable getBtnSelector(@DrawableRes int draId, int drawableType){
        int[] colors = new int[]{
                ResUtils.getColor(R.color.button_pressed),
                ResUtils.getColor(R.color.button_normal)};

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};

        Drawable drawable = TintUtils.getDrawable(draId, drawableType);
//        设置 着色器模式
//        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);
        return TintUtils.tintSelector(drawable, colors, states);
    }
}
