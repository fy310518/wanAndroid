package com.fy.baselibrary.application;

import android.app.Application;

import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * 基础 application
 * Created by fangs on 2017/5/5.
 */
public class BaseApp extends Application {

    /**
     * 记录应用是否被强杀
     */
    public static int mAppStatus = -1;

    @Override
    public void onCreate() {
        super.onCreate();

        new ConfigUtils.ConfigBiuder()
                .setCtx(this)
                .setBASE_URL("http://www.wanandroid.com/")
                .create();

        int designWidth = (int) ResourceUtils.getMetaData("Rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks(designWidth));

        NightModeUtils.setNightMode();
    }

}
