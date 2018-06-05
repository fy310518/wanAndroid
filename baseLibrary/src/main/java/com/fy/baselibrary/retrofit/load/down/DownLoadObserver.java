package com.fy.baselibrary.retrofit.load.down;

import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.TransfmtUtils;

import java.net.SocketException;

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
     * 单个下载任务 监听器
     */
    private DownLoadListener loadListener;

    private DownLoadListener.DownLoadCall loadCall;

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
        if (e instanceof SocketException) {
            if (e.getMessage().equals("Socket closed")) {
                if (downInfo.getStateInte() != DownInfo.CANCEL) {
                    downInfo.setStateInte(DownInfo.PAUSE);
                    DownManager.getInstentce().saveDownInfo(downInfo);
                    L.e("清除下载错误的任务", downInfo.getUrl() + "--->");
                }
                DownManager.getInstentce().removeTask(downInfo);
            }
        }
    }

    @Override
    public void onComplete() {
        if (null != loadListener) loadListener.onComplete();
        downInfo.setStateInte(DownInfo.FINISH);

        DownManager.getInstentce().saveDownInfo(downInfo);
        DownManager.getInstentce().runDownTask();
        L.e("清除下载完成的任务", downInfo.getUrl() + "--->");
        DownManager.getInstentce().removeTask(downInfo);
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
            downInfo.setPercent(percent);
            if (null != loadListener) loadListener.onProgress(100 + "");
            if (null != loadCall) loadCall.onProgress(downInfo);
            onComplete();
            return;
        }

        downInfo.setPercent(percent);
        if (null != loadListener) loadListener.onProgress(TransfmtUtils.doubleToKeepTwoDecimalPlaces(percent));
        if (null != loadCall) loadCall.onProgress(downInfo);
//     缓存进度百分比
        downInfo.setPercent(percent);
    }


    public Disposable getDisposed() {
        return disposed;
    }

    public void setLoadCall(DownLoadListener.DownLoadCall loadCall) {
        this.loadCall = loadCall;
    }
}
