package com.fy.wanandroid.testdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
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
import com.fy.baselibrary.retrofit.converter.file.FileRequestBodyConverter;
import com.fy.baselibrary.retrofit.load.LoadCallBack;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.statuslayout.OnSetStatusView;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.utils.notify.NotifyUtils;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressInterceptor;
import com.fy.baselibrary.utils.imgload.imgprogress.ProgressListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.AppUpdateEntity;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * 多状态布局 demo
 * Created by fangs on 2018/3/16.
 */
public class StatusDemoActivity extends AppCompatActivity implements IBaseActivity, OnSetStatusView, View.OnClickListener {

    @BindView(R.id.imgDemo)
    ImageView imgDemo;

    StatusLayoutManager slManager;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tvKing)
    TextView tvKing;
    @BindView(R.id.tvKing2)
    TextView tvKing2;

    @Override
    public boolean isShowHeadView() {
        return true;
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

//        List<String> files = new ArrayList<>();
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/679f6337gy1fr69ynfq3nj20hs0qodh0.jpg");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/IMG_20181108_144507.jpg");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/IMG_20181108_143502.jpg");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/RED,胡歌 - 逍遥叹（Cover 胡歌）.mp3");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/马郁 - 下辈子如果我还记得你.mp3");
//        files.add(FileUtils.getSDCardPath() + "DCIM/Camera/序人Hm - 再见（cover：张震岳）.mp3");

//        uploadFiles(files, tvKing);
//
//        List<String> files1 = new ArrayList<>();
//        files1.add(FileUtils.getSDCardPath() + "DCIM/Camera/总分.jpg");
//        files1.add(FileUtils.getSDCardPath() + "DCIM/Camera/首页7.jpg");
//        uploadFiles(files1, tvKing2);

        downLoadFiles(tvKing2);
//        uploadFiles();

//        loadImage();
    }


    @OnClick({R.id.tvKing, R.id.tvKing2})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKing:
                RemoteViews remoteViews = new RemoteViews(AppUtils.getLocalPackageName(), R.layout.notify_remote_layout);
                NotifyUtils.FyBuild fyBuild = NotifyUtils.FyBuild.init()
                        .setChannel(1, "chat")
                        .setIcon(R.mipmap.ic_launcher, R.color.appHeadBg)
                        .setLayout(remoteViews)
                        .createManager(this);

                int skip = 100;
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .take(skip + 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(aLong -> {
                            remoteViews.setTextViewText(R.id.txTitle, "我是标题");
//                remoteViews.setTextViewText(R.id.content, "我是内容");
                            remoteViews.setProgressBar(R.id.progress, 100, aLong.intValue(), false);
                            remoteViews.setTextViewText(R.id.txProgress, aLong.intValue() + "%");

                            fyBuild.notifyData(remoteViews);
                        })
                        .compose(RxHelper.bindToLifecycle(this))
                        .subscribe(aLong -> {
                            L.e("结束");
                        });

//                NightModeUtils.switchNightMode(this);
                showHideViewFlag(Constant.LAYOUT_NETWORK_ERROR_ID);
                break;
            case R.id.tvKing2:
                NotifyUtils.FyBuild.init()
                        .setChannel(2, "subscribe")
                        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                        .setIcon(R.mipmap.ic_launcher, R.color.appHeadBg)
                        .setMsgTitle("收到一条订阅消息")
                        .setMsgContent("地铁沿线30万商铺抢购中！")
                        .createManager(this)
                        .sendNotify();
                checkUpdate();
                break;
        }
    }

    @Override
    public View setStatusView() {
        return rv;
    }

    @Override
    public void showHideViewFlag(int flagId) {
        slManager.showHideViewFlag(flagId);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRetry() {
        showHideViewFlag(Constant.LAYOUT_EMPTYDATA_ID);

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

    public void downLoadFiles(TextView textView){
        RequestUtils.downLoadFile(this, "http://47.107.134.212:13201/8af7372fef2c4d849caffe524828b072.apk", new LoadCallBack<Object>() {
                    @Override
                    protected void onProgress(String percent) {
                        textView.setText(percent + "%");
                    }

                    @Override
                    protected void onSuccess(Object t) {

                    }

                    @Override
                    protected void updateLayout(int flag) {

                    }
                });
    }

    /**
     * 上传多个文件，监听进度 demo
     */
    @NeedPermission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void uploadFiles(List<String> files, TextView textView) {
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();

        List<MultipartBody.Part> data = FileRequestBodyConverter.filesToMultipartBodyPart(files, uploadOnSubscribe);
//        ArrayMap<String, Object> params = new ArrayMap<>();
//        params.put("filePathList", files);
//        params.put("UploadOnSubscribe", uploadOnSubscribe);
//        params.put("token", "大王叫我来巡山");
//        params.put("type", "图文");

        Observable.merge(Observable.create(uploadOnSubscribe), RequestUtils.create(LoadService.class).uploadFile2(data))
                .compose(RxHelper.bindToLifecycle(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadCallBack<Object>() {
                    @Override
                    protected void onProgress(String percent) {
                        textView.setText(percent + "%");
                    }

                    @Override
                    protected void onSuccess(Object t) {

                    }

                    @Override
                    protected void updateLayout(int flag) {

                    }
                });
    }

    /**
     * glide 带下载进度 demo
     */
    public void loadImage() {
        String url = "http://imgsrc.baidu.com/imgad/pic/item/10dfa9ec8a13632753f607fa9b8fa0ec09fac7e4.jpg";
//        String url = "http://112.74.129.54:13201/tax00/M00/06/08/QUIPAFySEwmAE6-vAAbuad59Db4685.jpg";

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
        NotifyUtils.createNotificationChannel(this, channelId, channelName, NotificationManager.IMPORTANCE_LOW, true, false, null);

        channelId = "subscribe";
        channelName = "订阅消息";
        NotifyUtils.createNotificationChannel(this, channelId, channelName, NotificationManager.IMPORTANCE_HIGH, true, true, null);
    }

    //检查更新
    private void checkUpdate(){
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("appId", 0);
        RequestUtils.create(ApiService.class)
                .checkUpdate(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<AppUpdateEntity>() {
                    @Override
                    protected void onSuccess(AppUpdateEntity dataList) {
                    }

                    @Override
                    protected void updateLayout(int flag) {
                    }
                });
    }
}
