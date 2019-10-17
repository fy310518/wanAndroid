package com.fy.baselibrary.retrofit.load.down;

/**
 * DESCRIPTION：文件下载 监听接口
 * Created by fangs on 2019/10/16 17:56.
 */
public interface DownLoadListener<T> {

    /**
     * 请求成功 回调
     */
    void onSuccess(T t);

    /**
     * 更新activity 界面（多状态视图）
     * 可根据flag 判断请求失败
     * @param flag 请求状态flag
     */
    void updateLayout(int flag);

    /**
     * 上传、下载 需重写此方法，更新进度
     * @param percent 进度百分比 数
     */
    void onProgress(String percent);
}
