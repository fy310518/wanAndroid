package com.fy.wanandroid.request;

import android.content.Context;
import android.content.Intent;

import com.fy.baselibrary.ioc.ConfigUtils;
import com.fy.baselibrary.retrofit.RequestBaseObserver;
import com.fy.baselibrary.retrofit.ServerException;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
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
        if (e instanceof ServerException) {
            dismissProgress();
            if (e.getMessage().equals("请先登录！")) {//token 失效 进入登录页面
                try {//SSlPeerUnverifiedException
                    Class cla = Class.forName(AppUtils.getLocalPackageName() + ".login.LoginActivity");
                    Context context = ConfigUtils.getAppCtx();

                    Intent intent = new Intent(context, cla);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    context.startActivity(intent);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            if (((ServerException) e).code != 401) actionResponseError(e.getMessage());
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        } else {
            super.onError(e);
        }
    }
}
