package com.fy.baselibrary.aop.permissionfilter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.fy.baselibrary.aop.annotation.PermissionDenied;
import com.fy.baselibrary.permission.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 对添加 @PermissionDenied 注解的方法做统一的切面处理
 * Created by fangs on 2018/8/27 17:09.
 */
@Aspect
public class PermissionAspect {
    private static final String TAG = "PermissionAspect";

//    @Pointcut 注解代表切入点，具体就是指哪些方法需要被执行"AOP"
//    execution()里指定了 NeedPermission 注解的路径，即加入 NeedPermission 注解的方法就是需要处理的切面
    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.PermissionDenied * *(..))" + " && @annotation(needPermission)")
    public void PermissionAround(PermissionDenied needPermission) {}


//    @Around 注解表示这个方法执行时机的前后都可以做切面处理
//    常用到的还有@Before、@After等等。@Before即方法执行前做处理，@After反之。
    @Around("PermissionAround(needPermission)")
    public void AroundJoinPoint(ProceedingJoinPoint joinPoint, PermissionDenied needPermission) throws Throwable {
//        此方法就是对切面的具体实现，ProceedingJoinPoint 参数意为环绕通知，这个类里面可以获取到方法的签名等各种信息

        final Object object = joinPoint.getThis();
        Context context = null;
        if (object instanceof Activity) {
            context = (Activity) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof Service){
            context = (Service) object;
        }

        PermissionUtils.getRequestPermissionList(context, needPermission.value());

        joinPoint.proceed();
    }

}
