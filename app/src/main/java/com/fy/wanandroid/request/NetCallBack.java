package com.fy.wanandroid.request;

import android.content.Context;
import android.content.Intent;

import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.retrofit.observer.RequestBaseObserver;
import com.fy.baselibrary.retrofit.ServerException;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.AppUtils;

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

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();//todo 正式发布时候 注释此打印

        if (e instanceof ServerException) {
            dismissProgress();
            if (e.getMessage().equals("请先登录！")) {//token 失效 进入登录页面
                Context context = ConfigUtils.getAppCtx();

                Intent intent = new Intent();
                intent.setAction(AppUtils.getLocalPackageName() + ".login.LoginActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            if (((ServerException) e).code != 401) actionResponseError(e.getMessage());
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        } else {
            super.onError(e);
        }
    }
}
