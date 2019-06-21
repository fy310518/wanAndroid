package com.fy.baselibrary.utils.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.fy.baselibrary.application.ioc.ConfigUtils;

import java.util.Map;

/**
 * describe： SharedPreferences 代理工具类
 * Created by fangs on 2018/12/24 16:39.
 */
public class SpfAgent {
//    创建的Preferences文件存放位置可以在Eclipse中查看：
//	  DDMS->File Explorer /<package name>/shared_prefs/setting.xml

    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SpfAgent(String fileName) {
        SharedPreferences spf = getSpf(fileName);
        this.editor = spf.edit();
    }

    /**
     * 向 SharedPreferences 保存String 数据
     * @param key   保存的键
     * @param value 保存的内容
     * @return SpfAgent
     */
    public SpfAgent saveString(String key, String value){
        this.editor.putString(key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 int 数据
     * @param key   保存的键
     * @param value 保存的内容
     * @return SpfAgent
     */
    public SpfAgent saveInt(String key, int value){
        this.editor.putInt(key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 long 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveLong(String key, long value){
        this.editor.putLong(key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存boolean 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveBoolean(String key, boolean value){
        this.editor.putBoolean(key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 float 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveFloat(String key, float value){
        this.editor.putFloat(key, value);
        return this;
    }


    /**
     * 删除 指定 key 的内容
     * @param key      The key of sp.
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        this.editor.remove(key);
        commit(isCommit);
    }

    /**
     * 清除所有数据
     * @param isCommit True to use {@link SharedPreferences.Editor#commit()},
     *                 false to use {@link SharedPreferences.Editor#apply()}
     */
    public void clear(final boolean isCommit) {
        this.editor.clear();
        commit(isCommit);
    }

    /**
     * 提交
     * @param isCommit 是否同步提交
     */
    public void commit(final boolean isCommit){
        if (isCommit) {
            this.editor.commit();
        } else {
            this.editor.apply();
        }
    }



    /**
     * 从 SharedPreferences 取String数据
     * @param fileName 文件名称
     * @param key key
     * @return   没有对应的key 默认返回的""
     */
    public static String getString(String fileName, String key){
        return getSpf(fileName).getString(key, "");
    }

    /**
     * 从 SharedPreferences 取int数据
     * @param fileName 文件名称
     * @param key key
     * @return   没有对应的key  默认返回 -1
     */
    public static int getInt(String fileName, String key){
        return getSpf(fileName).getInt(key, -1);
    }

    /**
     * 从 SharedPreferences 取 long 数据
     * @param fileName 文件名称
     * @param key key
     * @return   没有对应的key  默认返回 0
     */
    public static long getLong(String fileName, String key){
        return getSpf(fileName).getLong(key, 0);
    }

    /**
     * 从 SharedPreferences 获取 float 数据
     * @param fileName 文件名称
     * @param key       key
     * @return          没有对应的key 默认返回 0f
     */
    public static float getFloat(String fileName, String key){
        return getSpf(fileName).getFloat(key, 0f);
    }

    /**
     * 从 SharedPreferences 获取 boolean数据
     * @param fileName 文件名称
     * @param key   key
     * @return      没有对应的key 默认返回false
     */
    public static boolean getBoolean(String fileName, String key){
        return getSpf(fileName).getBoolean(key, false);
    }

    /**
     * 同上
     * @return
     */
    public static boolean getBoolean(String fileName, String key, boolean def){
        return getSpf(fileName).getBoolean(key, def);
    }


    /**
     * 获取所有键值对
     * @return
     */
    public static Map<String, ?> getAll(String fileName){
        return getSpf(fileName).getAll();
    }

    /**
     * 通过 application 获取 指定名称的 SharedPreferences
     * @param fileName 文件名称
     * @return         SharedPreferences
     */
    public static SharedPreferences getSpf(String fileName){
        Context ctx = ConfigUtils.getAppCtx();
        return ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
