package com.fy.baselibrary.retrofit.load.up;

import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.TransfmtUtils;

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

    private double mPercent = 0;//已经上传进度 百分比

    public UploadOnSubscribe() {}

    @Override
    public void subscribe(ObservableEmitter<Double> e) throws Exception {
        this.mObservableEmitter = e;
    }

    public void onRead(long read) {
        uploaded.addAndGet(read);

        if (mSumLength <= 0) {
            onProgress(0);
        } else {
            onProgress(100d * uploaded.get() / mSumLength);
        }
    }

    private void onProgress(double percent) {
        if (null == mObservableEmitter) return;
        if (percent == mPercent) return;
        L.e("进度E", uploaded.get() + "--/--" + mSumLength + "-->" + Thread.currentThread().getName());

        mPercent = percent;
        if (percent >= 100) {
            percent = 100;
            mObservableEmitter.onNext(percent);
            mObservableEmitter.onComplete();
            return;
        }
        mObservableEmitter.onNext(percent);
    }

    public void setmSumLength(long mSumLength) {
        this.mSumLength = mSumLength;
    }
}
