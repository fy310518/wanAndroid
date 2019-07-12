package com.gcstorage.framework.net;

import android.app.Application;
import android.util.Log;

import com.gcstorage.framework.net.basebean.ApiResponse;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * 网络请求封装
 * Created by zjs on 2018/3/16.
 */

public class HttpUtils {
    private final static String TAG = "HttpUtil";
//    private volatile static HttpUtils httpUtils;
//
//    private HttpUtils(){}
//
//    public static HttpUtils newInstance(){
//        if (httpUtils == null) {
//            synchronized (HttpUtils.class) {
//                if (httpUtils == null) {
//                    httpUtils = new HttpUtils();
//                }
//            }
//        }
//        return httpUtils;
//    }

    /**
     * OkGo初始化，包括OkHttp的初始化
     *
     * @param app
     * @param isHttps
     */
    public static void initClient(Application app, boolean isHttps) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
//        if (BuildConfig.DEBUG) {
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        ////log打印级别，决定了log显示的详细程度

        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
//        }
        //log颜色级别，决定了log在控制台显示的颜色
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.addInterceptor(new BodyInterceptor());
        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(app)));
        if (isHttps) {
            //方法一：信任所有证书,不安全有风险
            //HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
            //方法二：自定义信任规则，校验服务端证书
            //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
            try {
                //方法三：使用预埋证书，校验服务端证书（自签名证书）
                HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(app.getAssets().open("srca.cer"));
                builder.sslSocketFactory(sslParams3.sSLSocketFactory, sslParams3.trustManager);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(app.getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return hostname.equals(session.getPeerHost());
                }
            });
        } else {
            HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
            builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        }
        OkGo.getInstance().init(app)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);
    }

    /**
     * get请求  zjs
     *
     * @param url
     * @param tag
     * @param params
     * @param callback
     */
    public static void get(String url, Object tag, HttpParams params, AbsCallback callback) {
        OkGo.get(url).tag(tag).params(params).execute(callback);
    }


    /**
     * post请求
     *
     * @param url
     * @param tag
     * @param params
     * @param callback
     */
    public static <T> void post(String url, Object tag, HttpParams params, AbsCallback callback) {
        Log.d("dwb", "Url" + url + "\n  params ==" + params.toString());
        OkGo.<ApiResponse<T>>post(url).tag(tag).params(params).execute(callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param tag
     * @param file
     * @param callback
     */
    public static <T> void uploadFile(String url, Object tag, String alarm, String token, File file, AbsCallback callback) {
        String name = file.getName();
        Log.e("zjs", name);
        OkGo.<ApiResponse<T>>post(url).tag(tag)
                .params("alarm", alarm)
                .params("fileName", file)
                .execute(callback);
   /*     OkGo.<ApiResponse<T>>post(url).tag(tag)
                .params("file", file)
                .execute(callback);*/
    }

    /**
     * 文件下载
     *
     * @param url
     * @param tag
     * @param callback
     */
    public static void downloadFile(String url, Object tag, Callback<File> callback) {
        OkGo.<File>get(url).tag(tag).execute(callback);
    }

    /**
     * 断点文件下载
     *
     * @param url      文件下载地址
     * @param tag      下载标识
     * @param listener 下载进度相关监听
     * @return 下载任务task，这个task用于开启，暂停，取消，重新下载
     * 调用完成后,不要忘记start
     */
    public static DownloadTask breakPointDownload(String url, String tag, DownloadListener listener) {
        GetRequest<File> request = OkGo.get(url);
        return OkDownload.request(tag, request)
                .save()
                .register(listener);
    }
}
