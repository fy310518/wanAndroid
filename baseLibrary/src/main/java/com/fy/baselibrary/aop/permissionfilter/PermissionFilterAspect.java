package com.fy.baselibrary.aop.permissionfilter;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.permission.PermissionFragment;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * 对添加 @NeedPermission 注解的方法做统一的切面处理
 * Created by fangs on 2018/8/27 15:36.
 */
@Aspect
public class PermissionFilterAspect {
    private static final String TAG = "PermissionFilterAspect";


//    @Pointcut 注解代表切入点，具体就是指哪些方法需要被执行"AOP"
//    execution()里指定了 NeedPermission 注解的路径，即加入 NeedPermission 注解的方法就是需要处理的切面
    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.NeedPermission * *(..))" + " && @annotation(needPermission)")
    public void PermissionFilter(NeedPermission needPermission) {}

//    @Around 注解表示这个方法执行时机的前后都可以做切面处理
//    常用到的还有@Before、@After等等。@Before即方法执行前做处理，@After反之。
    @Around("PermissionFilter(needPermission)")
    public void AroundJoinPoint(ProceedingJoinPoint joinPoint, NeedPermission needPermission) throws Throwable {
//        此方法就是对切面的具体实现，ProceedingJoinPoint 参数意为环绕通知，这个类里面可以获取到方法的签名等各种信息

        final Object object = joinPoint.getThis();
        if (null == object || null == needPermission) return;

        PermissionFragment.newInstant(object, needPermission.value(), new PermissionFragment.OnPermission() {
            @Override
            public void hasPermission(List<String> denied, boolean isAll) {
                T.showLong("权限请求成功" + isAll);
                proceed(joinPoint);
            }

            @Override
            public void noPermission(List<String> denied) {
                T.showLong("权限请求失败");
                proceed(joinPoint);
            }
        });
    }


    private void proceed(ProceedingJoinPoint joinPoint){
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
