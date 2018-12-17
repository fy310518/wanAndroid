package com.fy.baselibrary.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * describe： 方法注解 BgDrawable，为控件添加背景 todo 待实现
 * Created by fangs on 2018/11/30 17:27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BgDrawable {

    int color() default 0;

}
