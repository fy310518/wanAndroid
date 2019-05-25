package com.gc.unifiedlogin;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.fy.baselibrary.application.BaseActivityLifecycleCallbacks;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.statuslayout.OnStatusAdapter;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * describe: 统一登陆 application
 * Created by fangs on 2019/5/24 22:01.
 */
public class UnifiedLoginApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化配置信息
        L.e("ActivityCallbacks", "Application--Create() 启动-----");
        new ConfigUtils.ConfigBiuder()
                .setBgColor(R.color.appHeadBg)
                .setTitleColor(R.color.white)
                .setTitleCenter(true)
//                .setCer(CER)
                .setBASE_URL("http://www.wanandroid.com/")
                .create(this);

        int designWidth = (int) ResUtils.getMetaData("rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks(designWidth, new OnStatusAdapter() {
            @Override
            public int errorViewId() {
                return R.layout.state_include_error;
            }

            @Override
            public int emptyDataView() {
                return R.layout.state_include_emptydata;
            }

            @Override
            public int netWorkErrorView() {
                return R.layout.state_include_networkerror;
            }

            @Override
            public int retryViewId() {
                return R.id.tvTry;
            }
        }));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}