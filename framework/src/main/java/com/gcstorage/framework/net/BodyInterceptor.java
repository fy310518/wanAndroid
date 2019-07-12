package com.gcstorage.framework.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public  class BodyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());



       /*

       if (!TextUtils.isEmpty(alarm))
            authorizedUrlBuilder.addQueryParameter("username", alarm);
        if (!TextUtils.isEmpty(token))
            authorizedUrlBuilder.addQueryParameter("token", token);


            */


        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .url(authorizedUrlBuilder.build())
                .build();
        Response response = chain.proceed(newRequest);

           /* if (response.code() != 200) {//这个地方可以根据返回码做一些事情。通过sendBroadcast发出去。
                ViewUtil.Toast("服务器异常,请稍后重试");
            }*/
        String format = String.format("发送请求 %s on %s%n%s", newRequest.url(), chain.connection(), newRequest.headers());
        LogUtil.yangRui().e(format);
        return response;
    }
}
