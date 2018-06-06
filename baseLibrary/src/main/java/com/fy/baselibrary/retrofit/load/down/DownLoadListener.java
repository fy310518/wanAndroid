package com.fy.baselibrary.retrofit.load.down;

/**
 * 文件下载过程 监听接口（包含：开始--》下载中--》暂停--》取消--》错误--》完成；等状态回调）
 * Created by fangs on 2018/6/1.
 */
public interface DownLoadListener {

    /**
     * 暂停下载
     */
    void onPuase();

    /**
     * 取消下载，清除对应的下载缓存数据（即：下次下载时候重新下载）
     */
    void onCancel();

    /**
     * 下载 进度回调方法
     * @param finished  文件的下载长度
     * @param total     文件总长度
     * @param progress  下载进度百分比 （finished / total * 100）
     */
    void onProgress(long finished, long total, double progress);

    /**
     * download complete
     */
    void onComplete();

    /**
     * download fail or exception callback
     * @param e
     */
    void onFailed(Exception e);
}
