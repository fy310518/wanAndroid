package com.fy.baselibrary.retrofit;

import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.utils.ConstantUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava + Retrofit 实现缓存机制 (目前仅适用于 不含界面跳转的数据展示，比如加载个人详情界面；见谅 ^_^)
 * Created by fangs on 2017/12/12.
 */
public class RxNetCache {

    private volatile static RxNetCache rxNetCache;
    private Builder builder;

    private RxNetCache() {
        ConstantUtils.custom_Url = SpfUtils.getSpfSaveStr("ServiceAddress");
    }

    public static RxNetCache getInstens(){
        if (null == rxNetCache) {
            synchronized (RxNetCache.class) {
                if (null == rxNetCache) {
                    rxNetCache = new RxNetCache();
                }
            }
        }

        return rxNetCache;
    }

    /**
     * 同时从缓存和网络获取请求结果
     * @param fromNetwork  从网络获取的 Observable
     * @return
     */
    public <T> Observable<T> request(Observable<T> fromNetwork) {
        /**
         * 定义读取缓存数据的 被观察者
         */
        Observable<T> fromCache = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
                ACache mCache = ACache.get(BaseApp.getAppCtx());
                T cache = (T) mCache.getAsObject(builder.getApi());
                if (null != cache) {
                    L.e("net cache", cache.toString());
                    subscriber.onNext(cache);
                }
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

        /**
         * 使用doOnNext 操作符，对网络请求的数据 缓存到本地
         * 这里的fromNetwork 不需要指定Schedule,在handleRequest中已经变换了
         */
        fromNetwork = fromNetwork.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T result) throws Exception {
                L.e("net doOnNext", result.toString());
                ACache mCache = ACache.get(BaseApp.getAppCtx());
                mCache.put(builder.getApi(), (Serializable)result, builder.getExpireTime());
            }
        });

        return Observable.concat(fromCache, fromNetwork);
    }

    public RxNetCache setBuilder(Builder builder) {
        this.builder = builder;

        return this;
    }


    public static class Builder {
        String api = "";//缓存key
        int expireTime = 86400;//默认一天超时时间(单位：秒；-1：表示没有过期时间)

        public Builder() {
        }

        public String getApi() {
            return api;
        }

        public Builder setApi(String api) {
            this.api = ConstantUtils.userId + api;
            return this;
        }

        public int getExpireTime() {
            return expireTime;
        }

        public Builder setExpireTime(int expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        public RxNetCache create(){
            return getInstens().setBuilder(this);
        }
    }
}
