package com.fy.baselibrary.retrofit.load.down;

import android.text.TextUtils;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.cache.ACache;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 文件下载 观察者
 * Created by fangs on 2018/6/1.
 */
public class DownLoadObserver<T> implements Observer<T> {

    private Disposable disposed;
    /**
     * 下载数据
     */
    private DownInfo downInfo;
    /**
     * 下载监听
     */
    private DownLoadListener loadListener;

    public DownLoadObserver(DownInfo downInfo, DownLoadListener loadListener) {
        this.downInfo = downInfo;
        this.loadListener = loadListener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposed = d;
    }

    @Override
    public void onNext(T t) {
        downInfo.setStateInte(DownInfo.DOWN);
    }

    @Override
    public void onError(Throwable e) {
        downInfo.setStateInte(DownInfo.ERROR);
    }

    @Override
    public void onComplete() {
        downInfo.setStateInte(DownInfo.FINISH);
        DownManager.getInstentce().saveDownInfo();
        loadListener.onComplete();
    }

    /**
     * 计算下载 进度百分比
     *
     * @param read
     */
    public void onRead(long read) {
        downInfo.getReadLength().addAndGet(read);

        if (downInfo.getCountLength() <= 0) {
            onPercent(-1);
        } else {
            onPercent(100d * downInfo.getReadLength().get() / downInfo.getCountLength());
        }
    }

    /**
     * 计算下载 进度百分比
     *
     * @param percent
     */
    private void onPercent(double percent) {
        if (percent == downInfo.getPercent()) return;

        if (percent >= 100d) {
            percent = 100d;
            loadListener.onProgress(100 + "");
            downInfo.setPercent(percent);
            onComplete();
            return;
        }

        loadListener.onProgress(TransfmtUtils.doubleToKeepTwoDecimalPlaces(percent));
//     缓存进度百分比
        downInfo.setPercent(percent);
    }


    public Disposable getDisposed() {
        return disposed;
    }
}
