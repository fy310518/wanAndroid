package com.fy.wanandroid.request;

import com.fy.wanandroid.entity.ArticleBean;
import com.fy.wanandroid.entity.BannerBean;
import com.fy.wanandroid.entity.Bookmark;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.entity.TreeBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * api接口 </p>
 * Created by fangs on 2017/8/28.
 */
public interface ApiService {

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
     * 知识体系下的文章
     *
     * @return
     */
    @Headers({"url_name:user"})
    @GET("article/list/{id}/json")
    Observable<BeanModule<ArticleBean>> getTreeArticle(@Path("id") int pageNum,
                                                       @Query("cid") int cid);


    /**
     * 收藏文章列表
     *
     * @return
     */
    @Headers({"url_name:user"})
    @GET("lg/collect/list/{id}/json")
    Observable<BeanModule<ArticleBean>> getCollectList(@Path("id") int pageNum);

    /**
     * 收藏站内文章
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("lg/collect/{id}/json")
    Observable<BeanModule<Object>> collectArticle(@Path("id") int articleId,
                                                  @Field("reason") String reason);

    /**
     * 站内文章  取消收藏 (文章列表)
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BeanModule<Object>> uncollectArticle(@Path("id") int articleId,
                                                    @Field("reason") String reason);

    /**
     * 站内文章 取消收藏 [我的收藏页面（该页面包含自己录入的内容）]
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("lg/uncollect/{id}/json")
    Observable<BeanModule<Object>> unMyCollectArticle(@Path("id") int articleId,
                                                      @Field("originId") int originId);


    /**
     * 搜索
     */
    @FormUrlEncoded
    @Headers({"url_name:user"})
    @POST("article/query/{id}/json")
    Observable<BeanModule<ArticleBean>> query(@Path("id") int articleId,
                                              @Field("k") String queryKey);

    /**
     * 小说类型下的 书籍列表
     *
     * @param paramString1 三种类型：hot（热门）；new(新书)；reputation（好评）
     * @param paramString2 小说类型 关键字(如：玄幻，武侠...)
     * @param paramInt     分页加载 页码 （从 1 开始）
     * @return
     */
    @GET("http://120.55.57.236/api/books")
    Observable<BeanModule<Object>> books(@Query("type") String paramString1,
                                         @Query("major") String paramString2,
                                         @Query("page") int paramInt);

    /**
     * 小说 简介（描述、标签）
     *
     * @param paramString _id：小说id
     * @return
     */
    @GET("http://120.55.57.236/api/books/{bookId}")
    Observable<BeanModule<Object>> bookInfo(@Path("bookId") String paramString);


    /**
     * @param paramString 标签（如：奇幻、无限流...）
     * @param paramInt    分页加载 页码 （从 1 开始）
     * @return
     */
    @GET("http://120.55.57.236/api/books/tag")
    Observable<BeanModule<List<Object>>> booksByTag(@Query("bookTag") String paramString,
                                                    @Query("page") int paramInt);

    /**
     * 获取对应章节 内容
     *
     * @param link
     * @return
     */
    @GET("http://chapterup.zhuishushenqi.com/chapter/{link}")
    Observable<BeanModule<List<Object>>> booksByTag(@Path("link") String link);

}
