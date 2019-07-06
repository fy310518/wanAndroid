package com.gcstorage.parkinggather.request;

import android.util.ArrayMap;

import com.fy.baselibrary.retrofit.load.up.UpLoadFileType;
import com.gcstorage.parkinggather.bean.LoginEntity;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.bean.UploadFileEntity;
import com.gcstorage.parkinggather.bean.WeatherEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
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

    /** 接口地址 */
    String urlBase2 = "http://47.107.134.212:13201/";

    /** 多图上传接口 */
    String uploadFileMore = urlBase2 + "Falcon/2.0/tools/uploadfile_more";
    /** 登录 接口 */
    String login = urlBase2 + "Falcon/2.0/main/login";
    /** 天气 */
    String weather = urlBase2 + "Micro/fireweather";


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(login)
    Observable<BeanModule<List<LoginEntity>>> login(@FieldMap ArrayMap<String, String> options);

    /**
     * 获取天气
     * @param pageSize
     * @return
     */
    @GET(weather)
    Observable<BeanModule<WeatherEntity>> getWeather(@Query("area") String pageSize);


    /**
     * 查询驻车列表
     * @param pageSize 每页个数  默认10个
     * @param pageNum  页数   默认第一页
     * @return
     */
    @GET("/query/car/serchParkingInfoList")
    Observable<BeanModule<ParkingInfoEntity>> getParkingList(@Query("pageSize") String pageSize, @Query("pageNum") String pageNum);


    /**
     * 通用 图文上传 (支持多图片) （参数注解：@Body；参数类型：MultipartBody）
     * params.put("uploadFile", "fileName");
     * params.put("filePathList", files);
     * params.put("UploadOnSubscribe", new UploadOnSubscribe());
     *
     * 注意：其它 文本参数 value 必须是 字符串类型（如下 token 参数）
     * params.put("token", "123");
     */
    @UpLoadFileType
    @POST(uploadFileMore)
    Observable<BeanModule<List<UploadFileEntity>>> uploadFile(@Body ArrayMap<String, Object> params);

    /**
     * 保存驻车信息
     * 接口文档地址：http://www.doclever.cn/controller/share/share.html#5c501d3c3dce46264b2580f7
     */
    @FormUrlEncoded
    @POST("/query/car/saveParkingInfo")
    Observable<BeanModule<Object>> saveParkingInfo(@FieldMap ArrayMap<String, String> options);



}
