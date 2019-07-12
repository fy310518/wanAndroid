package com.gcstorage.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放一些公用的工具方法
 * 1.通过拆分字符串形成对应的key-value的map
 * Created by zjs on 2019/1/15.
 */

public class CommonUtils {

    /**
     * 解析字符串为key-value的map
     * @param source 字符串
     * @param firstSpilt 第一次拆分条件
     * @param secondSpilt 第二次拆分条件
     * @return 结果
     */
    public static Map<String,String> splitStringToMap(String source,String firstSpilt,String secondSpilt){
        Map<String,String> data = new HashMap<>();
        String[] split = source.split(firstSpilt);
        for (String s : split) {
            String[] second = s.split(secondSpilt);
            if(second.length == 1){
                data.put(second[0],"");
            }else if(second.length == 2){
                data.put(second[0],second[2]);
            }
        }
        return data;
    }
}
