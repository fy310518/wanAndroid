package com.fy.baselibrary.retrofit.interceptor;

import com.fy.baselibrary.retrofit.load.down.FileResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * describe: 文件下载拦截器
 * Created by fangs on 2019/10/8 22:08.
 */
public class FileDownInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        FileResponseBody fileResponseBody = new FileResponseBody(response.body());
        fileResponseBody.setDownUrl(request.url().url().toString());

        return response.newBuilder()
                .body(fileResponseBody)
                .build();
    }
}
