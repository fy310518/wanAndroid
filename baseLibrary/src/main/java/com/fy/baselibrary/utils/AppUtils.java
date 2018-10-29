package com.fy.baselibrary.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.fy.baselibrary.ioc.ConfigUtils;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * App相关的辅助类
 *
 * Created by fangs on 2017/3/1.
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序名称
     * @return appName
     */
    public static String getAppName() {
        try {
            Context context = ConfigUtils.getAppCtx();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用的版本号 versionCode
     * @return versionCode
     */
    public static int getVersionCode() {
        Context context = ConfigUtils.getAppCtx();
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return 1;
        }
        return info.versionCode;
    }

    /**
     * 获取当前应用的版本名称 versionName
     * @return versionName
     */
    public static String getVersionName() {
        Context context = ConfigUtils.getAppCtx();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0.0.0";
        }

        return packageInfo.versionName;
    }

    /**
     * 获取当前应用的 应用id
     * @return 应用id（包名）
     */
    public static String getLocalPackageName() {
        Context context = ConfigUtils.getAppCtx();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "获取失败";
        }
        return info.packageName;
    }

    /**
     * 当前的包是否存在
     * @param pckName 包名
     * @return true/false
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo pckInfo = packageManager.getPackageInfo(pckName, 0);

            if (null != pckInfo) return true;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("TDvice", e.getMessage());
        }
        return false;
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回应用程序的签名
     *
     * @param packageName The name of the package.
     * @return the application's signature
     */
    public static Signature[] getAppSignature(final String packageName) {
        Context context = ConfigUtils.getAppCtx();

        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Android 7.0 在应用间共享文件
     * 获取 清单文件注册的 文件共享签名认证
     */
    public static String getFileProviderName() {
        return getLocalPackageName() + ".provider";
    }

    /**
     * 判断app是否在前台还是在后台运行
     * @return true/false
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
//                BACKGROUND=400 EMPTY=500 FOREGROUND=100
//                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200

                int pid = appProcess.pid;

                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                    L.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    L.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前应用 进程id
     * @return id
     */
    public static int getProcessId(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
//                BACKGROUND=400 EMPTY=500 FOREGROUND=100
//                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200

                return appProcess.pid;

            }
        }
        return -1;
    }


}
