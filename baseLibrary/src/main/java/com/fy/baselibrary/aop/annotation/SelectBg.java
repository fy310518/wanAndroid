package com.fy.baselibrary.aop.annotation;

import android.support.annotation.DrawableRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * describe： 方法注解 SelectBg，为控件添加背景
 * Created by fangs on 2018/11/30 17:27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface SelectBg {

    /** drawable 类型（0：png、shape 图标等；1：svg 图标；2：动画 svg 图标） */
    int drawableType() default 0;

    /** Drawable 资源id */
    @DrawableRes int draId() default -1;

    /** 背景 selector 不同状态 显示不同的颜色 数组 */
    int[] colors() default {};

    /** 背景 selector View状态数组（比如按下，选中等） */
    int[] states() default {};
}
