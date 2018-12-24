package com.fy.baselibrary.utils.cache;

import android.support.annotation.NonNull;

import java.util.Map;

/**
 * describe： SharedPreferences 代理工具类
 * Created by fangs on 2018/12/24 16:39.
 */
public class SpfAgent {

    private String fileName = "app";

    public SpfAgent(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 向 SharedPreferences 保存String 数据
     * @param key   保存的键
     * @param value 保存的内容
     * @return SpfAgent
     */
    public SpfAgent saveString(String key, String value){
        SpfUtils.saveStrToSpf(fileName, key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 int 数据
     * @param key   保存的键
     * @param value 保存的内容
     * @return SpfAgent
     */
    public SpfAgent saveInt(String key, int value){
        SpfUtils.saveIntToSpf(fileName, key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 long 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveLong(String key, long value){
        SpfUtils.saveLongToSpf(fileName, key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存boolean 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveBoolean(String key, boolean value){
        SpfUtils.saveBooleanToSpf(fileName, key, value);
        return this;
    }

    /**
     * 向 SharedPreferences 保存 float 数据
     * @param key    key
     * @param value  value
     * @return SpfAgent
     */
    public SpfAgent saveFloat(String key, float value){
        SpfUtils.saveFloatToSpf(fileName, key, value);
        return this;
    }


    public String getString(String key){
        return SpfUtils.getSpfSaveStr(fileName, key);
    }

    public int getInt(String key){
        return SpfUtils.getSpfSaveInt(fileName, key);
    }

    public long getLong(String key){
        return SpfUtils.getSpfSaveLong(fileName, key);
    }

    public float getFloat(String key){
        return SpfUtils.getSpfSaveFloat(fileName, key);
    }

    public boolean getBoolean(String key){
        return SpfUtils.getSpfSaveBoolean(fileName, key);
    }


    /**
     * 获取所有键值对
     * @return
     */
    public Map<String, ?> getAll(){
        return SpfUtils.getAll(fileName);
    }

    /**
     * 删除 指定 key 的内容
     * @param key
     */
    public void remove(@NonNull final String key){
        SpfUtils.remove(fileName, key, false);
    }

    /**
     * 清除所有数据
     */
    public void clear(){
        SpfUtils.clear(fileName, false);
    }
}
