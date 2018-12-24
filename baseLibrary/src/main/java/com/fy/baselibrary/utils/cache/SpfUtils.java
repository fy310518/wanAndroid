package com.fy.baselibrary.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.fy.baselibrary.application.ioc.ConfigUtils;

import java.util.Map;

/**
 * SharedPreferences 管理工具类
 * Created by fangs on 2017/5/18.
 */
public class SpfUtils {

//    创建的Preferences文件存放位置可以在Eclipse中查看：
//	  DDMS->File Explorer /<package name>/shared_prefs/setting.xml

    private SpfUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static SharedPreferences getSpf(String fileName){
        Context ctx = ConfigUtils.getAppCtx();
        return ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 向默认的SharedPreferences文件保存String内容
     *
     * @param key   保存的键
     * @param value 保存的内容
     */
    protected static void saveStrToSpf(String fileName, String key, String value) {
        SharedPreferences.Editor editor = getSpf(fileName).edit();

        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 从默认的SharedPreferences文件 取String数据
     *
     * @param key
     * @return   没有对应的key 默认返回的""
     */
    protected static String getSpfSaveStr(String fileName, String key) {
        return getSpf(fileName).getString(key, "");
    }

    /**
     * 向默认的SharedPreferences文件保存int数据
     *
     * @param key   保存的键
     * @param value 保存的内容
     */
    protected static void saveIntToSpf(String fileName, String key, int value) {
        SharedPreferences.Editor editor = getSpf(fileName).edit();

        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 从默认的SharedPreferences文件 取int数据
     *
     * @param key
     * @return   没有对应的key  默认返回 -1
     */
    protected static int getSpfSaveInt(String fileName, String key) {
        return getSpf(fileName).getInt(key, -1);
    }

    /**
     * 向默认的SharedPreferences文件保存 long数据
     *
     * @param key   保存的键
     * @param value 保存的内容
     */
    protected static void saveLongToSpf(String fileName, String key, long value) {
        SharedPreferences.Editor editor = getSpf(fileName).edit();

        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 从默认的SharedPreferences文件 取 long数据
     *
     * @param key
     * @return   没有对应的key  默认返回 0
     */
    protected static long getSpfSaveLong(String fileName, String key) {
        return getSpf(fileName).getLong(key, 0);
    }

    /**
     * 向默认的SharedPreferences文件保存boolean内容
     * @param key
     * @param value
     */
    protected static void saveBooleanToSpf(String fileName, String key, boolean value){
        SharedPreferences.Editor editor = getSpf(fileName).edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 从默认的SharedPreferences文件获取 boolean数据
     * @param key
     * @return      没有对应的key 默认返回false
     */
    protected static boolean getSpfSaveBoolean(String fileName, String key) {
        return getSpf(fileName).getBoolean(key, false);
    }

    protected static void saveFloatToSpf(String fileName, String key, float value) {
        SharedPreferences.Editor editor = getSpf(fileName).edit();

        editor.putFloat(key, value);
        editor.apply();
    }

    protected static float getSpfSaveFloat(String fileName, String key) {
        return getSpf(fileName).getFloat(key, 0f);
    }

    /**
     * 获取所有键值对
     * @return
     */
    protected static Map<String, ?> getAll(String fileName) {
        return getSpf(fileName).getAll();
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key The key of sp.
     * @return {@code true}: yes{@code false}: no
     */
    protected static boolean contains(String fileName, @NonNull final String key) {
        return getSpf(fileName).contains(key);
    }

    /**
     * 删除已存内容
     * @param key      The key of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    protected static void remove(String fileName, @NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            getSpf(fileName).edit().remove(key).commit();
        } else {
            getSpf(fileName).edit().remove(key).apply();
        }
    }

    /**
     * 清除所有数据
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    protected static void clear(String fileName, final boolean isCommit) {
        if (isCommit) {
            getSpf(fileName).edit().clear().commit();
        } else {
            getSpf(fileName).edit().clear().apply();
        }
    }
}
