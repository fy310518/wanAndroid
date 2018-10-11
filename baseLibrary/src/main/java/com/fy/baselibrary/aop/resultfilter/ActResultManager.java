package com.fy.baselibrary.aop.resultfilter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * aop方式解决 activity 或者 fragment startActivityForResult 启动新的activity 接收返回结果，重写onActivityResult()方法，造成代码凌乱问题
 * 带回调结果的跳转，管理类
 * Created by fangs on 2017/5/9.
 */
public class ActResultManager {

    private HashMap<String, HashMap<Integer, ResultCallBack>> mMap;
    private static ActResultManager instance;

    private ActResultManager() {}

    public static ActResultManager getInstance() {
        if (instance == null) {
            synchronized (ActResultManager.class) {
                if (instance == null) {
                    instance = new ActResultManager();
                }
            }
        }
        return instance;
    }

    public void startActivityForResult(Activity context, Intent intent, int requestCode, ResultCallBack callBack) {
        if (context == null || intent == null) return;

        if (callBack != null) {
            if (mMap == null) mMap = new HashMap<>();
            HashMap<Integer, ResultCallBack> resultMap = mMap.get(context.getClass().getName());
            if (resultMap == null) {
                resultMap = new HashMap<>();
                mMap.put(context.getClass().getName(), resultMap);
            }
            resultMap.put(requestCode, callBack);
        }
        context.startActivityForResult(intent, requestCode);
    }

    public void afterActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        if (mMap != null) {
            HashMap<Integer, ResultCallBack> resultMap = mMap.get(context.getClass().getName());
            if (resultMap != null) {
                ResultCallBack callBack = resultMap.get(requestCode);
                if (callBack != null) {
                    callBack.onActivityResult(requestCode, resultCode, data);
                    resultMap.remove(requestCode);
                    if (resultMap.size() == 0) {
                        mMap.remove(context.getClass().getName());
                        if (mMap.size() == 0) {
                            mMap = null;
                        }
                    }
                }
            }
        }
    }


}