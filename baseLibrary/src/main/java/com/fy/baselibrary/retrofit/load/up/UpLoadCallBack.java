package com.fy.baselibrary.retrofit.load.up;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义Subscribe (增强 NetCallBAck)
 * Created by fangs on 2018/5/21.
 */
public abstract class UpLoadCallBack extends NetCallBack {

    public UpLoadCallBack() {
    }

    public UpLoadCallBack(@NonNull final String url) {
        this.url = url;
    }

    public UpLoadCallBack(IProgressDialog pDialog) {
        super(pDialog);
    }

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            onProgress(o + "");
        } else {
            super.onNext(o);
        }
    }

    public void setmSumLength(long mSumLength) {
        this.mSumLength = mSumLength;

        ACache mCache = ACache.get(BaseApp.getAppCtx());
        //从缓存中获取 已经下载的总进度
        loaded.addAndGet(mCache.getAsLong(url + Constant.DownTask));
    }

    /**
     * 下载 url
     */
    private String url;
    public long mSumLength = 0L;//总长度
    public AtomicLong loaded = new AtomicLong();//已经下载的 总长度

    private double mPercent = 0;//进度百分比 数

    public void onRead(long read) {
        loaded.addAndGet(read);

        if (mSumLength <= 0) {
            onPercent(-1);
        } else {
            onPercent(100d * loaded.get() / mSumLength);
        }
    }

    /**
     * 计算 上传、下载 进度百分比
     *
     * @param percent
     */
    private void onPercent(double percent) {
        if (percent == mPercent) return;

        if (percent >= 100d) {
            percent = 100d;
            onProgress(100 + "");
            cachePercent(percent);
            onComplete();
            return;
        }

        onProgress(TransfmtUtils.doubleToKeepTwoDecimalPlaces(percent));
        cachePercent(percent);
    }

    /**
     * 缓存进度百分比
     * @param percent
     */
    private void cachePercent(double percent) {
        //缓存 断点续传 进度百分比
        if (!TextUtils.isEmpty(url)) {
            ACache mCache = ACache.get(BaseApp.getAppCtx());
            mCache.put(url + Constant.DownPercent, percent);
            mCache.put(url + Constant.DownTask, loaded.get());
        }
    }


    /**
     * 上传、下载 进度回调方法
     *
     * @param percent
     */
    protected abstract void onProgress(String percent);
}
