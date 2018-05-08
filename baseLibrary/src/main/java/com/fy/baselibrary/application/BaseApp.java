package com.fy.baselibrary.application;


import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * 基础 application
 * Created by fangs on 2017/5/5.
 */
public class BaseApp extends Application {

    private static BaseApp mApplication; // 单例模式

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        int designWidth = (int) ResourceUtils.getMetaData("Rudeness_Adapter_Screen_width", 0);
        RudenessScreenAdapter screenAdapter = new RudenessScreenAdapter(designWidth);
        screenAdapter.activate(this);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks(screenAdapter));

        NightModeUtils.setNightMode();
    }

    /**
     * 单例模式，获取 BaseApplication的实例
     *
     * @return
     */
    public static BaseApp getAppCtx() {
        return mApplication;
    }
}
