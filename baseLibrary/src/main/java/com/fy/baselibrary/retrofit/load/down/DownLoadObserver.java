package com.fy.baselibrary.retrofit.load.down;

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
     * 下载开关
     */
    private boolean downSwitch = true;
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
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof SocketException) {
            int status = -1;
            if (e.getMessage().equals(DownInfo.STATUS_CANCEL + "")) {
                status = DownInfo.STATUS_CANCEL;
            } else if (e.getMessage().equals(DownInfo.STATUS_PAUSED + "")) {
                status = DownInfo.STATUS_PAUSED;
            } else {
                DownManager.getInstentce().removeTask(downInfo);
            }

            if (status != -1) {
                downInfo.setStateInte(status);
                downSwitch = false;
                //切断当前的订阅
                if (null != disposed && !disposed.isDisposed()) disposed.dispose();
                if (null != loadListener) loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(), downInfo.getPercent());
            }

            DownManager.getInstentce().saveDownInfo(downInfo);
        }
    }

    @Override
    public void onComplete() {
        if (null != loadListener) loadListener.onComplete();
        downInfo.setStateInte(DownInfo.STATUS_COMPLETE);

        DownManager.getInstentce().removeTask(downInfo);

        DownManager.getInstentce().saveDownInfo(downInfo);
        DownManager.getInstentce().runDownTask();
    }


    /**
     * 计算下载 进度百分比
     *
     * @param read
     */
    public void onRead(long read) {
        if (!downSwitch) return;

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


        downInfo.setStateInte(DownInfo.STATUS_DOWNLOADING);
        if (percent >= 100d) {
            percent = 100d;
            downInfo.setPercent(percent);
            if (null != loadListener)
                loadListener.onProgress(downInfo.getReadLength().get(),
                        downInfo.getCountLength(),
                        downInfo.getPercent());
            onComplete();
            return;
        }


        downInfo.setPercent(percent);
        if (null != loadListener)
            loadListener.onProgress(downInfo.getReadLength().get(),
                downInfo.getCountLength(),
                downInfo.getPercent());
    }

    public void setDownSwitch(boolean downSwitch) {
        this.downSwitch = downSwitch;
    }
}
