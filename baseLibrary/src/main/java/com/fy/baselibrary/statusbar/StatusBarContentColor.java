package com.fy.baselibrary.statusbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏内容着色 工具类
 * Created by fangs on 18/3/14.
 */
public class StatusBarContentColor {
//      1.View.SYSTEM_UI_FLAG_VISIBLE ：状态栏和Activity共存，Activity不全屏显示。也就是应用平常的显示画面
//      2.View.SYSTEM_UI_FLAG_FULLSCREEN ：Activity全屏显示，且状态栏被覆盖掉
//      4.View.INVISIBLE ： Activity全屏显示，隐藏状态栏

//      5.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 是从API 16 开始启用，实现效果：视图延伸至状态栏区域，状态栏悬浮于视图之上
//      6.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 是从 API 23 开始启用，实现效果：
//          设置状态栏图标和状态栏文字颜色为深色，为适应状态栏背景为浅色调而启用，该Flag只有在使用了 FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS，
//          并且没有使用 FLAG_TRANSLUCENT_STATUS 时才有效，即只有在【透明状态栏时才有效】
//      7.View.SYSTEM_UI_FLAG_LAYOUT_STABLE 保持整个View稳定,
//          常和控制System UI悬浮, 隐藏的Flags共用, 使View不会因为System UI的变化而重新layout

    private StatusBarContentColor() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断手机是否是魅族
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 判断手机是否是小米
     * @return
     */
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     * @param activity
     * @param useDart 是否使用深色调
     * @param isTransparentBar 布局是否延伸到状态栏
     */
    public static void setStatusTextColor(Activity activity, boolean useDart, boolean isTransparentBar) {
        if (isFlyme()) {
            processFlyMe(activity, useDart);
        } else if (isMIUI()) {
            MIUISetStatusBarLightMode(activity ,useDart, isTransparentBar);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusTextColor2(activity, useDart, isTransparentBar);
            }
        }
    }

    @SuppressLint("InlinedApi")
    private static void setStatusTextColor2(Activity activity, boolean useDart, boolean isTransparentBar){
        int option = 0;
        int option2 = 0;

        if (isTransparentBar){
            option = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            option2 = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        } else {
            option = View.SYSTEM_UI_FLAG_VISIBLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            option2 = View.SYSTEM_UI_FLAG_VISIBLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }

        if (useDart) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(option);
        } else {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(option2);
        }
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(Activity activity, boolean useDart) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (useDart) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * 需要MIUIV6以上
     *
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark, boolean isTransparentBar) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    setStatusTextColor2(activity, dark, isTransparentBar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
