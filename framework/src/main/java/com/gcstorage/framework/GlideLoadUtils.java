package com.gcstorage.framework;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gcstorage.framework.utils.MeasureUtil;

/**
 * 使用Glide进行的图片加载类
 * Created by zjs on 2018/9/11.
 */

public class GlideLoadUtils {

    private volatile static GlideLoadUtils instance;

    private GlideLoadUtils() {
    }

    public static GlideLoadUtils getInstance() {
        if (instance == null) {
            synchronized (GlideLoadUtils.class) {
                if (instance == null) {
                    instance = new GlideLoadUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 加载网络图片
     * 图片拥有圆角并且图片展示位CenterCrop
     * @param context 上下文
     * @param target 目标控件
     * @param url 网络地址
     * @param roundRadius 圆角大小
     */
    public void loadImageByUrlWithRoundCenterCrop(Context context, ImageView target, String url, int roundRadius) {
        RequestOptions options = RequestOptions.errorOf(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .transforms(new CenterCrop(),new RoundedCorners(MeasureUtil.dp2px(context,roundRadius)));
        Glide.with(context).load(url).apply(options).into(target);
    }

    /**
     * 加载网络图片
     * 图片拥有圆角并且图片展示位CenterCrop
     * @param context 上下文
     * @param target 目标控件
     * @param url 网络地址
     * @param roundRadius 圆角大小
     * @param errorImg 错误显示图
     * @param placeHolderImg 占位图
     */
    public void loadImageByUrlWithRoundCenterCrop(Context context, ImageView target, String url, int roundRadius, @DrawableRes int errorImg, @DrawableRes int placeHolderImg) {
        RequestOptions options = RequestOptions.errorOf(errorImg)
                .placeholder(placeHolderImg)
                .transforms(new CenterCrop(),new RoundedCorners(MeasureUtil.dp2px(context,roundRadius)));
        Glide.with(context).load(url).apply(options).into(target);
    }

    /**
     * 加载网络图片
     * 图片展示位CenterCrop
     * @param context 上下文
     * @param target 目标控件
     * @param url 网络地址
     */
    public void loadImageByUrlWithCenterCrop(Context context, ImageView target, String url){
        RequestOptions options = RequestOptions.errorOf(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop();
        Glide.with(context).load(url).apply(options).into(target);
    }

    /**
     * 加载网络图片
     * 图片展示位CenterCrop
     * @param context 上下文
     * @param target 目标控件
     * @param url 网络地址
     * @param errorImg 错误显示图
     * @param placeHolderImg 占位图
     */
    public static void loadImageByUrlWithCenterCrop(Context context, ImageView target, String url, @DrawableRes int errorImg, @DrawableRes int placeHolderImg){
        RequestOptions options = RequestOptions.errorOf(errorImg)
                .placeholder(placeHolderImg)
                .centerCrop();
        Glide.with(context).load(url).apply(options).into(target);
    }

    /**
     * 加载网络图片，不做任何处理
     * @param context 上下文
     * @param target 目标控件
     * @param url 网络地址
     */
    public void loadImageByUrl(Context context, ImageView target, String url){
        RequestOptions options = RequestOptions.errorOf(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(context).load(url).apply(options).into(target);
    }

    /**
     * 加载图片,centercrop
     * @param context 上下文
     * @param url 图片对象可以是uri,string,int,
     * @param target 目标控件
     * @param defaultResId 默认占位图片id
     * @param errotResId 错误图片id
     */
    public static void displayCenterCrop(Context context, Object url, ImageView target, int defaultResId, int errotResId){
        RequestOptions options = RequestOptions
                .centerCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errotResId)
                .placeholder(defaultResId);
        Glide.with(context).load(url).apply(options).into(target);
    }


}
