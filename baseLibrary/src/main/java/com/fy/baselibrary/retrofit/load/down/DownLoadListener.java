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
     * 上传、下载 进度回调方法
     *
     * @param percent
     */
    void onProgress(String percent);

    /**
     * 完成下载
     */
    void onComplete();
}
