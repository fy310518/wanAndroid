package com.fy.baselibrary.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注解 StatusBar，用来注解方法，
 * Created by fangs on 2018/8/24 14:00.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StatusBar {

    /**
     * 状态栏颜色
     * @return
     */
    int statusColor() default 0;
    /**
     * 状态栏透明度
     * @return
     */
    int statusAlpha() default 0;


    /**
     * 导航栏颜色
     * @return
     */
    int navColor() default 0;
    /**
     * 导航栏 透明度
     * @return
     */
    int navAlpha() default 0;

}
