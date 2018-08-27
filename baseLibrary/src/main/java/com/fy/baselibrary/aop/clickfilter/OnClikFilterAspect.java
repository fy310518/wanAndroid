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
    private static final String TAG = "OnClikFilterAspect";

    private static int viewId = 0;
    private static Long sLastclick = 0L;
    private static final Long FILTER_TIMEM = 300L;


//    @Pointcut 注解代表切入点，具体就是指哪些方法需要被执行"AOP"
//    execution()里指定了 LoginFilter 注解的路径，即加入 LoginFilter 注解的方法就是需要处理的切面
    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.ClickFilter * *(..))")
    public void clickFilter() {}

//    @Around 注解表示这个方法执行时机的前后都可以做切面处理
//    常用到的还有@Before、@After等等。@Before即方法执行前做处理，@After反之。
    @Around("clickFilter()")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) throws Throwable {
//        此方法就是对切面的具体实现，ProceedingJoinPoint 参数意为环绕通知，这个类里面可以获取到方法的签名等各种信息

        View view = (View) joinPoint.getArgs()[0];

        if (viewId == view.getId()) {
            if (System.currentTimeMillis() - sLastclick >= FILTER_TIMEM) {
                sLastclick = System.currentTimeMillis();
                L.e("View 点击");
                joinPoint.proceed();
            } else {
                L.e("View 重复点击,已过滤");
            }
        } else {
            joinPoint.proceed();
        }

        viewId = view.getId();
    }
}
