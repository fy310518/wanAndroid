package com.fy.baselibrary.retrofit;

import com.fy.baselibrary.ioc.ConfigUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.Serializable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * 网络请求入口
 * Created by fangs on 2018/3/13.
 */
public class RequestUtils {

    public volatile static RequestUtils instentce;

    @Inject
    protected Retrofit netRetrofit;

    protected CompositeDisposable mCompositeDisposable;

    private RequestUtils() {
        DaggerRequestComponent.builder().build().inJect(this);

        mCompositeDisposable = new CompositeDisposable();
    }

    public static synchronized RequestUtils getInstance() {
        if (null == instentce) {
            synchronized (RequestUtils.class) {
                if (null == instentce) {
                    instentce = new RequestUtils();
                }
            }
        }

        return instentce;
    }

    /**
     * 得到 RxJava + Retrofit 被观察者 实体类
     *
     * @param clazz 被观察者 类（ApiService.class）
     * @param <T>   被观察者 实体类（ApiService）
     * @return 封装的网络请求api
     */
    public static <T> T create(Class<T> clazz) {
        return getInstance().netRetrofit.create(clazz);
    }

    /**
     * 同时从缓存和网络获取请求结果
     *
     * @param fromNetwork 从网络获取数据的 Observable
     * @param apiKey      key
     * @param <T>         泛型
     * @return 被观察者
     */
    public static <T> Observable<T> request(Observable<T> fromNetwork, String apiKey) {
        /** 定义读取缓存数据的 被观察者 */
        Observable<T> fromCache = Observable.create(new ObservableOnSubscribe<T>() {
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                T cache = (T) mCache.getAsObject(apiKey);
                if (null != cache) {
                    L.e("net cache", cache.toString());
                    emitter.onNext(cache);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /**
         * 使用doOnNext 操作符，对网络请求的数据 缓存到本地
         * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
         */
        fromNetwork = fromNetwork.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T result) throws Exception {
                L.e("net doOnNext", result.toString());
                ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                mCache.put(apiKey, (Serializable) result);
            }
        });

        return Observable.concat(fromCache, fromNetwork);
    }


    /**
     * 使用RxJava CompositeDisposable 控制请求队列
     *
     * @param d 切断订阅事件 接口
     */
    public static void addDispos(Disposable d) {
        getInstance().mCompositeDisposable.add(d);
    }

    /**
     * 使用RxJava CompositeDisposable 清理所有的网络请求
     */
    public static void clearDispos() {
        getInstance().mCompositeDisposable.clear();
    }

}
