package com.fy.wanandroid.login;

import android.Manifest;
import android.content.Context;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.base.mvp.BasePresenter;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.utils.notify.L;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;
import com.fy.wanandroid.request.NetDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * describe： 登录模块 presenter
 * Created by fangs on 2019/1/23 17:31.
 */
public class LogingPresenter extends BasePresenter<LoginContract.LoginView> {

//    private final ISingleInterfaceModel singleInterfaceModel;

    public LogingPresenter() {
//        this.singleInterfaceModel = new SingleInterfaceModel();
    }

    @NeedPermission(value = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, isRun = true)
    public void login(String mUserName, String mPassWord) {
        IProgressDialog progressDialog = new NetDialog().init(mView)
                .setDialogMsg(R.string.user_login);

        Map<String, Object> param = new HashMap<>();
        param.put("username", mUserName);
        param.put("password", mPassWord);

        RequestUtils.create(ApiService.class)
                .login(param)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle((Context) mView))
                .subscribe(new NetCallBack<LoginBean>(progressDialog) {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        mView.loginSuccess(login);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }
}
