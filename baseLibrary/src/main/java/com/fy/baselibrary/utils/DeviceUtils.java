package com.fy.baselibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.fy.baselibrary.application.BaseApp;

import java.io.File;

/**
 * 获取设备信息 工具类
 * <p/>
 * Created by fangs on 2017/3/22.
 */
public class DeviceUtils {

    private DeviceUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取 手机 版本号
     *
     * @return
     */
    public static String getDeviceVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        String model = Build.MODEL;

        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceMake() {
        return Build.BRAND;
    }

    /**
     * 获取设备 Android ID
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        Context context = BaseApp.getAppCtx();
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 打印设备内存信息
     */
    public static void getDevices(){
        //应用程序最大可用内存
        int maxMemory = ((int) Runtime.getRuntime().maxMemory())/1024/1024;
        //应用程序已获得内存
        long totalMemory = ((int) Runtime.getRuntime().totalMemory())/1024/1024;
        //应用程序已获得内存中未使用内存
        long freeMemory = ((int) Runtime.getRuntime().freeMemory())/1024/1024;
        System.out.println("---> maxMemory="+maxMemory+"M,totalMemory="+totalMemory+"M,freeMemory="+freeMemory+"M");

        L.e("maxMemory" + maxMemory + "totalMemory" + totalMemory + "freeMemory" + freeMemory);
    }

    /**
     * 判断设备是否 rooted.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
}
