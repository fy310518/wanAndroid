package com.fy.baselibrary.retrofit.upload;

import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;

/**
 * 自定义Subscribe (增强 NetCallBAck)
 * Created by fangs on 2018/5/21.
 */
public abstract class UpLoadCallBack extends NetCallBack {

    public UpLoadCallBack() {}

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

    private long mSumLength = 0l;//总长度
    public long loaded = 0l;//进度

    private int mPercent = 0;

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
     * @param percent
     */
    private void onPercent(int percent) {
        if (percent == mPercent) return;

        if (percent >= 100) {
            percent = 100;
            onProgress(percent);
//            onComplete();
            return;
        }

        onProgress(percent);
    }

    /**
     * 上传、下载 进度回调方法
     *
     * @param percent
     */
    protected abstract void onProgress(Integer percent);
}
