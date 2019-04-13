package com.fy.baselibrary.h5;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.retrofit.observer.RequestBaseObserver;
import com.fy.baselibrary.utils.GsonUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * DESCRIPTION：h5 android 交互
 * Created by fangs on 2019/3/27 17:03.
 */
public class BaseAndroidJSInterface {

    protected Context context;
    protected WebView view;
    private String host;

    private ArrayMap<String, String> defaultParams = new ArrayMap<>();
    private IProgressDialog progressDialog;

    public BaseAndroidJSInterface(Context context, WebView view, String host) {
        this.context = context;
        this.view = view;
        this.host = host;
    }

    public void setProgressDialog(IProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public void setDefaultParams(ArrayMap<String, String> defaultParams) {
        this.defaultParams = defaultParams;
    }

    //定义方法 供 h5 调用
    @JavascriptInterface
    public String httpRequest(String requestContent) {
        H5RequestBean request = GsonUtils.fromJson(requestContent, H5RequestBean.class);

        if (TextUtils.isEmpty(request.getUrl())) {
            return "{错误提示}";//todo 返回 json格式 错误信息
        }

        ArrayMap<String, String> params = request.getParams();
        if (!defaultParams.isEmpty()) {
            for (String key : defaultParams.keySet()) {
                params.put(key, defaultParams.get(key));
            }
        }

        ArrayMap<String, String> headers = request.getHeader();
        String method = request.getRequestMethod();

        switch (method.toUpperCase()) {
            case "GET":
                httpGet(headers, params, request.getUrl(), request.getJsMethod());
                break;
            case "POST":
                httpPost(headers, params, request.getUrl(), request.getJsMethod());
                break;
        }
        return null;
    }

    private void httpGet(ArrayMap<String, String> headers, ArrayMap<String, String> params, final String url, final String jsMethod) {
        RequestUtils.create(LoadService.class)
                .jsInAndroidGetRequest(host + url, headers, params)// todo 请求头 带测试
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(context))
                .subscribe(getCallObserver(jsMethod));
    }

    private void httpPost(ArrayMap<String, String> headers, ArrayMap<String, String> params, final String url, final String jsMethod) {
        RequestUtils.create(LoadService.class)
                .jsInAndroidPostRequest(host + url, headers, params)// todo 请求头 带测试
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(context))
                .subscribe(getCallObserver(jsMethod));
    }

    //定义网络请求 观察者，统一处理返回数据
    private RequestBaseObserver<String> getCallObserver( final String jsMethod){
        return new RequestBaseObserver<String>(progressDialog) {
            @Override
            protected void onSuccess(String body) {
                //Android 调用 h5 方法
                view.loadUrl("javascript:" + jsMethod + "(\'" + body + "\')");
            }

            @Override
            protected void updataLayout(int flag) {
                super.updataLayout(flag);

                view.loadUrl("javascript:" + jsMethod + "(\'" + new Gson().toJson("") + "\')");
            }
        };
    }
}
