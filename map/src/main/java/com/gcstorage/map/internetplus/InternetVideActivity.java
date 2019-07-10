package com.gcstorage.map.internetplus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.framework.utils.LoadingUtils;
import com.gcstorage.framework.utils.Logger;
import com.jeff.map.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

//CamerasVideoActivity
public class InternetVideActivity extends FragmentActivity implements View.OnClickListener {
    private LoadingUtils loading;
    private Context mcontext = InternetVideActivity.this;
    private final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MicroRecon/icon";
    File dir = new File(path);
    OrientationUtils orientationUtils;
    private GSYVideoOptionBuilder gsyVideoOption;
    private boolean isTransition;
    private Date mStartDate;
    private Date mEndDate;

    private boolean isPlay;
    private boolean isPause;

    private ImageView ivBack;
    private ImageView iv_control;
    private FrameLayout containerController;
    private LinearLayout ll_info;
    private TextView tvTitle;
    private RelativeLayout rlTitle;
    private StandardGSYVideoPlayer videoPlayer;
    private ImageView ivScreenshot;
    private ImageView ivCollect;
    private ImageView iv_history;
    private LinearLayout ll_crop;
    private TextView tv_resolving;
    private LinearLayout ll_resolving;
    private String rtsp_url;
    private InternetMapVideoApi api;
    private InternetPlusBean extra;
    private String currAlarm;
    private String currName;
    private LinearLayout ll_my_collect;
    private LinearLayout ll_watch_history;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横竖屏切换
        setContentView(R.layout.activity_internet_video);
        ButterKnife.bind(this);
        loading = new LoadingUtils(this);
        api = new InternetMapVideoApi();
        initView();
        intentParam();
        //初始化摄像头相关参数
        initParameter();
        //获取摄像头的流,并进行播放
        getRTSPSteam();
        //初始化时间选择器
        initTimeView();
        //云控制的布局
    }

    private void initTimeView() {
        //默认播放的开始时间,结束时间
        mStartDate = new Date();
        mEndDate = new Date();

        //自定义控件,演示用的,后期会改动 18/10/26
        ll_resolving = (LinearLayout) videoPlayer.findViewById(R.id.ll_resolving);
        TextView tv_fbl1 = (TextView) videoPlayer.findViewById(R.id.tv_fbl1);
        TextView tv_fbl2 = (TextView) videoPlayer.findViewById(R.id.tv_fbl2);
        TextView tv_fbl3 = (TextView) videoPlayer.findViewById(R.id.tv_fbl3);
        TextView tv_fbl4 = (TextView) videoPlayer.findViewById(R.id.tv_fbl4);
        TextView tv_fbl5 = (TextView) videoPlayer.findViewById(R.id.tv_fbl5);
        tv_fbl1.setOnClickListener(this);
        tv_fbl2.setOnClickListener(this);
        tv_fbl3.setOnClickListener(this);
        tv_fbl4.setOnClickListener(this);
        tv_fbl5.setOnClickListener(this);

        ImageView fullscreen = (ImageView) videoPlayer.findViewById(R.id.fullscreen);
        tv_resolving = (TextView) videoPlayer.findViewById(R.id.tv_resolving);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goneFragment();
                if (orientationUtils != null && videoPlayer != null) {
                    //直接横屏
                    orientationUtils.resolveByClick();
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    videoPlayer.startWindowFullscreen(mcontext, true, true);
                }
            }
        });
        tv_resolving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = ll_resolving.getVisibility();
                if (visibility == View.GONE) {
                    ll_resolving.setVisibility(View.VISIBLE);
                } else {
                    ll_resolving.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        LinearLayout ll_control = findViewById(R.id.ll_control);  //云控制
        ll_control.setVisibility(View.GONE);
        LinearLayout ll_share = findViewById(R.id.ll_share);  //分享

        iv_control = findViewById(R.id.iv_control);
        ll_watch_history = findViewById(R.id.ll_watch_history);
        containerController = findViewById(R.id.container_controller);
        ll_info = findViewById(R.id.ll_info);
        tvTitle = findViewById(R.id.tv_title);
        rlTitle = findViewById(R.id.rl_title);
        videoPlayer = findViewById(R.id.detail_player);
        ivScreenshot = findViewById(R.id.iv_screenshot);
        ivCollect = findViewById(R.id.iv_collect);
        iv_history = findViewById(R.id.iv_history);
        ll_crop = findViewById(R.id.ll_crop);
        ll_my_collect = findViewById(R.id.ll_my_collect);
        ivBack.setOnClickListener(this);
        ivCollect.setOnClickListener(this);
        ll_my_collect.setOnClickListener(this);
        iv_history.setOnClickListener(this);
        ll_crop.setOnClickListener(this);
        ll_watch_history.setOnClickListener(this);
    }

    private void intentParam() {
        extra = getIntent().getParcelableExtra("internet_data");
        currAlarm = getIntent().getStringExtra("curr_alarm");
        currName = getIntent().getStringExtra("curr_name");
    }

    private void initParameter() {
        GSYVideoManager.instance().setVideoType(this, GSYVideoType.IJKPLAYER);
        loading = new LoadingUtils(this);
        //返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.backToProtVideo();
            }
        });
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
    }

    //根据不同的分辨率,设置不同的截屏清晰度, 0-100.默认是最100%无压缩.
    private int quality = 100;
    private int newWidth;
    private int newheight;

    private boolean isCollect = false;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_history) {
            timeSelected();
        } else if (i == R.id.ll_crop) {
            shotImage();
        } else if (i == R.id.tv_fbl5) {
            tv_resolving.setText("130");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;
            newWidth = 195;
            newheight = 130;

        } else if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.iv_collect) {
            if (isCollect) {
                delCollection();
            } else {
                myAddCollection();
            }
        } else if (i == R.id.ll_my_collect) {
            Intent intent = new Intent();
            intent.putExtra("curr_alarm", currAlarm);
            intent.setClass(this, CollectionListActivity.class);
            startActivity(intent);
        } else if (i == R.id.ll_watch_history) {
            Intent intent = new Intent();
            intent.putExtra("curr_alarm", currAlarm);
            intent.putExtra("watch_history", "history");
            intent.setClass(this, CollectionListActivity.class);
            startActivity(intent);
        }
        if (rtsp_url != null) {
            initGSYVideo(rtsp_url);
        }
    }


    private void initGSYVideo(String path) {
        List<VideoOptionModel> list = new ArrayList<>();
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "stimeout", 20000000);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max_delay", 500000);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 1560);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);  // 无限读
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", "2000000");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", "4096");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", 1560);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 60);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 0);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fps", 30);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", 10);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_YV12);
        list.add(videoOptionModel);
