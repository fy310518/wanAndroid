package com.fy.baselibrary.utils.imgload;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.R;
import com.fy.baselibrary.application.BaseApp;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载工具类(目前使用 Glide)
 *
 * Created by fangs on 2017/5/5.
 */
public class ImgLoadUtils {

    private ImgLoadUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 预加载 （把指定URL地址的图片 的原始尺寸保存到缓存中）
     * @param url
     */
    public static void preloadImg(String url){
        Context context = BaseApp.getAppCtx();
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .preload();
    }

    /**
     * 加载指定URL的图片(从缓存中取得)
     * @param url
     * @param imageView
     */
    public static void loadImg(String url, ImageView imageView){
        Context context = BaseApp.getAppCtx();
        Glide.with(context)
                .load(url)
                .error(R.mipmap.img_load_error)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 加载指定URL的图片
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .fallback(R.mipmap.img_load_default)
                .error(R.mipmap.img_load_error)
                .placeholder(R.mipmap.img_loading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存最后一次那个image
                .into(imageView);
    }

    /**
     * 加载指定URL的图片 不要缓存
     * @param url
     * @param imageView
     */
    public static void loadImages(String url, ImageView imageView) {
        Context context = BaseApp.getAppCtx();
        Glide.with(context)
                .load(url)
                .fallback(R.mipmap.img_load_default)
                .error(R.mipmap.img_load_error)
                .placeholder(R.mipmap.img_loading)
                .into(imageView);
    }

    /**
     * 加载圆角网络图片
     * @param context
     * @param url
     * @param imageView
     * @param radius
     */
    public static void loadCircularBead(Context context, String url, ImageView imageView, int radius) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, radius))
                .crossFade(500) //标准的淡入淡出动画
                .into(imageView);
    }


    /**
     * 加载指定URL的图片（不做任何缓存）圆形显示
     * @param url
     * @param imageView
     */
    public static void loadCircleImg(String url, ImageView imageView) {
        Context context = BaseApp.getAppCtx();
        Glide.with(context).load(url)
                .fallback(R.mipmap.img_load_error)
                .placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error)
                .transform(new GlideCircleTransform(context))
                .skipMemoryCache(true)//不自动缓存到内存
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 加载指定URL的图片（不做任何缓存）圆角显示
     * @param url
     * @param imageView
     */
    public static void loadRound(String url, ImageView imageView){
        Context context = BaseApp.getAppCtx();
        Glide.with(context).load(url)
                .fallback(R.mipmap.img_load_default)
                .placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error)
                .transform(new GlideRoundTransform(context))
                .skipMemoryCache(true)//不自动缓存到内存
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void loadRoundImg(int url, ImageView imageView){
        Context context = BaseApp.getAppCtx();
        Glide.with(context).load(url)
                .fallback(R.mipmap.img_load_default)
                .placeholder(R.mipmap.img_loading)
                .error(R.mipmap.img_load_error)
                .skipMemoryCache(true)//不自动缓存到内存
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 加载指定URL的图片 显示加载进度
     * 原图缓存到磁盘，
     * @param context
     * @param url
     * @param imgTarget
     */
    public static void loadImage(Context context, String url, GlideDrawableImageViewTarget imgTarget) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//原图缓存
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imgTarget);
    }

    /**
     * 异步获取 glide 缓存在磁盘的图片
     * </p>
     * Glide提供了一个downloadOnly() 接口来获取缓存的图片文件，
     * 但是前提必须要设置diskCacheStrategy方法的缓存策略为DiskCacheStrategy.ALL或者 DiskCacheStrategy.SOURCE，
     * 还有downloadOnly()方法需要在线程里进行
     * @param context
     * @param url
     * @param consumer
     */
    public static void getImgCachePath(Context context, String url, Consumer<File> consumer) {
        Observable.just(url)
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String s) throws Exception {
                        File cacheImg = null;
                        try {
                            cacheImg = Glide.with(context)
                                    .load(url)
                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                    .get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return cacheImg;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }


}
