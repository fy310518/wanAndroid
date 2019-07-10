package com.gcstorage.map.shake.api;

//import com.lzy.okgo.utils.HttpUtils;


import android.content.Context;
import android.text.TextUtils;

import com.gcstorage.framework.net.HttpUtils;
import com.gcstorage.framework.net.basebean.ApiResponse;
import com.gcstorage.framework.net.callback.GcOkhttpCallback;
import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.parkinggather.ApiConstant;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.model.HttpParams;

import java.lang.reflect.Type;
import java.util.List;

import com.gcstorage.map.shake.bean.RTSPSteam;

public class ApplyShakeHttp {

    private static ApplyShakeHttp shakeHttp;

    public static ApplyShakeHttp getInstan() {
        if (shakeHttp == null) {
            shakeHttp = new ApplyShakeHttp();
        }
        return shakeHttp;
    }

    //摇一摇
    public void shake(Context tag, String alarm, String token, String gps_w, String gps_h, String shakeType,
                      String distance, ActionCallbackListener<List<ShakeCameraModel>> callback) {
        HttpParams params = new HttpParams();
        params.put("action", "sharkgetdata");
        params.put("alarm", alarm);
        params.put("token", token);
        params.put("gps_w", gps_w);
        params.put("gps_h", gps_h);
        params.put("type", shakeType);
        params.put("distance", distance);
        Type type = new TypeToken<ApiResponse<List<ShakeCameraModel>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.SHAKE_URL, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    /**
     * 根据摄像头ID,获取摄像头的视频播放流.由网力提供
     */
    public void getRtspURL(String camera_id, Object tag, ActionCallbackListener<RTSPSteam> callback) {
        String shake_rtsp_url = ApiConstant.SHAKE_RTSP_URL;
        HttpParams params = new HttpParams();
        params.put("action", "get_video_rtsp");
        params.put("alarm", "lzyadmin"/*GlobalUserInfo.getAlarm(this)*/);
        params.put("token", "tokentoken");//暂时没有验证
        params.put("cameraId", camera_id);
        params.put("videoType", "live");//视频流类型live实时流，vod历史流
        Type type = new TypeToken<ApiResponse<RTSPSteam>>() {
        }.getType();
        HttpUtils.get(shake_rtsp_url, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    /**
     * 心跳包
     * @param alarm
     * @param token
     * @param tag
     * @param gps_h
     * @param gps_w
     * @param callback
     */
    public void heartbeat(String alarm, String token, Object tag,
                          String gps_h, String gps_w, ActionCallbackListener<Void> callback) {
        HttpParams params = new HttpParams();
        params.put("alarm", alarm);
        params.put("token", token);
        if (!TextUtils.isEmpty(gps_h) && !"0.0".equals(gps_h)) {
            params.put("gps_h", gps_h);
        }
        if (!TextUtils.isEmpty(gps_w) && !"0.0".equals(gps_w)) {
            params.put("gps_w", gps_w);
        }
        Type type = new TypeToken<ApiResponse<Void>>() {
        }.getType();
        HttpUtils.get(ApiConstant.HEART_BEAT_URL, tag, params, new GcOkhttpCallback<>(type, callback));
    }
}
