package com.fy.wanandroid.testdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.BaseActivityBean;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.LoadCallBack;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.statuslayout.StatusLayout;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NotificationUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressInterceptor;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 多状态布局 demo
 * Created by fangs on 2018/3/16.
 */
public class StatusDemoActivity extends AppCompatActivity implements IBaseActivity, StatusLayout.OnRetryListener, StatusLayout.OnSetStatusView, View.OnClickListener {

    @BindView(R.id.imgDemo)
    ImageView imgDemo;

    StatusLayoutManager slManager;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tvKing)
    TextView tvKing;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_status;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        BaseActivityBean activityBean = (BaseActivityBean) activity.getIntent()
                .getSerializableExtra("ActivityBean");
        slManager = activityBean.getSlManager();

        initNotificationChannel();

        uploadFiles();
//        uploadFiles();

//        loadImage();
    }


    @OnClick({R.id.tvKing, R.id.tvKing2})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKing:
                NotificationUtils.FyBuild.init()
                        .setChannel(1, "chat")
                        .setIcon(R.mipmap.ic_launcher, R.color.appHeadBg)
                        .setMsgTitle("收到一条聊天消息")
                        .setMsgContent("今天中午吃什么？")
                        .sendNotify(this, LoginActivity.class);

//                NightModeUtils.switchNightMode(this);
                slManager.showNetWorkError();
                Observable.timer(3000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                slManager.showError();
                            }
                        });
                break;
            case R.id.tvKing2:
                NotificationUtils.FyBuild.init()
                        .setChannel(2, "subscribe")
                        .setIcon(R.mipmap.ic_launcher, R.color.appHeadBg)
                        .setMsgTitle("收到一条订阅消息")
                        .setMsgContent("地铁沿线30万商铺抢购中！")
                        .sendNotify(this, null);
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRetry() {
        slManager.showEmptyData();

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Exception {
                        slManager.showNetWorkError();
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Object, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Object o) throws Exception {
                        slManager.showError();
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        slManager.showContent();
                    }
                });
    }

    @NeedPermission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @SuppressLint("CheckResult")
    public void uploadFiles() {
        List<String> files = new ArrayList<>();
        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/679f6337gy1fr69ynfq3nj20hs0qodh0.jpg");
        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/IMG_20181108_144507.jpg");
        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/IMG_20181108_143502.jpg");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/体质健康.zip");

        RequestUtils.create(LoadService.class)
                .uploadFile(files)
                .compose(RxHelper.bindToLifecycle(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadCallBack<Object>() {
                    @Override
                    protected void onProgress(String percent) {
                        L.e("进度M", percent + "%-->" + Thread.currentThread().getName());
                    }

                    @Override
                    protected void onSuccess(Object t) {

                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });

//        UpLoadUtils.uploadFiles(files, RequestUtils.create(LoadService.class))
//                .compose(RxHelper.bindToLifecycle(this))
//                .subscribe(new LoadCallBack<Object>() {
//                    @Override
//                    protected void onProgress(String percent) {
//                        L.e("进度M", percent + "%-->" + Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    protected void onSuccess(Object t) {
//
//                    }
//
//                    @Override
//                    protected void updataLayout(int flag) {
//
//                    }
//                });
    }

    /**
     * glide 带下载进度 demo
     */
    public void loadImage() {
        String url = "http://imgsrc.baidu.com/imgad/pic/item/10dfa9ec8a13632753f607fa9b8fa0ec09fac7e4.jpg";

        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                L.e("glide", progress + "%");
            }
        });

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(this)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressInterceptor.removeListener(url);
                        return false;
                    }
                })
                .into(imgDemo);
    }

    private void initNotificationChannel() {
        String channelId = "chat";
        String channelName = "聊天消息";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);

        channelId = "subscribe";
        channelName = "订阅消息";
        importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);
    }


    @Override
    public View setStatusView() {
        return rv;
    }
}
