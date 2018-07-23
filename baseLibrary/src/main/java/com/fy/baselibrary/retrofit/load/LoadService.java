package com.fy.baselibrary.retrofit.load;

import com.fy.baselibrary.retrofit.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 通用的 上传文件 下载文件 api
 * Created by fangs on 2018/6/1.
 */
public interface LoadService {

    /**
     * 多图片上传
     * @param token
     * @return
     */
    @Multipart
    @Headers({"url_name:user"})
    @POST("http://192.168.100.123/hfs/")
    Observable<BaseBean<String>> uploadPostFile(@Part("token") RequestBody token,
                                                  @Part("type") RequestBody type,
                                                  @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("http://192.168.100.123/hfs/")
    Observable<BaseBean<Object>> uploadFile1(@Part List<MultipartBody.Part> files);

    /**
     * 多图片上传
     * @return
     */
    @Multipart
    @Headers({"url_name:user"})
    @POST("http://192.168.100.123/hfs/")
    Observable<Object> uploadFile2(@Part List<MultipartBody.Part> files);

    /**
     * 文件下载
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);


    /**
     * 断点下载
     * @param downParam 下载参数，传下载区间使用
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String downParam, @Url String url);

}