//        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        list.add(videoOptionModel);
        //  关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
        gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setCacheWithPlay(false)
                .setUrl(path)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        videoPlayer.getBackButton().setVisibility(View.GONE);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                        Toast.makeText(mcontext, "该视频无法播放", Toast.LENGTH_SHORT).show();
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                }).build(videoPlayer);
        videoPlayer.startPlayLogic();
    }

    //隐藏云控制的Fragment
    private void goneFragment() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void getRTSPSteam() {
        if (extra.getCid() == null) {
            return;
        }
        api.getVideoStream(this, extra.getCid(), new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) throws Exception {
                if (data != null && data.length() > 0) {
                    initGSYVideo(data);
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }


    private void timeSelected() {

    }

    //获取截图
    void shotImage() {
        videoPlayer.taskShotPic(new GSYVideoShotListener() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                if (bitmap != null) {
                    try {
                        File file = saveBitmap(bitmap);
                        Toast.makeText(mcontext, "截图成功,请在相册中查看", Toast.LENGTH_SHORT).show();

                        //调用系统裁剪查人
                        if (file != null && file.exists()) {
                            startPhotoZoom(android.net.Uri.fromFile(file));
                        }
                    } catch (java.io.FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }

                } else {
                    Toast.makeText(mcontext, "截图失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //添加收藏
    private void myAddCollection() {
        api.addCollection(this, extra.getCid(), currAlarm, currName, "收藏地址",
                extra.getLongitude(), extra.getLatitude(), "", new ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) throws Exception {
                        isCollect = true;
                        ivCollect.setImageResource(isCollect ? R.mipmap.yishoucang : R.mipmap.shoucang);
                        Toast.makeText(InternetVideActivity.this.getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        isCollect = false;
                        ivCollect.setImageResource(R.mipmap.shoucang);
                        Toast.makeText(InternetVideActivity.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Logger.e("dong", "收藏成功onFailure:");
                    }
                }
        );
    }

    //取消收藏
    private void delCollection() {
        api.deleteCollection(this, extra.getId(), new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String data) throws Exception {
                isCollect = false;
                ivCollect.setImageResource(R.mipmap.shoucang);
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                ivCollect.setImageResource(isCollect ? R.mipmap.yishoucang : R.mipmap.shoucang);
                Logger.e("dong", "取消收藏成功onFailure:");
            }
        });
    }

    public void getHistoryVideo() {
        api.getHistorageStream(this, extra.getCid(), mStartDate.toString(), mEndDate.toString(), new ActionCallbackListener<List<InternetPlusBean>>() {
            @Override
            public void onSuccess(List<InternetPlusBean> data) throws Exception {
                if (data != null && data.size() != 0) {
                    //历史视频好像最多30分钟一段,要是这个地址的视频播放完了,播放下一个
                    String url = data.get(0).getUrl();
                    initGSYVideo(url);
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Toast.makeText(InternetVideActivity.this.getApplicationContext(), "未获取到历史视频流onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public File saveBitmap(Bitmap bitmap) throws java.io.FileNotFoundException {
        File file = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (bitmap != null) {
            file = new File(path, "LY-" + System.currentTimeMillis() + ".jpg");
            java.io.OutputStream outputStream = new java.io.FileOutputStream(file);
            //不等于空,按照指定的宽高来压缩
            if (newWidth != 0 && newheight != 0) {
                Bitmap bos = com.gcstorage.framework.utils.ImageUtil.zoomBitmap(bitmap, newWidth, newheight);
                bos.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                bos.recycle();
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            }
            bitmap.recycle();
            //发送广播系统更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            android.net.Uri uri = android.net.Uri.fromFile(file);
            intent.setData(uri);
            this.sendBroadcast(intent);
        }
        return file;
    }

    /**
     * 调用系统图片裁剪方法
     *
     * @param uri
     */
    public void startPhotoZoom(android.net.Uri uri) {
//        Intent intent = new Intent(this, CameraShotActivity.class);
//        intent.setData(uri);
//        startActivityForResult(intent, 1122);
    }
}
