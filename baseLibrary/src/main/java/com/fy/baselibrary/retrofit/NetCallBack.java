package com.fy.baselibrary.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NetUtils;
import com.fy.baselibrary.utils.T;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 自定义Subscribe
 * Created by fangs on 2017/8/28.
 */
public abstract class NetCallBack<V> implements Observer<V> {

    private Disposable disposed;
    private IProgressDialog progressDialog;
    private CommonDialog dialog;

    public NetCallBack() {
    }

    public NetCallBack(IProgressDialog pDialog) {
        this.progressDialog = pDialog;
        init();
    }

    private void init() {
        if (null == progressDialog) return;
        dialog = progressDialog.getDialog();
        if (null == dialog) return;
        dialog.setDialogList(() -> {
            if (null != disposed && !disposed.isDisposed()) disposed.dispose();
        });
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        L.e("net", "onSubscribe()");

        this.disposed = d;
        if (null != progressDialog && null != disposed && !disposed.isDisposed()) {
            progressDialog.show();
        }
    }

    @Override
    public void onNext(V t) {
        L.e("net", "onNext()");
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        L.e("net", "onError()");
        e.printStackTrace();
        if (!NetUtils.isConnected(BaseApp.getAppCtx())) {
            actionResponseError("网络不可用");
            updataLayout(StatusLayoutManager.LAYOUT_NETWORK_ERROR_ID);
        } else if (e instanceof ServerException) {
            if (e.getMessage().equals("请先登录！")) {//token 失效 进入登录页面
                try {
                    Class cla = Class.forName("wanandroid.fy.com.login.LoginActivity");
                    Context context = BaseApp.getAppCtx();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("untoken",true);
                    Intent intent = new Intent(context, cla);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }

            if (((ServerException) e).code != 401)actionResponseError(e.getMessage());
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        } else if (e instanceof ConnectException) {
            actionResponseError("请求超时，请稍后再试...");
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        } else if (e instanceof SocketTimeoutException) {
            actionResponseError("服务器响应超时，请稍后再试...");
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        } else {
            actionResponseError("请求失败，请稍后再试...");
            updataLayout(StatusLayoutManager.REQUEST_FAIL);
        }
        dismissProgress();
    }

    @Override
    public void onComplete() {
        L.e("net", "onComplete()");
        updataLayout(StatusLayoutManager.LAYOUT_CONTENT_ID);
        dismissProgress();
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    private void actionResponseError(String msg) {
        T.showShort(msg);
    }

    /**
     * 取消进度框
     */
    private void dismissProgress() {
        if (null != progressDialog) {
            progressDialog.close();
        }
    }


    /**
     * 请求成功 回调
     *
     * @param t 请求返回的数据
     */
    protected abstract void onSuccess(V t);

    /**
     * 更新activity 界面（多状态视图）
     * 可根据flag 判断请求失败
     *
     * @param flag
     */
    protected abstract void updataLayout(int flag);

}
