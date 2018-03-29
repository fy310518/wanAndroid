package com.fy.baselibrary.retrofit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
        RequestComponent component = DaggerRequestComponent.builder().build();
        component.inJect(this);

        mCompositeDisposable = new CompositeDisposable();
    }

    public static synchronized RequestUtils getInstentce(){
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
     * @param clazz 被观察者 类（ApiService.class）
     * @param <T>   被观察者 实体类（ApiService）
     * @return
     */
    public static <T> T create(Class<T> clazz) {

        return getInstentce().netRetrofit.create(clazz);
    }



    /**
     * 使用RxJava CompositeDisposable 控制请求队列
     * @param d
     */
    public static void addDispos(Disposable d){
        getInstentce().mCompositeDisposable.add(d);
    }

    /**
     * 使用RxJava CompositeDisposable 清理所有的网络请求
     */
    public static void clearDispos(){
        getInstentce().mCompositeDisposable.clear();
    }
}
