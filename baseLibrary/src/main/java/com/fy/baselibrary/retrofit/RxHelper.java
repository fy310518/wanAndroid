package com.fy.baselibrary.retrofit;

import com.fy.baselibrary.utils.L;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: Rx 一些巧妙的处理
 * Created by Jam on 16-6-12
 */
public class RxHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BeanModule<T>, T> handleResult() {

        return new ObservableTransformer<BeanModule<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BeanModule<T>> upstream) {
                return upstream.flatMap(new Function<BeanModule<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BeanModule<T> tBeanModule) throws Exception {
                        if (tBeanModule.isSuccess()) {
                            return createData(tBeanModule.getData());
                        } else {
                            return Observable.error(new ServerException(tBeanModule.getErrorMsg(), tBeanModule.getErrorCode()));
                        }
                    }
                })
                        .subscribeOn(Schedulers.io())//指定的是上游发送事件的线程
                        .observeOn(AndroidSchedulers.mainThread());//指定的是下游接收事件的线程
//              多次指定上游的线程只有第一次指定的有效, 也就是说多次调用subscribeOn() 只有第一次的有效, 其余的会被忽略.
//              多次指定下游的线程是可以的, 也就是说每调用一次observeOn() , 下游的线程就会切换一次.
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> subscriber) throws Exception {
                try {
                    L.e("net", "成功 _ onNext");
                    if (null == data) subscriber.onNext((T) new Object());
                    else subscriber.onNext(data);

                    subscriber.onComplete();
                } catch (Exception e) {
                    L.e("net", "异常 _ onError");
                    subscriber.onError(e);
                }
            }
        });
    }
}
