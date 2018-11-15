package com.fy.baselibrary.retrofit.load;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fy.baselibrary.ioc.ConfigUtils;
import com.fy.baselibrary.retrofit.IProgressDialog;
import com.fy.baselibrary.retrofit.RequestBaseObserver;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义文件上传、下载 观察者 (增强 RequestBaseObserver)
 * Created by fangs on 2018/5/21.
 */
public abstract class LoadCallBack<T> extends RequestBaseObserver<T> {

    public long mSumLength = 0L;//总长度
    public AtomicLong uploaded = new AtomicLong();//已经上传 长度
    private double mPercent = 0;//已经上传进度 百分比

    public LoadCallBack() {}

    public LoadCallBack(@NonNull final String url) {
        this.url = url;
    }

    public LoadCallBack(IProgressDialog pDialog) {
        super(pDialog);
    }

    @Override
    public void onNext(T t) {
        if (t instanceof Double) {
            String percent = TransfmtUtils.doubleToKeepTwoDecimalPlaces(((Double) t).doubleValue());
            onProgress(percent);
        } else {
            super.onNext(t);
        }
    }

    public void setmSumLength(long mSumLength) {
        this.mSumLength = mSumLength;
    }


    /**
     * 下载
     */
    private String url;
    public AtomicLong loaded = new AtomicLong();//已经下载的 总长度

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
            ACache mCache = ACache.get(ConfigUtils.getAppCtx());
            mCache.put(url + Constant.DownPercent, percent);
            mCache.put(url + Constant.DownTask, loaded.get());
        }
    }
}
