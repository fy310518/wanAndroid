package com.fy.baselibrary.aop.statusbar;

import android.app.Activity;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.statusbar.MdStatusBar;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;

@Aspect
public class StatusBarAspect {

    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.StatusBar * *(..))" + " && @annotation(param)")
    public void statusBar(StatusBar param) {}

    @Around("statusBar(param)")
    public void clickFilterHook(ProceedingJoinPoint joinPoint, StatusBar param) throws Throwable {

        Activity activity = null;
        final Object object = joinPoint.getThis();
        if (null == object) return;

        if (object instanceof Activity) {
            activity = ((Activity)object);
        }
        if (null == activity) return;

//        // 取出方法的注解
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        if (!method.isAnnotationPresent(StatusBar.class)) return;
//
//        StatusBar statusBar = method.getAnnotation(StatusBar.class);

        MdStatusBar.StatusBuilder.init()
                .setStatusColor(param.statusColor(), param.statusAlpha())
                .setNavColor(param.navColor(), param.navAlpha())
                .setColorBar(activity);

        joinPoint.proceed();
    }
}
