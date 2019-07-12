package com.gcstorage.framework.net;


import android.util.Log;

import java.util.Hashtable;

/**
 * The class for print log
 *
 * @author kesenhoo
 */
public class LogUtil {
    private final static boolean logFlag = true;

    //private final static boolean logFlag = true;

    public final static String TAG = "main";
    private final static int logLevel = Log.VERBOSE;
    private static Hashtable<String, LogUtil> sLoggerTable = new Hashtable<String, LogUtil>();
    private String mClassName;
    // 不同开发人员的日志使用对象
    private static LogUtil jlog;
    private static LogUtil klog;
    private static LogUtil yangrui;
    private static LogUtil xutianxiong;
    // 开发人员的名字
    private static final String JAMES = "@james@ ";
    private static final String KESEN = "@kesen@ ";
    private static final String YANGRUI = "@yangrui@ ";
    private static final String XUTIANXIONG = "@xutianxiong@ ";

    private LogUtil(String name) {
        mClassName = name;
    }

    /**
     * @param className
     * @return
     */
    @SuppressWarnings("unused")
    private static LogUtil getLogger(String className) {
        LogUtil classLogger = sLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new LogUtil(className);
            sLoggerTable.put(className, classLogger);

        }
        return classLogger;
    }

    /**
     * Purpose:Mark user one
     *
     * @return
     */
    public static LogUtil kLog() {
        if (klog == null) {
            klog = new LogUtil(KESEN);
        }
        return klog;
    }

    /**
     * Purpose:Mark user two
     *
     * @return
     */
    public static LogUtil jLog() {
        if (jlog == null) {
            jlog = new LogUtil(JAMES);
        }
        return jlog;
    }

    public static LogUtil yangRui() {
        if (yangrui == null) {
            yangrui = new LogUtil(YANGRUI);
        }
        return yangrui;
    }

    public static LogUtil xuTianXiong() {
        if (xutianxiong == null) {
            xutianxiong = new LogUtil(XUTIANXIONG);
        }
        return xutianxiong;
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    public String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return mClassName + "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " " + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * The Log Level:i tint
     *
     * @param str
     */
    public void i(Object str) {
        if (logFlag) {
            if (logLevel <= Log.INFO) {
                String name = getFunctionName();
                if (name != null) {
                    Log.i(TAG, name + " - " + str);
                } else {
                    Log.i(TAG, str.toString());
                }
            }
        }

    }

    /**
     * The Log Level:d
     *
     * @param str
     */
    public void d(Object str) {
        if (logFlag) {
            if (logLevel <= Log.DEBUG) {
                String name = getFunctionName();
                if (name != null) {
                    Log.d(TAG, name + " - " + str);
                } else {
                    Log.d(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:V
     *
     * @param str
     */
    public void v(Object str) {
        if (logFlag) {
            if (logLevel <= Log.VERBOSE) {
                String name = getFunctionName();
                if (name != null) {
                    Log.v(TAG, name + " - " + str);
                } else {
                    Log.v(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:w
     *
     * @param str
     */
    public void w(Object str) {
        if (logFlag) {
            if (logLevel <= Log.WARN) {
                String name = getFunctionName();
                if (name != null) {
                    Log.w(TAG, name + " - " + str);
                } else {
                    Log.w(TAG, str.toString());
                }
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param str
     */
    public void e(Object str) {
        if (logFlag) {
            String name = getFunctionName();
            if (name != null && str != null) {
                Log.e(TAG, name + " - " + str.toString());
            } else {
                Log.e(TAG, "--null--");
            }
        }
    }

    public void e() {
        if (logFlag) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(TAG, name + " - " + System.currentTimeMillis());
            } else {
                Log.e(TAG, ":" + System.currentTimeMillis());
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param
     */
    public void Y() {
        if (logFlag) {
            String name = getFunctionName();
            if (name != null) {
                Log.e(TAG, name + " - " + System.currentTimeMillis());
            } else {
                Log.e(TAG, ":" + System.currentTimeMillis());
            }
        }
    }

    /**
     * The Log Level:e
     *
     * @param
     */
    public void e(Exception response) {
        if (logFlag) {
            Log.e(TAG, "error", response);
        }
    }

    /**
     * The Log Level:e
     *
     * @param log
     * @param tr
     */
    public void e(String log, Throwable tr) {
        if (logFlag) {
            String line = getFunctionName();
            Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + mClassName + line + ":] " + log + "\n", tr);
        }
    }
}