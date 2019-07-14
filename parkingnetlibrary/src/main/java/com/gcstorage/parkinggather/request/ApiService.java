package com.gcstorage.parkinggather.request;

import android.util.ArrayMap;

import com.fy.baselibrary.retrofit.load.up.UpLoadFileType;
import com.gcstorage.parkinggather.bean.CalendarBean;
import com.gcstorage.parkinggather.bean.DailyRankingEntity;
import com.gcstorage.parkinggather.bean.LoginEntity;
import com.gcstorage.parkinggather.bean.ParkingInfo;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.bean.PersonEntity;
import com.gcstorage.parkinggather.bean.StatisticsEntity;
import com.gcstorage.parkinggather.bean.UploadFileEntity;
import com.gcstorage.parkinggather.bean.WeatherEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
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
    String HOST = "http://47.107.134.212";
    String PORT = ":13201/";
    String h5PORT = ":80/";
    String urlBase1 = "http://120.79.194.253:8768/";
    String urlBase2 = HOST + PORT + "/";

    /** 天门 警务通环境 */
//    String HOST = "http://20.51.3.43";
//    String PORT = ":8768/";
//    String h5PORT = ":8768/TMMHTEST/";
//    String urlBase1 = "http://20.51.3.43:8768/";
//    String urlBase2 = HOST + PORT + "/TMMHTEST/";



    /** 查车界面（H5） */
    String queryPerson = HOST + h5PORT + "queryPage/#/index";

    /** 多图上传接口 */
    String uploadFileMore = urlBase2 + "Falcon/2.0/tools/uploadfile_more";

    /** 登录 接口 */
    String login = urlBase2 + "Falcon/2.0/main/login";

    /** 获取 门户 用户信息 */
    String person = "https://47.107.134.212:8082" + "/totalLogin/getPersonInfo";

    /** 天气 */
    String weather = urlBase2 + "Micro/fireweather";


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST(login)
    Observable<BeanModule<List<LoginEntity>>> login(@FieldMap ArrayMap<String, String> options);

    /**
     * 获取 用户基本信息
     * @param account 身份证号
     * @return
     */
    @FormUrlEncoded
    @POST(person)
    Observable<BeanModule<PersonEntity>> getPersonInfo(@Field("account") String account);

    /**
     * 获取天气
     * @param pageSize
     * @return
     */
    @GET(weather)
    Observable<BeanModule<WeatherEntity>> getWeather(@Query("area") String pageSize);

    /**
     * 驻车采集信息 (统计图)
     * params.put("action","parkingcollect");
     * params.put("alarm",GlobalUserInfo.getAlarm(context));
     * params.put("token",GlobalUserInfo.getToken(context));
     */
    @GET("/query/parking/serchWeekDate")
    Observable<BeanModule<StatisticsEntity>> parkingCollect(@Query("userId") String userId);


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


    /**
     * 个人排行榜
     * @return Observable
     */
    @GET("/query/parking/dailyRanking")
    Observable<BeanModule<List<DailyRankingEntity>>> dailyRanking();

    /**
     * 部门排行榜
     * @return Observable
     */
    @GET("/query/parking/dailyRankOrg")
    Observable<BeanModule<List<DailyRankingEntity>>> dailyRankOrg();

    /**
     * 根据月份查询当月每天驻车数据
     * @return Observable
     */
    @GET("/query/parking/serchMonCount")
    Observable<BeanModule<CalendarBean>> queryMonthParkingData(@Query("userId") String userId,
                                                               @Query("data") String data);

    /**
     * 根据日期查询当天所有采集车辆数据
     * @param userId
     * @param data
     * @return Observable
     */
    @GET("/query/parking/serchParkingBydata")
    Observable<BeanModule<ParkingInfo>> serchParkingBydata(@Query("userId") String userId,
                                                           @Query("data") String data);



}
