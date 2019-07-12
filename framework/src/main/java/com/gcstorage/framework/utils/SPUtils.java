package com.gcstorage.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * SharedPreferences的一个工具类
 * 调用put就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用get就能获取到保存在手机里面的数据
 *
 * @Author 郑胜
 * @Date 2016/1/12
 * @Version 1.0
 */
public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String CONFIG_NAME = "store.cfg";
    /**  该文件是私有数据，只能被应用本身访问  */
    private static final int MODE = Context.MODE_PRIVATE;

    private static final int IGNORE = Context.CONTEXT_IGNORE_SECURITY;

    /**
     * 主界面导航栏的选择状态
     */
    public static final String TABPAGE = "tabPage";

    /**
     * 保存 int 类型的数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void putInt(Context context , String key, int value) {
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().putInt(key, value).commit();
    }

    /**
     * 保存 String 类型的数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void putString(Context context , String key, String value) {
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().putString(key, value).commit();
    }

    /**
     * 保存 long 类型的数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void putLong(Context context , String key, long value) {
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().putLong(key, value).commit();
    }

    /**
     * 保存 float 类型的数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void putFloat(Context context , String key, float value) {
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().putFloat(key, value).commit();
    }

    /**
     * 保存 boolean 类型的数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void putBoolean(Context context , String key, boolean value) {
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().putBoolean(key, value).commit();
    }

    /**
     * 获取 int 类型的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 默认值
     */
    public static int getInt(Context context , String key, int defValue) {
        return context.getSharedPreferences(CONFIG_NAME, MODE).getInt(key, defValue);
    }

    /**
     * 获取 String 类型的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 默认值
     */
    public static String getString(Context context , String key, String defValue) {
        return context.getSharedPreferences(CONFIG_NAME, MODE).getString(key, defValue);
    }

    /**
     * 获取 long 类型的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 默认值
     */
    public static long getLong(Context context , String key, long defValue) {
        return context.getSharedPreferences(CONFIG_NAME, MODE).getLong(key, defValue);
    }

    /**
     * 获取 boolean 类型的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 默认值
     */
    public static boolean getBoolean(Context context , String key, boolean defValue) {
        return context.getSharedPreferences(CONFIG_NAME, MODE).getBoolean(key, defValue);
    }

    /**
     * 获取 float 类型的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 默认值
     */
    public static float getFloat(Context context , String key, float defValue) {
        return context.getSharedPreferences(CONFIG_NAME, MODE).getFloat(key, defValue);
    }

    public static void clearData(Context context){
        context.getSharedPreferences(CONFIG_NAME, MODE).edit().clear().commit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, MODE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, MODE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     */
    public static void setObject(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, MODE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static  <T> T getObject(Context context, String key, Class<T> clazz) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, MODE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}