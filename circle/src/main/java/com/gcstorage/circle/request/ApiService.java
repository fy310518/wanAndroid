package com.gcstorage.circle.request;


import android.util.ArrayMap;

import com.fy.baselibrary.retrofit.load.up.UpLoadFileType;
import com.gcstorage.circle.bean.CommentListBean;
import com.gcstorage.circle.bean.LyCircleListBean;
import com.gcstorage.circle.bean.LyLoginBean;
import com.gcstorage.circle.bean.UploadBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * api接口 </p>
 * Created by fangs on 2017/8/28.
 */
public interface ApiService {


    /**
     * 猎鹰 登录
     */
    @FormUrlEncoded
    @POST("http://47.107.134.212:13201/Falcon/2.0/main/login")
    Observable<BeanModule<List<LyLoginBean>>> lyLogin(@FieldMap ArrayMap<String, Object> options);

    /**
     * 获取帖子列表的接口
     */
    @GET("http://47.107.134.212:13202/Falcon/1.1/post/postlist1206")
    Observable<BeanModule<List<LyCircleListBean>>> lypostlist(@QueryMap ArrayMap<String, Object> options);


    /**
     * 帖子列表 积分打赏
     */
    @FormUrlEncoded
    @POST("http://47.107.134.212:13202/Falcon/1.1/post/give_reward")
    Observable<BeanModule<String>> integralGiveReward(@FieldMap ArrayMap<String, Object> options);


    /**
     * 点赞 与 取消点赞
     */
    @FormUrlEncoded
    @POST("http://47.107.134.212:13202/Falcon/1.1/praise/setpraise_new")
    Observable<BeanModule<List<LyCircleListBean.PraiseListBean>>> setpraiseNew(@FieldMap ArrayMap<String, Object> options);


    /**
     * 帖子评论 上传图片
     * 多图片上传 之 图文上传
     * @return
     */
    @Multipart
    @POST("http://47.107.134.212:13201/Falcon/2.0/tools/uploadfile")
    Observable<BeanModule<UploadBean>> uploadPostFile(@Part MultipartBody.Part file);

    @UpLoadFileType
    @POST("http://47.107.134.212:13201/Falcon/2.0/tools/uploadfile_more")
    Observable<BeanModule<List<UploadBean>>> uploadFile(@Body ArrayMap<String, Object> params);

    /**
     * 帖子评论
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("http://47.107.134.212:13202/Falcon/1.1/comment/setcomment_new")
    Observable<BeanModule<ArrayList<CommentListBean>>> commentNew(@FieldMap ArrayMap<String, Object> params);


}
