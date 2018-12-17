package com.fy.baselibrary.aop.viewbg;

import com.fy.baselibrary.aop.annotation.BgDrawable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * describe： 对添加 @BgDrawable 注解的字段 添加背景
 * Created by fangs on 2018/12/14 18:15.
 */
@Aspect
public class BgDrawableAspect {
    private static final String TAG = "BgDrawableAspect";


    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.BgDrawable * *(..))" + " && @annotation(bgDrawable)")
    public void BgDrawable(BgDrawable bgDrawable) {}

    @Around("BgDrawable(bgDrawable)")
    public void BeforeJoinPoint(ProceedingJoinPoint joinPoint, BgDrawable needPermission) throws Throwable {

    }
}
