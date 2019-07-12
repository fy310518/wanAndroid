package com.gcstorage.framework.update;

import java.io.File;

/**
 * Created by zjs on 2018/12/26.
 */

public interface OnUpdateListener {
    /**
     * 下载时候的进度监听
     * @param progress 下载进度
     */
    void onProgress(float progress);

    /**
     * 下载完成监听
     */
    void onFinish(File downloadFile);

    /**
     * 下载发送错误监听
     */
    void onError();
}
