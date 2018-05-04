package com.fy.baselibrary.application;


import android.app.Application;

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

        L.e("tag ==》", ScreenUtils.getScreenDensityDpi(this) + "---》比例" + ScreenUtils.getScreenDensity(this));
        L.e("tag ==》", ScreenUtils.getScreenWidth(this) + "---》高" + ScreenUtils.getScreenHeight(this));
        L.e("tag ==》", DensityUtils.pt2px(1) + "");

        int designWidth = (int) ResourceUtils.getMetaData("Rudeness_Adapter_Screen_width", 0);

        RudenessScreenAdapter screenAdapter = new RudenessScreenAdapter(designWidth);
        screenAdapter.activate();

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
