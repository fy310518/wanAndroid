package com.fy.baselibrary.retrofit.upload;

import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;

/**
 * 自定义Subscribe (增强 NetCallBAck)
 * Created by fangs on 2018/5/21.
 */
public abstract class UpLoadCallBack extends NetCallBack {

    public UpLoadCallBack() {}

    public UpLoadCallBack(IProgressDialog pDialog) {
        super(pDialog);
    }

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            onProgress((Integer) o);
        } else {
            super.onNext(o);
        }
    }

    /**
     * 上传进度 回调方法
     *
     * @param percent
     */
    protected abstract void onProgress(Integer percent);
}
