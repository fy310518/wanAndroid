package com.fy.baselibrary.aop.clickfilter;

import android.view.View;

import com.fy.baselibrary.utils.L;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 对添加 @ClickFilter 注解的方法做统一的切面处理
 * Created by fangs on 2018/8/23 17:54.
 */
@Aspect
public class OnClikFilterAspect {

    private static int viewId = 0;
    private static Long sLastclick = 0L;
    private static final Long FILTER_TIMEM = 300L;


    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.ClickFilter * *(..))")
    public void clickFilter() {}

    @Around("clickFilter()")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) throws Throwable {

        View view = (View) joinPoint.getArgs()[0];

        if (viewId == view.getId()) {
            if (System.currentTimeMillis() - sLastclick >= FILTER_TIMEM) {
                sLastclick = System.currentTimeMillis();
                joinPoint.proceed();
            }
        } else {
            joinPoint.proceed();
        }

        viewId = view.getId();
    }
}
