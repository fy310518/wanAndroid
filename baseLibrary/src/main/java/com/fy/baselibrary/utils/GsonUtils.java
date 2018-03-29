package com.fy.baselibrary.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * Gson 工具类
 * Created by fangs on 2017/7/18.
 */
public class GsonUtils {

    private GsonUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将bean转换成Json字符串
     * @param bean
     * @return
     */
    public static String beanToJSONString(Object bean) {
        return new Gson().toJson(bean);
    }

    /**
     * json字符串 转换成 json对象
     * @param jsonStr
     * @return
     */
    public static JsonObject jsonStrToJsonObj(String jsonStr){
        JsonObject returnData = new JsonParser().parse(jsonStr).getAsJsonObject();

        return returnData;
    }

    /**
     * 将Json字符串转换成对象
     * @param json
     * @param beanClass
     * @return
     */
    public static Object JSONToObject(String json,Class beanClass) {
        Gson gson = new Gson();
        Object res = gson.fromJson(json, beanClass);
        return res;
    }

    /**
     * 将 map 转换成 json字符串
     * @param params
     * @return
     */
    public static String mapToJsonStr(Map<String, Object> params){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        return gson.toJson(params);
    }


    /**
     * 将Json字符串转换成 List集合
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static<T> List<T> jsonToList(String jsonStr){
        Gson gson = new Gson();

        List<T> list = gson.fromJson(jsonStr, new TypeToken<List<T>>(){}.getType());

        return list;
    }

    /**
     * 将 list 转换成json字符串
     * @return
     */
    public static<T> String listToJson(List<T> data){
        Gson gson = new Gson();

        return gson.toJson(data);
    }

}
