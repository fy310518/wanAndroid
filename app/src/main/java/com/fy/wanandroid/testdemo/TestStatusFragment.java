package com.fy.wanandroid.testdemo;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fy.baselibrary.base.fragment.BaseFragment;
import com.fy.baselibrary.utils.Constant;
import com.fy.wanandroid.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试 多状态布局 在fragment中的使用
 */
public class TestStatusFragment extends BaseFragment {

    @BindView(R.id.tvTest)
    TextView TextView;

    @BindView(R.id.flContent)
    FrameLayout flContent;

    @Override
    protected int setContentLayout() {
        return R.layout.test_status_fragment;
    }

    @Override
    public View setStatusView() {
        return flContent;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void baseInit() {
        showHideViewFlag(Constant.LAYOUT_NETWORK_ERROR_ID);
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        showHideViewFlag(Constant.LAYOUT_ERROR_ID);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRetry() {
        super.onRetry();
        showHideViewFlag(Constant.LAYOUT_EMPTYDATA_ID);

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Exception {
                        showHideViewFlag(Constant.LAYOUT_NETWORK_ERROR_ID);
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Object, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Object o) throws Exception {
                        showHideViewFlag(Constant.LAYOUT_ERROR_ID);
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        showHideViewFlag(Constant.LAYOUT_CONTENT_ID);
                    }
                });
    }

}
