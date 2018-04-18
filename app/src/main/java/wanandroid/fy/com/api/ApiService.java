package wanandroid.fy.com.api;

import com.fy.baselibrary.retrofit.BeanModule;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.Path;
import wanandroid.fy.com.entity.BannerBean;
import wanandroid.fy.com.entity.Bookmark;
import wanandroid.fy.com.entity.ArticleBean;
import wanandroid.fy.com.entity.LoginBean;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import wanandroid.fy.com.entity.TreeBean;

/**
 * 通用的 api接口 </p>
 * Created by fangs on 2017/8/28.
 */
public interface ApiService {

    /**
     * 多图片上传
     *
     * @param token
     * @return
     */
    @Multipart
    @Headers({"url_name:user"})
    @POST("file/uploadImages")
//    @POST("http://192.168.100.123/hfs/")
    Observable<BeanModule<String>> uploadPostFile(@Part("token") RequestBody token,
                                                  @Part("type") RequestBody type,
                                                  @Part List<MultipartBody.Part> files);


    /**
     * 登录
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("user/login")
    Observable<BeanModule<LoginBean>> login(@FieldMap Map<String, Object> options);

    /**
     * 注册
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("user/register")
    Observable<BeanModule<LoginBean>> register(@FieldMap Map<String, Object> options);

    /**
     * 首页文章列表
     */
    @Headers({"url_name:user"})
    @GET("article/list/{id}/json")
    Observable<BeanModule<ArticleBean>> getArticleList(@Path("id") int pageNum);

    /**
     * 首页 banner
     */
    @Headers({"url_name:user"})
    @GET("banner/json")
    Observable<BeanModule<List<BannerBean>>> getBannerList();

    /**
     * 搜索热词
     */
    @Headers({"url_name:user"})
    @GET("hotkey/json")
    Observable<BeanModule<List<Bookmark>>> getHotkeyList();

    /**
     * 常用网站
     */
    @Headers({"url_name:user"})
    @GET("friend/json")
    Observable<BeanModule<List<Bookmark>>> getFriendList();


    /**
     * 体系数据
     *
     * @return
     */
    @Headers({"url_name:user"})
    @GET("tree/json")
    Observable<BeanModule<List<TreeBean>>> getTreeList();


    /**
     * 收藏站内文章
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("lg/collect/{id}/json")
    Observable<BeanModule<Object>> collectArticle(@Path("id") int articleId,
                                                  @Field("reason") String reason);


}
