package com.gcstorage.scanface.request;


import android.util.ArrayMap;

import com.gcstorage.scanface.Constants;
import com.gcstorage.scanface.bean.LgCodeBean;
import com.gcstorage.scanface.bean.LgTokenBean;
import com.gcstorage.scanface.bean.LgUserInfoBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * api接口 </p>
 * Created by fangs on 2017/8/28.
 */
public interface ApiService {

    /**
     * 新登录流程的获取code
     * @param options
     * @return
     */
    @GET(Constants.authUrl + "/connect/getcode")
    Observable<BeanModule<LgCodeBean>> getcode(@QueryMap ArrayMap<String, Object> options);

    /**
     * 通过code获取token
     * @param options
     * @return
     */
    @GET(Constants.authUrl + "/connect/access_token")
    Observable<BeanModule<LgTokenBean>> getToken(@QueryMap ArrayMap<String, Object> options);

    /**
     * 通过token拿取个人信息
     * @param options
     * @return
     */
    @GET(Constants.authUrl + "/connect/userinfo")
    Observable<BeanModule<LgUserInfoBean>> getInfo(@QueryMap ArrayMap<String, Object> options);

    /**
     * 获取 用户头像 bitmap
     * @param headImgUrl
     * @return
     */
    @GET()
    Observable<ResponseBody> getBitmap(@Url String headImgUrl);


//    /**
//     * 使用 猎鹰 的图片上传接口 帖子评论 上传图片
//     * 多图片上传 之 图文上传
//     * @return
//     */
//    @UpLoadFileType
//    @POST(Constant.HOST + "tools/uploadfile_more")
//    Observable<BeanModule<List<UploadBean>>> uploadFile(@Body ArrayMap<String, Object> params);
//    /**
//     * 战友圈 帖子评论
//     * @param params
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(Constant.zyq_HOST + "comment/setcomment_new")
//    Observable<BeanModule<ArrayList<CommentListBean>>> commentNew(@FieldMap ArrayMap<String, Object> params);
//


}
