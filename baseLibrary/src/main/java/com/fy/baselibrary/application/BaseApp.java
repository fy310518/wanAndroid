package com.fy.baselibrary.application;

import android.app.Application;

import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.utils.SpfUtils;

/**
 * 基础 application
 * Created by fangs on 2017/5/5.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new ConfigUtils.ConfigBiuder()
                .setCer(CER)
                .setBASE_URL("http://www.wanandroid.com/")
                .create(this);

        int designWidth = (int) ResUtils.getMetaData("Rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

        //通过缓存的 进程id 判断应用是否被强杀
        int processId = SpfUtils.getSpfSaveInt(Constant.appProcessId);
        if (processId != -1 && processId != AppUtils.getProcessId(this)) {
            Constant.isRunState = "isRunState";//说明重启
        }

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks(designWidth));

        NightModeUtils.setNightMode();
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
}
