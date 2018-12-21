package com.fy.baselibrary.aop.background;

import android.app.Activity;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * describe： aop实现 在activity的 onCreate 方法执行前执行一段代码
 * Created by fangs on 2018/12/21 17:37.
 */
@Aspect
public class SvgCompatAspect {

    @Before("call(* android.support.v7.app.AppCompatActivity.onCreate(..))")
    public void activityOnCreateMethod(JoinPoint joinPoint) throws Throwable {
        Activity activity = (Activity)joinPoint.getTarget();
        SvgCompatInject.inject(activity);
    }
}
