package com.fy.baselibrary.h5;

import android.app.Activity;
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

import java.io.IOException;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

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

    /**
     * 设置 请求参数
     * @param key
     * @param value
     * @return
     */
    public BaseAndroidJSInterface setDefaultParams(String key, String value) {
        defaultParams.put(key, value);
        return this;
    }

    //解析 H5RequestBean 获取 请求参数
    private ArrayMap<String, String> getHttpParams(H5RequestBean request){
        ArrayMap<String, String> params = request.getParams();

        if (!defaultParams.isEmpty()) {
            for (String key : defaultParams.keySet()) {
                params.put(key, defaultParams.get(key));
            }
        }

        return params;
    }

    //添加请求头
    private ArrayMap<String, String> getHeaderParams(H5RequestBean request){
        ArrayMap<String, String> params = request.getHeader();
        if (null == params){
            params = new ArrayMap<>();
//            params.put("Content-Type", "multipart/form-data;charse=UTF-8");
//            params.put("Connection", "keep-alive");
//            params.put("Accept", "*/*");
//            params.put("app-type", "Android");
        }

        return params;
    }

    /**
     * 定义本地网络请求方法 供 h5 调用
     * @param requestContent h5 传递的 网络请求 请求头，请求方法（get，post），请求参数，请求 url
     * @return ""
     */
    @JavascriptInterface
    public String httpRequest(String requestContent) {
        return httpRequest("", requestContent);
    }

    /**
     * 定义本地网络请求方法 供 h5 调用
     * @param hostIp          请求的主机地址 可为空，为空则表示 使用构造方法传递的 host
     * @param requestContent  h5 传递的 网络请求 请求头，请求方法（get，post），请求参数，请求 url
     * @return
     */
    @JavascriptInterface
    public String httpRequest(String hostIp, String requestContent) {
        H5RequestBean request = null;
        try {
            request = GsonUtils.fromJson(requestContent, H5RequestBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "{错误提示}";//todo 返回 json格式 错误信息
        }

        if (TextUtils.isEmpty(request.getUrl())) {
            return "{错误提示}";//todo 返回 json格式 错误信息
        }

        String method = request.getRequestMethod();
        ArrayMap<String, String> headers = getHeaderParams(request);
        ArrayMap<String, String> params = getHttpParams(request);

        String hostAddress = !TextUtils.isEmpty(hostIp) ? hostIp : this.host;

        switch (method.toUpperCase()) {
            case "GET":
                httpGet(headers, params, hostAddress + request.getUrl(), request.getJsMethod());
                break;
            case "POST":
                httpPost(headers, params, hostAddress + request.getUrl(), request.getJsMethod());
                break;
        }

        return "";
    }


    private void httpGet(ArrayMap<String, String> headers, ArrayMap<String, String> params, final String url, final String jsMethod) {
        RequestUtils.create(LoadService.class)
                .jsInAndroidGetRequest(url, headers, params)// todo 请求头 带测试
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(context))
                .subscribe(getCallObserver(jsMethod));
    }

    private void httpPost(ArrayMap<String, String> headers, ArrayMap<String, String> params, final String url, final String jsMethod) {
        RequestUtils.create(LoadService.class)
                .jsInAndroidPostRequest(url, headers, params)// todo 请求头 带测试
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(context))
                .subscribe(getCallObserver(jsMethod));
    }

    //定义网络请求 观察者，统一处理返回数据
    private RequestBaseObserver<ResponseBody> getCallObserver( final String jsMethod){
        return new RequestBaseObserver<ResponseBody>(progressDialog) {
            @Override
            protected void onSuccess(ResponseBody body) {
                //Android 调用 h5 方法
                try {
                    String data = body.string();
                    view.loadUrl("javascript:" + jsMethod + "(\'" + data + "\')");
                } catch (IOException e) {
                    e.printStackTrace();
                    view.loadUrl("javascript:" + jsMethod + "(\'" + "" + "\')");
                }
            }

            @Override
            protected void updataLayout(int flag) {
                super.updataLayout(flag);

                view.loadUrl("javascript:" + jsMethod + "(\'" + new Gson().toJson("") + "\')");
            }
        };
    }



    @JavascriptInterface
    public void back() {
        ((Activity)this.context).finish();
    }

    @JavascriptInterface
    public void webViewback() {
        if (this.view != null && this.view.canGoBack()) {
            this.view.goBack();
        } else {
            ((Activity)this.context).finish();
        }

    }
}
