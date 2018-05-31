package com.fy.baselibrary.retrofit.load.up;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 文件上传 进度观察者
 * Created by fangs on 2018/5/21.
 */
public class UploadOnSubscribe implements ObservableOnSubscribe<Integer> {

    private ObservableEmitter<Integer> mObservableEmitter;
    private long mSumLength = 0l;
    private long uploaded = 0l;

    private int mPercent = 0;

    public UploadOnSubscribe(long sumLength) {
        this.mSumLength = sumLength;
    }

    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
        this.mObservableEmitter = e;
    }

    public void onRead(long read) {
        uploaded += read;
        if (mSumLength <= 0) {
            onProgress(-1);
        } else {
            onProgress((int) (100 * uploaded / mSumLength));
        }
    }

    private void onProgress(int percent) {
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
