package com.fy.baselibrary.aop.viewbg;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.fy.baselibrary.aop.annotation.SelectBg;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.drawable.TintUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * describe： 对添加 @SelectBg 注解的字段 添加背景
 * Created by fangs on 2018/12/14 18:15.
 */
@Aspect
public class SelectBgAspect {
    private static final String TAG = "SelectBgAspect";

//    @Pointcut("execution(@com.fy.baselibrary.aop.annotation.SelectBg * *(..))" + " && @annotation(bgDrawable)")
//    public void BgDrawable(SelectBg selectBg) {}
//
//    @Around("SelectBg(selectBg)")
//    public void BeforeJoinPoint(ProceedingJoinPoint joinPoint, SelectBg selectBg) throws Throwable {
//        Object returnValue = joinPoint.proceed();
//        if (null == returnValue || null == selectBg) return;
//
//        View view = (View) returnValue;
//        int[] colors = new int[selectBg.states().length];
//        int[][] states = new int[selectBg.states().length][];
//        for (int i = 0; i < selectBg.states().length; i++) {
//            states[i] = new int[]{selectBg.states()[i]};
//            colors[i] = ResUtils.getColor(selectBg.colors()[i]);
//        }
//
//        Drawable drawable = TintUtils.getDrawable(selectBg.draId(), selectBg.drawableType());
//        Drawable viewBg = TintUtils.tintSelector(drawable, colors, states);
//        view.setBackground(viewBg);
//    }



}
