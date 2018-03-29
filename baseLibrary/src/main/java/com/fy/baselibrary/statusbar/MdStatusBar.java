package com.fy.baselibrary.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fy.baselibrary.utils.ScreenUtils;

/**
 * 状态栏和导航栏 操作工具类
 * https://github.com/Zackratos/UltimateBar
 * <br>
 * Created by github on 18/3/14.
 */
public class MdStatusBar {

    /** 状态栏透明度 */
    public static int statusAlpha = 90;

    /** 导航栏透明度 */
    public static int navAlpha = 10;

    private MdStatusBar() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 自定义 状态栏和导航栏 的颜色
     * @param act
     * @param statusColor StatusBar color
     * @param navColor    NavigationBar color
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setColorBar(Activity act, @ColorRes int statusColor, @ColorRes int navColor) {
        int statusc = ContextCompat.getColor(act, statusColor);
        int navc = ContextCompat.getColor(act, navColor);
        setColorBar(act, statusc, statusAlpha, true, navc, navAlpha);
    }

    /**
     * 自定义 状态栏和导航栏 的颜色
     *
     * @param statusColor StatusBar color
     * @param statusDepth StatusBar color depth
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navDepth    NavigationBar color depth (applyNav = true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setColorBar(Activity act, @ColorInt int statusColor, int statusDepth,
                             boolean applyNav,
                             @ColorInt int navColor, int navDepth) {

        int realStatusDepth = limitDepthOrAlpha(statusDepth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            window.setStatusBarColor(finalStatusColor);
            if (applyNav) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.setNavigationBarColor(finalNavColor);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(createStatusBarView(act, finalStatusColor));
            if (applyNav && navigationBarExist(act)) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                decorView.addView(createNavBarView(act, finalNavColor));
            }
            setRootView(act, true);
        }
    }


    /**
     * 设置 状态栏和导航栏 的 透明度
     * @param act
     * @param statusColor  StatusBar color
     * @param navColor     NavigationBar color
     */
    public static void setTransparentBar(Activity act, @ColorRes int statusColor, @ColorRes int navColor) {
        int statusc = ContextCompat.getColor(act, statusColor);
        int navc = ContextCompat.getColor(act, navColor);
        setTransparentBar(act, statusc, statusAlpha, true, navc, navAlpha);
    }

    /**
     * 设置 状态栏和导航栏 的 透明度
     *
     * @param statusColor StatusBar color
     * @param statusAlpha StatusBar alpha
     * @param applyNav    apply NavigationBar or no
     * @param navColor    NavigationBar color (applyNav == true)
     * @param navAlpha    NavigationBar alpha (applyNav == true)
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setTransparentBar(Activity act, @ColorInt int statusColor, int statusAlpha,
                                   boolean applyNav,
                                   @ColorInt int navColor, int navAlpha) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int finalStatusColor = statusColor == 0 ? Color.TRANSPARENT :
                    Color.argb(limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                            Color.green(statusColor), Color.blue(statusColor));
            window.setStatusBarColor(finalStatusColor);
            if (applyNav) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                int finalNavColor = navColor == 0 ? Color.TRANSPARENT :
                        Color.argb(limitDepthOrAlpha(navAlpha), Color.red(navColor),
                                Color.green(navColor), Color.blue(navColor));
                window.setNavigationBarColor(finalNavColor);
            }
            decorView.setSystemUiVisibility(option);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalStatusColor = statusColor == 0 ? Color.TRANSPARENT :
                    Color.argb(limitDepthOrAlpha(statusAlpha), Color.red(statusColor),
                            Color.green(statusColor), Color.blue(statusColor));
            decorView.addView(createStatusBarView(act, finalStatusColor));
            if (applyNav && navigationBarExist(act)) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                int finalNavColor = navColor == 0 ? Color.TRANSPARENT :
                        Color.argb(limitDepthOrAlpha(navAlpha), Color.red(navColor),
                                Color.green(navColor), Color.blue(navColor));
                decorView.addView(createNavBarView(act, finalNavColor));
            }
        }
    }


    /**
     * 隐藏状态栏和导航栏<br>
     * 注：实现这种效果，必须重写 Activity 的 onWindowFocusChanged 方法，在onWindowFocusChanged()中执行
     * @param applyNav apply NavigationBar
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setHideBar(Activity act, boolean applyNav) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = act.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            if (applyNav) {
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
        }
    }


    /**
     * DrawerLayout 实现状态栏和导航栏
     * @param act
     * @param statusColor
     * @param navColor
     */
    public static void setColorBarForDrawer(Activity act, @ColorRes int statusColor, @ColorRes int navColor) {
        int statusc = ContextCompat.getColor(act, statusColor);
        int navc = ContextCompat.getColor(act, navColor);
        setColorBarForDrawer(act, statusc, statusAlpha, true, navc, navAlpha);
    }

    /**
     * DrawerLayout 实现状态栏和导航栏
     * 注：必须在布局文件中 DawerLayout 的子view 的【主界面】添加 android:fitsSystemWindows="true"
     * @param statusColor
     * @param statusDepth
     * @param applyNav
     * @param navColor
     * @param navDepth
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setColorBarForDrawer(Activity act, @ColorInt int statusColor, int statusDepth,
                                      boolean applyNav,
                                      @ColorInt int navColor, int navDepth) {
        int realStatusDepth = limitDepthOrAlpha(statusDepth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            window.setStatusBarColor(Color.TRANSPARENT);

            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            decorView.addView(createStatusBarView(act, finalStatusColor), 0);
            if (applyNav && navigationBarExist(act)) {
                window.setNavigationBarColor(Color.TRANSPARENT);
                int realNavDepth = limitDepthOrAlpha(navDepth);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                decorView.addView(createNavBarView(act, finalNavColor), 1);
                option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            }
            decorView.setSystemUiVisibility(option);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            int finalStatusColor = realStatusDepth == 0 ? statusColor : calculateColor(statusColor, realStatusDepth);
            decorView.addView(createStatusBarView(act, finalStatusColor), 0);
            if (applyNav && navigationBarExist(act)) {
                int realNavDepth = limitDepthOrAlpha(navDepth);
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                int finalNavColor = realNavDepth == 0 ? navColor : calculateColor(navColor, realNavDepth);
                decorView.addView(createNavBarView(act, finalNavColor), 1);
            }
        }
    }

    /**
     * 颜色 透明度 判断（必须在 0 --- 255之间）
     * @param depthOrAlpha
     * @return
     */
    public static int limitDepthOrAlpha(int depthOrAlpha) {
        if (depthOrAlpha < 0) {
            return 0;
        }
        if (depthOrAlpha > 255) {
            return 255;
        }
        return depthOrAlpha;
    }


    private static View createStatusBarView(Context context, @ColorInt int color) {
        View statusBarView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight(context));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static View createNavBarView(Context context, @ColorInt int color) {
        View navBarView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getNavigationHeight(context));
        params.gravity = Gravity.BOTTOM;
        navBarView.setLayoutParams(params);
        navBarView.setBackgroundColor(color);
        return navBarView;
    }


    private static boolean navigationBarExist(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }


    @ColorInt
    private static int calculateColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    public static void setRootView(Activity activity, boolean fit) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(fit);
                ((ViewGroup) childView).setClipToPadding(fit);
            }
        }
    }

}
