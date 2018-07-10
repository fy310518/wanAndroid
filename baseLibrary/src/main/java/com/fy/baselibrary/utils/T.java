package com.fy.baselibrary.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.ContextUtils;

/**
 * Toast统一管理类 (解决多次弹出toast)
 * Created by fangs on 2017/3/1.
 */
public class T {

    /**
     * 显示toast 开关
     */
    public static boolean isShow = true;
    private static Toast toast;

    private T() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        show(message.toString(), Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(int message) {
        show(ContextUtils.getAppCtx().getResources().getString(message), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        show(message.toString(), Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        show(ContextUtils.getAppCtx().getResources().getString(message), Toast.LENGTH_LONG);
    }

    /**
     * 显示系统 toast
     *
     * @param message 消息
     */
    private static void show(String message, int duration) {
        if (isShow) {

            if (null == toast) {
                toast = Toast.makeText(ContextUtils.getAppCtx(), message, duration);
            } else {
                toast.setText(message);
            }

            toast.setDuration(duration);
            toast.show();
        }
    }


    /**
     * 自定义 布局的Toast todo 待测
     *
     * @param duration
     * @param message
     */
    public static void showQulifier(int duration, CharSequence message) {
        if (null == toast) {
            LayoutInflater inflate = (LayoutInflater) ContextUtils.getAppCtx().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflate.inflate(R.layout.dialog_permission, null);// todo 布局待实现

            toast = new Toast(ContextUtils.getAppCtx());
            toast.setView(view);
            toast.setDuration(duration);
        } else {
            toast.setText(message);
        }

        show(message.toString(), Toast.LENGTH_LONG);
    }
}
