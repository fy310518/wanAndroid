package com.fy.wanandroid.status;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fy.baselibrary.application.BaseActivityBean;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.load.LoadCallBack;
import com.fy.baselibrary.retrofit.load.up.UpLoadUtils;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NightModeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import com.fy.baselibrary.utils.NotificationUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressInterceptor;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.api.ApiService;
import com.fy.wanandroid.login.LoginActivity;

/**
 * 多状态布局 demo
 * Created by fangs on 2018/3/16.
 */
public class StatusDemoActivity extends AppCompatActivity implements IBaseActivity {

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

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        BaseActivityBean activityBean = (BaseActivityBean) activity.getIntent()
                .getSerializableExtra("ActivityBean");
        slManager = activityBean.getSlManager();

        initNotificationChannel();

//        updateUser();

//        uploadFiles();

//        loadImage();
    }


    @OnClick({R.id.tvKing, R.id.tvKing2})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKing:
                NotificationUtils.manageNotificationChannel(this, "chat");
                NotificationUtils.sendSubscribeMsg(this, LoginActivity.class, "chat", "收到一条聊天消息", "今天中午吃什么？");

//                NightModeUtils.switchNightMode(this);
//                slManager.showNetWorkError();
//                Observable.timer(3000, TimeUnit.MILLISECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//                                slManager.showError();
//                            }
//                        });
                break;
            case R.id.tvKing2:
                NotificationUtils.sendSubscribeMsg(this, null, "subscribe", "收到一条订阅消息", "地铁沿线30万商铺抢购中！");
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void reTry() {
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

    private void uploadImg() {
        List<String> fileList = new ArrayList<>();
        fileList.add(FileUtils.getSDCardPath() + "DCIM/Camera/20121006174327607.jpg");
        fileList.add(FileUtils.getSDCardPath() + "DCIM/Camera/tooopen_sy_133481514678.jpg");

        RequestUtils.create(LoadService.class)
                .uploadFile1(UpLoadUtils.fileToMultipartBodyParts(fileList))
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });

    }

    public void uploadFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Camera/20121006174327607.jpg"));
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Downloads.zip"));
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Camera/tooopen_sy_133481514678.jpg"));

        UpLoadUtils.uploadFiles(files, RequestUtils.create(LoadService.class))
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new LoadCallBack<Object>() {
                    @Override
                    protected void onProgress(String percent) {
                        L.e("进度监听", percent + "%");

                    }

                    @Override
                    protected void onSuccess(Object t) {

                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
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

    private void updateUser(){
        Map<String, Object> parame = new HashMap<>();
        parame.put("id", 2);
        parame.put("nickname", "zs");
        parame.put("sex", 0);

        RequestUtils.create(ApiService.class)
                .updateToApp(parame)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }

    private void initNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "订阅消息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationUtils.createNotificationChannel(this, channelId, channelName, importance);
        }
    }


}
