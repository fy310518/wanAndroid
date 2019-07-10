package com.gcstorage.map.maker;

import com.gcstorage.framework.net.HttpUtils;
import com.gcstorage.framework.net.basebean.ApiResponse;
import com.gcstorage.framework.net.callback.GcOkhttpCallback;
import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.parkinggather.UAVHttpAddress;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.model.HttpParams;

import java.lang.reflect.Type;

public class MapApi {
    private static MapApi mHttp;

    public static MapApi getInstan() {
        if (mHttp == null) {
            mHttp = new MapApi();
        }
        return mHttp;
    }

    /**
     * 移动端创建标记点
     *
     * @param taskId
     * @param signType
     * @param signName
     * @param jsonrang
     * @param description
     * @param userId
     * @param userName
     * @param tag
     * @param callback
     */
    public void createMaker(String taskId, Integer signType, String signName, String jsonrang,
                            String description, String userId, String userName, Object tag, ActionCallbackListener<String> callback) {
        HttpParams params = new HttpParams();
        params.put("taskId", taskId);
        params.put("signType", String.valueOf(signType));
        params.put("signName", signName);
        params.put("rang", jsonrang); //地址  json
        params.put("description", description);
        params.put("userId", userId);
        params.put("userName", userName);
        Type type = new TypeToken<ApiResponse<String>>() {
        }.getType();
        HttpUtils.post(UAVHttpAddress.UAV_TAKS_CREATE_MAKER, tag, params, new GcOkhttpCallback<>(type, callback));
    }
}
