package com.gcstorage.parkinggather.request;

import com.gcstorage.parkinggather.main.ParkingInfoEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * api接口 </p>
 * Created by fangs on 2017/8/28.
 */
public interface ApiService {

    /**
     * wanAndroid 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<BeanModule<String>> login(@FieldMap Map<String, Object> options);

    /**
     * 查询驻车列表
     * @param pageSize 每页个数  默认10个
     * @param pageNum  页数   默认第一页
     * @return
     */
    @GET("/query/car/serchParkingInfoList")
    Observable<BeanModule<ParkingInfoEntity>> getParkingList(@Query("pageSize") String pageSize, @Query("pageNum") String pageNum);


}
