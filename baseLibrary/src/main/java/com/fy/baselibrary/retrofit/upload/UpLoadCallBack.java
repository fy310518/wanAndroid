package com.fy.baselibrary.retrofit.upload;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.utils.cache.ACache;

/**
 * 自定义Subscribe (增强 NetCallBAck)
 * Created by fangs on 2018/5/21.
 */
public abstract class UpLoadCallBack extends NetCallBack {

    public UpLoadCallBack() {
    }

    public UpLoadCallBack(@NonNull final String url) {
        this.url = url;

        //从缓存中获取 已经下载的总进度
        ACache mCache = ACache.get(BaseApp.getAppCtx());
        String strLoaded = mCache.getAsString(url + "loaded");
        if (!TextUtils.isEmpty(strLoaded)) {
            loaded = Long.parseLong(strLoaded);
        }
    }

    public UpLoadCallBack(IProgressDialog pDialog) {
        super(pDialog);
    }

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            onProgress((Integer) o);
        } else {
            super.onNext(o);
        }
    }

    public void setmSumLength(long mSumLength) {
        this.mSumLength = mSumLength;
    }

    /**
     * 下载 url
     */
    private String url;
    private long mSumLength = 0L;//总长度
    public long loaded = 0L;//已经下载的 总长度

    private int mPercent = 0;//进度百分比 数

    public void onRead(long read) {
        loaded += read;
        if (mSumLength <= 0) {
            onPercent(-1);
        } else {
            onPercent((int) (100 * loaded / mSumLength));
        }
    }

    /**
     * 计算 上传、下载 进度百分比
     *
     * @param percent
     */
    private void onPercent(int percent) {
        if (percent == mPercent) return;

        if (percent >= 100) {
            percent = 100;
            onProgress(percent);
            cachePercent(percent);
            onComplete();
            return;
        }

        onProgress(percent);
        cachePercent(percent);
    }

    /**
     * 缓存进度百分比
     * @param percent
     */
    private void cachePercent(int percent) {
        //缓存 断点续传 进度百分比
        if (!TextUtils.isEmpty(url)) {
            ACache mCache = ACache.get(BaseApp.getAppCtx());
            mCache.put(url + "percent", percent + "");
            mCache.put(url + "loaded", loaded + "");
        }
    }


    /**
     * 上传、下载 进度回调方法
     *
     * @param percent
     */
    protected abstract void onProgress(Integer percent);
}
