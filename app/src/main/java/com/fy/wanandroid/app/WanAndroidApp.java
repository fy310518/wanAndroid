package com.fy.wanandroid.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.fanjun.keeplive.KeepLive;
import com.fanjun.keeplive.config.ForegroundNotification;
import com.fanjun.keeplive.config.ForegroundNotificationClickListener;
import com.fanjun.keeplive.config.KeepLiveService;
import com.fy.baselibrary.application.BaseActivityLifecycleCallbacks;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.statuslayout.OnStatusAdapter;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.wanandroid.R;

/**
 * Created by fangs on 2018/7/24 17:36.
 */
public class WanAndroidApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

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

        initKeepLive();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static String CER = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDXzCCAkegAwIBAgIEMr+CgTANBgkqhkiG9w0BAQsFADBgMQwwCgYDVQQGEwMx\n" +
            "MjMxDDAKBgNVBAgTAzEyMzEMMAoGA1UEBxMDMTIzMQwwCgYDVQQKEwMxMjMxDDAK\n" +
            "BgNVBAsTAzEyMzEYMBYGA1UEAxMPMTkyLjE2OC4xMDAuMjUxMB4XDTE4MDcyMDAz\n" +
            "NTgyN1oXDTE4MTAxODAzNTgyN1owYDEMMAoGA1UEBhMDMTIzMQwwCgYDVQQIEwMx\n" +
            "MjMxDDAKBgNVBAcTAzEyMzEMMAoGA1UEChMDMTIzMQwwCgYDVQQLEwMxMjMxGDAW\n" +
            "BgNVBAMTDzE5Mi4xNjguMTAwLjI1MTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCC\n" +
            "AQoCggEBAIzONpjYUosVfh1r0cUOGXRAZ8Pxjny4D/pEQp3H5yaocAznLcDc9BD+\n" +
            "hX4fnKeE/xz2MaRody08a1HjxFlVOld2exvunrC3ErjEBcxGjWyzM7+1ArCKp3j8\n" +
            "YyzVoFw6Qx6F0Xw2DLf5YF6MMAmQyBdxWO+YhnYoLG8/ZBYE7zRBcbsm9riZOfEJ\n" +
            "gW+1zQ6LUnTOZOk2eYm1NquqIiopzLKc+BH/S8hBAwL+lh5iKlOFJyGkW4U1HDCZ\n" +
            "2qsaMeCY2UJOoPr6y2/eUkAnjSVl4IvVMPEdTfD6XcxIsvGQhDtVGzEgY8w4unoO\n" +
            "jXk7qPiHUXbFQzx9e7Mgqx2lxrGhx8UCAwEAAaMhMB8wHQYDVR0OBBYEFBNg4twq\n" +
            "4jHnGW/5d+RKreimcBc5MA0GCSqGSIb3DQEBCwUAA4IBAQBytAlH8QiY5FqwcJ4+\n" +
            "UwiwppTNNm7V8QqkIbfC8jKW7EueObLSRHqYV7+yMG/SIyPg50Pc2aDHziukhQ2s\n" +
            "QmORK/xwXPZhW3iajeDQ23ejvvSeGEYap7jMPzDaoumGfzv8cjgMFX1CLhqKp8er\n" +
            "5RAxmF4uFGJqiS1nqvcV7D0iR39s2SLWllvcY4TGqGQ/KvfPh7zyjaqbavXQjR2/\n" +
            "m4TcBsGFi8LJioLbbEPL9ya8kbjGtRfJ/usXpzEypSMqPdwV3QfabKo0tegk71N7\n" +
            "I8OoatUrvA/JHA1iYHh1edwxWLvWJF3+FEzY779nrZWUQUwFInuhY3HnaaTJmChF\n" +
            "RHNT\n" +
            "-----END CERTIFICATE-----";


    private void initKeepLive(){
        //定义前台服务的默认样式。即标题、描述和图标
        ForegroundNotification foregroundNotification = new ForegroundNotification("测试","描述", R.mipmap.ic_launcher,
                //定义前台服务的通知点击事件
                new ForegroundNotificationClickListener() {
                    @Override
                    public void foregroundNotificationClick(Context context, Intent intent) {
                    }
                });
        //启动保活服务
        KeepLive.startWork(this, KeepLive.RunMode.ENERGY, foregroundNotification,
                //你需要保活的服务，如socket连接、定时任务等，建议不用匿名内部类的方式在这里写
                new KeepLiveService() {
                    /**
                     * 运行中
                     * 由于服务可能会多次自动启动，该方法可能重复调用
                     */
                    @Override
                    public void onWorking() {
                       L.e("保活", "复活");
                    }
                    /**
                     * 服务终止
                     * 由于服务可能会被多次终止，该方法可能重复调用，需同onWorking配套使用，如注册和注销broadcast
                     */
                    @Override
                    public void onStop() {

                    }
                }
        );
    }
}
