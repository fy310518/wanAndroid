package com.fy.baselibrary.application;

import android.app.Application;

import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.ResourceUtils;

/**
 * 基础 application
 * Created by fangs on 2017/5/5.
 */
public class BaseApp extends Application {

    /** 记录应用是否被强杀 */
    public static int mAppStatus = -1;
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
