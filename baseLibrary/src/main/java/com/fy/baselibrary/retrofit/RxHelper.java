package com.fy.baselibrary.retrofit;

import android.app.Activity;
import android.content.Context;

import com.fy.baselibrary.application.BaseActivityBean;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.L;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Rx 一些巧妙的处理
 * Created by fangs on 2018/3/13.
 */
public class RxHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T> 泛型
     * @return  ObservableTransformer
     */
    public static <T> ObservableTransformer<BaseBean<T>, T> handleResult() {
        return new ObservableTransformer<BaseBean<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseBean<T>> upstream) {
                return upstream.flatMap(new Function<BaseBean<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseBean<T> baseBean) throws Exception {
                        if (baseBean.isSuccess()) {
                            return createData(baseBean.getData());
                        } else {
                            return Observable.error(new ServerException(baseBean.getMsg(), baseBean.getCode()));
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

    /**
     * 绑定activity 或 fragment声明周期，在生命周期结束后断开 rxjava 请求
     * @param context 环境
     * @param <T> 泛型
     * @return 包装过的被观察者
     */
    public static <T> ObservableTransformer<T, T> bindToLifecycle(@NonNull Context context) {
        BehaviorSubject<String> subject = null;
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            subject = ((BaseActivityBean) activity.getIntent()
                    .getSerializableExtra("ActivityBean"))
                    .getSubject();
        }

        BehaviorSubject<String> finalSubject = subject;
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.takeUntil(
                        finalSubject.filter(new Predicate<String>() {
                            @Override
                            public boolean test(String anObject) throws Exception {
                                L.e("net 请求or响应", anObject);
                                return Constant.DESTROY.equals(anObject);
                            }
                        })
                );
            }
        };
    }

    /**
     //  ┏┓　　　┏┓
     //┏┛┻━━━┛┻┓
     //┃　　　　　　　┃
     //┃　　　━　　　┃
     //┃　┳┛　┗┳　┃
     //┃　　　　　　　┃
     //┃　　　┻　　　┃
     //┗━┓　　　┏━┛
     //   ┃　　　┃   阿弥陀佛
     //   ┃　　　┃   神兽保佑
     //   ┃      ┃  代码无BUG
     //   ┃　　　┗━━━┓
     //   ┃　　　　　　　┣━┓
     //   ┃　　　　　　　┏━┛
     //   ┗┓┓┏━┳┓┏┛
     //     ┃┫┫　┃┫┫
     //     ┗┻┛　┗┻┛
     */
}
