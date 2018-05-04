package com.fy.baselibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.fy.baselibrary.application.BaseApp;

import java.util.Random;

/**
 * 获取项目资源 （如：res目录下的，assets 目录下的，manifest文件配置的资源等）</br>
 * Created by fangs on 2017/9/13.
 */
public class ResourceUtils {

    private ResourceUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取 dimens 资源文件 指定 id 的资源
     *
     * @param dimenId
     * @return
     */
    public static float getDimen(@DimenRes int dimenId) {
        return BaseApp.getAppCtx().getResources().getDimension(dimenId);
    }

    /**
     * 获取 colors 资源文件 指定 id 的资源 (getResources().getColor 过时 替代方式)
     *
     * @param colorId
     * @return
     */
    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(BaseApp.getAppCtx(), colorId);
    }

    /**
     * 生成随机颜色
     *
     * @return
     */
    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return Color.rgb(r, g, b);
    }

    /**
     * 获取 strings 资源文件 指定 id 的资源
     *
     * @param stringId
     * @return
     */
    public static String getStr(@StringRes int stringId) {
        return BaseApp.getAppCtx().getResources().getString(stringId);
    }

    /**
     * %d   （表示整数）
     * %f   （表示浮点数）
     * %s   （表示字符串）<br>
     * 获取 strings 资源文件，指定 id 的资源，替换后的字符串
     *
     * @param id   资源ID（如：ID内容为 “病人ID：%1$d”）
     * @param args 将要替换的内容
     * @return 替换后的字符串
     */
    public static String getReplaceStr(@StringRes int id, Object... args) {
        String format = getStr(id);
        return String.format(format, args);
    }

    /**
     * 获取清单文件 指定 key的 <meta-data> 的值；（<meta-data>是一个键值对）</b>
     * @param metaKey     <meta-data> 的 key
     * @param metaValueType   返回值类型
     * @return
     */
    public static Object getMetaData(String metaKey, Object metaValueType) {
        Object value = null;

        try {
            Context context = BaseApp.getAppCtx();
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            Bundle bundle = ai.metaData;
            if (metaValueType instanceof Integer) {
                value = bundle.getInt(metaKey);
            } else if (metaValueType instanceof Float){
                value = bundle.getFloat(metaKey);
            } else if (metaValueType instanceof Boolean){
                value = bundle.getBoolean(metaKey);
            } else if (metaValueType instanceof String){
                value = bundle.getString(metaKey);
            }

        } catch (Exception e) {
            L.e("获取清单文件配置失败", "Dear developer. Don't forget to configure <meta-data android:name=\"my_test_metagadata\" android:value=\"testValue\"/> in your AndroidManifest.xml file.");
        }

        return value;
    }


    /**
     * 得到raw目录下某个文件内容
     * @param resId raw 资源ID
     * (一般是 MP3和 Ogg等文件，在raw(raw是Resources 的子目录)文件内的资源会原封不动的拷贝到APK中，
     *              而不会像其它资源文件那样被编译成二进制的形式)
     * @return
     */

}
