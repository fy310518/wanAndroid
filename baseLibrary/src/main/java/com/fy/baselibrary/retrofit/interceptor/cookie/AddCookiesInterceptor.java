package com.fy.baselibrary.retrofit.interceptor.cookie;

import android.annotation.SuppressLint;

import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现拦截器接口,将本地的 cookie追加到 http 请求头中
 * Created by fangs on 2018/3/30.
 */
public class AddCookiesInterceptor implements Interceptor {

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (null == chain) L.e("http", "Addchain == null");

        final Request.Builder builder = chain.request().newBuilder();

        String[] cookieArray = SpfUtils.getSpfSaveStr("cookie").split(";");

        Observable.fromArray(cookieArray)
                .subscribe(cookie -> {
                    //添加cookie
                    L.e("http", "AddCookiesInterceptor--" + cookie);
                    builder.addHeader("cookie", cookie);
                });

        return chain.proceed(builder.build());
    }
}
