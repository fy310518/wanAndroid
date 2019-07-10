package com.gcstorage.map.shake.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.gcstorage.map.shake.api.ApplyShakeHttp;
import com.gcstorage.map.shake.bean.RTSPSteam;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频播放的页面 2019/4/13 0013.
 */

public class CamerasVideoActivity extends FragmentActivity implements View.OnClickListener {

    private LoadingUtils loading;
    private Context mcontext = CamerasVideoActivity.this;
    private final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MicroRecon/icon";
    File dir = new File(path);
    OrientationUtils orientationUtils;
    private GSYVideoOptionBuilder gsyVideoOption;
    private boolean isTransition;
    private Date mStartDate;
    private Date mEndDate;

    private boolean isPlay;
    private boolean isPause;
    private String url = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";//互联网的测试地址

    private ImageView ivBack;
    private LinearLayout ll_control;
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
    private String name;
    private String address;
    private String cameraID;
    private String cameraIp;
    private RTSPSteam history;
    private String rtsp_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横竖屏切换
        setContentView(R.layout.activity_cameras_video);
        ButterKnife.bind(this);
        loading = new LoadingUtils(this);
        initView();
        intentParam();
        //初始化摄像头相关参数
        initParameter();
        //获取摄像头的流,并进行播放
//        getRTSPSteam();
        Logger.d("dong", "rtsp的流==" +rtsp_url);
        if (rtsp_url != null) {
            initGSYVideo(rtsp_url);
        }
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
        //云控制
        ll_control = findViewById(R.id.ll_control);
        iv_control = findViewById(R.id.iv_control);
        containerController = findViewById(R.id.container_controller);
        ll_info = findViewById(R.id.ll_info);
        tvTitle = findViewById(R.id.tv_title);
        rlTitle = findViewById(R.id.rl_title);
        videoPlayer = findViewById(R.id.detail_player);
        ivScreenshot = findViewById(R.id.iv_screenshot);
        ivCollect = findViewById(R.id.iv_collect);
        iv_history = findViewById(R.id.iv_history);
        ll_crop = findViewById(R.id.ll_crop);
    }

    private void intentParam() {
        Intent intent = getIntent();
        name = intent.getStringExtra(ApplyShakeResultGisActivity.NAME);
        address = intent.getStringExtra(ApplyShakeResultGisActivity.ADDRESS);
        cameraID = intent.getStringExtra(ApplyShakeResultGisActivity.ID);
        cameraIp = intent.getStringExtra(ApplyShakeResultGisActivity.IP);
        cameraIp = intent.getStringExtra(ApplyShakeResultGisActivity.IP);
        rtsp_url = intent.getStringExtra(ApplyShakeResultGisActivity.RTSP);
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_fbl1) {
            tv_resolving.setText("1080");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;

        } else if (i == R.id.tv_fbl2) {
            tv_resolving.setText("720");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;

        } else if (i == R.id.tv_fbl3) {
            tv_resolving.setText("480");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;
            newWidth = 720;
            newheight = 480;

        } else if (i == R.id.tv_fbl4) {
            tv_resolving.setText("270");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;
            newWidth = 405;
            newheight = 270;

        } else if (i == R.id.tv_fbl5) {
            tv_resolving.setText("130");
            ll_resolving.setVisibility(View.GONE);
            quality = 100;
            newWidth = 195;
            newheight = 130;

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

    private boolean isCollect = false;
    private boolean isShowController = false;

    //隐藏云控制的Fragment
    private void goneFragment() {
        isShowController = !isShowController;
        if (!isShowController) {
            ll_info.setVisibility(View.VISIBLE);
            containerController.setVisibility(View.GONE);
//            transaction.hide(fragment);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private String camera_id_test = "001z48vu3WkC9WTdC";

    private void getRTSPSteam() {
        ApplyShakeHttp.getInstan().getRtspURL(camera_id_test, this, new ActionCallbackListener<RTSPSteam>() {
            @Override
            public void onSuccess(RTSPSteam data) {
                if (data != null) {
                    history = data;
                    if (history.getPath() != null) {
                        initGSYVideo(history.getPath());
                    } else {
                        Toast.makeText(mcontext, "没有获取视频流", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Toast.makeText(mcontext, "获取视频流", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
