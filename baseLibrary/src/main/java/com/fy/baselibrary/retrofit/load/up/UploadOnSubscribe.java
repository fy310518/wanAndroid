package com.fy.baselibrary.retrofit.load.up;

import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 文件上传 进度观察者 发射器（计算上传百分比）
 * Created by fangs on 2018/5/21.
 */
public class UploadOnSubscribe implements ObservableOnSubscribe<Double> {

    private ObservableEmitter<Double> mObservableEmitter;//进度观察者 发射器
    public long mSumLength = 0L;//总长度
    public AtomicLong uploaded = new AtomicLong();//已经上传 长度

    private double mPercent = 0;//当前上传进度 百分比

    public UploadOnSubscribe(long sumLength) {
        this.mSumLength = sumLength;
    }

    @Override
    public void subscribe(ObservableEmitter<Double> e) throws Exception {
        this.mObservableEmitter = e;
    }

    public void onRead(double percent) {
        if (mSumLength <= 0) {
            onProgress(0);
        } else {
            onProgress(percent);
        }
    }

    private void onProgress(double percent) {
        if (null == mObservableEmitter) return;
        if (percent == mPercent) return;

        mPercent = percent;
        if (percent >= 100) {
            percent = 100;
            mObservableEmitter.onNext(percent);
            mObservableEmitter.onComplete();
            return;
        }
        mObservableEmitter.onNext(percent);
    }
}
