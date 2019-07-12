package com.gcstorage.framework.net;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

/**
 * Created by zjs on 2018/9/7.
 */

public class SimpleDownloadListener extends DownloadListener {
    public SimpleDownloadListener(Object tag) {
        super(tag);
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {

    }

    @Override
    public void onError(Progress progress) {

    }

    @Override
    public void onFinish(File file, Progress progress) {

    }

    @Override
    public void onRemove(Progress progress) {

    }
}
