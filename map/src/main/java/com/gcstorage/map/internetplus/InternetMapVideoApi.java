package com.gcstorage.map.internetplus;

import android.content.Context;

import com.gcstorage.framework.net.HttpUtils;
import com.gcstorage.framework.net.basebean.ApiResponse;
import com.gcstorage.framework.net.callback.GcOkhttpCallback;
import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.parkinggather.ApiConstant;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.model.HttpParams;

import java.lang.reflect.Type;
import java.util.List;

public class InternetMapVideoApi {

    private static InternetMapVideoApi videoApi;

    public static InternetMapVideoApi getInstan() {
        if (videoApi == null) {
            videoApi = new InternetMapVideoApi();
        }
        return videoApi;
    }

    public void getVideoListByLL(Context tag, String longitude, String latitude,
                                 String name, String user_id, ActionCallbackListener<List<InternetPlusBean>> callback) {
        HttpParams params = new HttpParams();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("name", name);
        params.put("userId", user_id);
        Type type = new TypeToken<ApiResponse<List<InternetPlusBean>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_LIST, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    //搜索视频的时候,筛选条件
    public void getDic(Context tag, ActionCallbackListener callback) {
        HttpParams params = new HttpParams();
        Type type = new TypeToken<ApiResponse<List<InternetPlusBean>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_SEARCH, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    /**
     * 根据条件搜索视频资源
     *
     * @param longitude  经度
     * @param latitude   纬度
     * @param diviceName       设备名称
     * @param pathId     场所类型
     * @param deviceType 设备类型
     * @param md         设备分类
     * @param orgId      部门id
     * @param user_id    用户唯一ID
     * @param callback
     */
    public void getVideoListBySeeach(Context tag, String longitude, String latitude,
                                     String diviceName, String pathId, String deviceType, String md,
                                     String orgId, String user_id, ActionCallbackListener<List<InternetPlusBean>> callback) {
        HttpParams params = new HttpParams();
        params.put("longitude", longitude + "");
        params.put("latitude", latitude + "");
        params.put("name", diviceName + "");
        params.put("pathId", pathId + ""); //场所类型
        params.put("deviceType", deviceType + ""); //设备类型
        params.put("manufacturerDeviceType", md + ""); //设备分类
        params.put("orgId", orgId + ""); //部门id
        params.put("userId", user_id);   //必填
        Type type = new TypeToken<ApiResponse<List<InternetPlusBean>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_LIST, tag, params, new GcOkhttpCallback<>(type, callback));
    }


    //获取摄像头的历史视频
    public void getHistorageStream(Context tag, String cid, String beginTime,
                                   String endTime, ActionCallbackListener<List<InternetPlusBean>> callback) {
        HttpParams params = new HttpParams();
        params.put("cid", cid);
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        Type type = new TypeToken<ApiResponse<List<InternetPlusBean>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_HISTORY, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    //获取当前摄像头的视频流
    public void getVideoStream(Context tag, String cid, ActionCallbackListener<String> callback) {
        HttpParams params = new HttpParams();
        params.put("cid", cid);
        Type type = new TypeToken<ApiResponse<String>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_CURRENT, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    //删除收藏的摄像头
    public void deleteCollection(Context tag, String ids, ActionCallbackListener<String> callback) {
        HttpParams params = new HttpParams();
        params.put("ids", ids);
        Type type = new TypeToken<ApiResponse<String>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_COLLECTION_DEL, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    //查询收藏的摄像头列表
    public void serchCollectionList(Context tag, String userId, ActionCallbackListener<List<InternetPlusBean>> callback) {
        HttpParams params = new HttpParams();
        params.put("userId", userId);
        params.put("count", "20");
        Type type = new TypeToken<ApiResponse<List<InternetPlusBean>>>() {
        }.getType();
        HttpUtils.get(ApiConstant.INTERNET_VIDEO_COLLECTION_QUERY, tag, params, new GcOkhttpCallback<>(type, callback));
    }

    //添加摄像头收藏
    public void addCollection(Context tag, String cid, String userId, String userName,
                              String address, String longitude, String latitude, String pic, ActionCallbackListener<String> callback) {
        HttpParams params = new HttpParams();
        params.put("cid", cid);
        params.put("userId", userId);
        params.put("userName", userName);
        params.put("address", address);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("pic", pic);
        Type type = new TypeToken<ApiResponse<String>>() {
        }.getType();
        HttpUtils.post(ApiConstant.INTERNET_VIDEO_COLLECTION_ADD, tag, params, new GcOkhttpCallback<>(type, callback));
    }
}
