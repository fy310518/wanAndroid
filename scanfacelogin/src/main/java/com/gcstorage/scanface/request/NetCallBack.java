package com.gcstorage.scanface.request;

import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.retrofit.observer.RequestBaseObserver;

/**
 * 应用主 moudle 网络请求观察者
 * 用于适配不同app的服务器 状态码不同
 * @param <T>
 */
public abstract class NetCallBack<T> extends RequestBaseObserver<T>{

    public NetCallBack() {}

    public NetCallBack(IProgressDialog pDialog) {
        super(pDialog);
    }

    public NetCallBack(Object context) {
        super(context);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();//todo 正式发布时候 注释此打印
        super.onError(e);
    }
}
