package com.gcstorage.framework.utils;

import android.util.Log;

/**
 * dongwenbo 2018/9/17 0017.
 * Log统一管理的工具类, 可以查看log日志,显示那个类,那个方法的日志,方便分析
 * 重点: 上线前将  idDebug 改成 flase , 第一次启动打印log会有几秒延迟
 */

public class Logger {
    private static boolean idDebug = true;   //true表示打印,flase表示关闭log, 上线打包是改成flase
    private static String className;
    private static String methodName;
    private static int lineNumber;

    public Logger() {

    }

    /**
     * 用来判断是否打印log
     *
     * @return
     */
    public static boolean isDebuggable() {
        return idDebug;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    //根据idDebug状态,判断是否打印日志
    public static void e(String tag, String message) {
        if (!isDebuggable())
            return;
        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag + "---  " + className, createLog(message));
    }


    public static void i(String tag, String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(tag + "---  " + className, createLog(message));
    }

    public static void d(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(tag + "---  " + className, createLog(message));
    }

    public static void v(String tag, String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.v(tag + "---  " + className, createLog(message));
    }

    public static void w(String tag, String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.w(tag + "---  " + className, createLog(message));
    }

    public static void wtf(String tag, String message) {
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(tag + "---  " + className, createLog(message));
    }
}
