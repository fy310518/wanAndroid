package com.gcstorage.map.shake.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.framework.utils.Logger;
import com.jeff.map.R;

import java.util.ArrayList;
import java.util.List;

import com.gcstorage.map.shake.api.ApplyShakeHttp;
import com.gcstorage.map.shake.maputils.LocationMapTask;
import com.gcstorage.map.shake.maputils.OnLocationGetListener;
import com.gcstorage.map.shake.maputils.PositionEntity;
import com.gcstorage.map.shake.api.ShakeCameraModel;


public class ApplyShakeActivity extends Activity implements OnLocationGetListener {

    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;
    private LinearLayout mLoadingLl;
    private TextView jumpTv;
    private TextView tv_loading;
    private ImageView ivProgressSpinner;
    private TextView tv_distance;
    private RadioGroup rg_distance;
    private RadioGroup rg_shake_type;
    private ImageView ivLineUp;
    private ImageView ivLineDown;
    private RadioButton rbtnWifi;
    private TextView ps;


    private int shakeType = 1; // 1 摄像头  2 监控室  3 基站
    private ApplyShakeListener mShakeListener;
    private Vibrator mVibrator;
    private ApplyShakeListener.OnShakeListener listener;
    private List<ShakeCameraModel> mData;
    private String shakeName = "摄像头";
    private RotateAnimation refreshingAnimation;
    private AsyncTask shakeTask;
    private String distance = "100";
    private boolean isWifi;
    private double latitue;
    private double longitude;
    private ApplyShakeHttp shakeHttp;
    private Context mContext;
    private String mAlarm;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_shake);
        mContext = ApplyShakeActivity.this;
        shakeHttp = ApplyShakeHttp.getInstan();
        mAlarm = getIntent().getStringExtra("alarm");
        mToken = getIntent().getStringExtra("token");
        initView();
        initLocation();
        mData = new ArrayList<>();
        initAnimation();  //初始化动画
        initListener();
    }

    private void initListener() {
        jumpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApplyShakeResultGisActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shake_data", (ArrayList) mData);
                intent.putExtras(bundle);
                intent.putExtra("shake_type", shakeType);
                startActivity(intent);
                jumpTv.setVisibility(View.INVISIBLE);
                ps.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mShakeListener = new ApplyShakeListener(ApplyShakeActivity.this);
        //开始 摇一摇手掌动画
        listener = new ApplyShakeListener.OnShakeListener() {
            public void onShake() {
                rg_distance.setVisibility(View.INVISIBLE);
                jumpTv.setVisibility(View.INVISIBLE);
                ps.setVisibility(View.INVISIBLE);
                if (mLoadingLl.getVisibility() == View.VISIBLE) {
                    return;
                }
                setEnabledRadioGroup(false);
                tv_distance.setEnabled(false);
                startAnim();  //开始 摇一摇手掌动画
                mShakeListener.stop();
                startVibrato(); //开始 震动
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivLineDown.setVisibility(View.INVISIBLE);
                        ivLineUp.setVisibility(View.INVISIBLE);
                        tv_loading.setText("正在搜索附近" + shakeName);
                        mLoadingLl.setVisibility(View.VISIBLE);
                        mVibrator.cancel();
                        mShakeListener.start();
                        ivProgressSpinner.startAnimation(refreshingAnimation);
                        if (!isWifi) {
                            shakeGetData();
                            //114.297953,30.537129   武昌分局地址
                        }
                    }
                }, 2000);
            }
        };
        tv_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_distance.setVisibility(View.VISIBLE);
            }
        });
        rg_shake_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_camera) {
                    shakeName = "摄像头";
                    shakeType = 1;

                } else if (checkedId == R.id.rbtn_policeroom) {
                    shakeName = "监控室";
                    shakeType = 2;

                } else if (checkedId == R.id.rbtn_basestation) {
                    shakeName = "基站";
                    shakeType = 3;

                }
                jumpTv.setVisibility(View.INVISIBLE);
            }
        });

        rg_distance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbtn_50) {
                    tv_distance.setText("50 M");
                    distance = "50";

                } else if (checkedId == R.id.rbtn_100) {
                    tv_distance.setText("100 M");
                    distance = "100";

                } else if (checkedId == R.id.rbtn_200) {
                    tv_distance.setText("200 M");
                    distance = "200";

                } else if (checkedId == R.id.rbtn_500) {
                    tv_distance.setText("500 M");
                    distance = "500";

                }
                rg_distance.setVisibility(View.INVISIBLE);
            }
        });

    }

    //摇一摇网络请求
    private void shakeGetData() {
        shakeHttp.shake(mContext, mAlarm, mToken + "token"/* GlobalUserInfo.getToken(mContext)*/,
                String.valueOf(latitue), String.valueOf(longitude), String.valueOf(shakeType), distance,
                new ActionCallbackListener<List<ShakeCameraModel>>() {
                    @Override
                    public void onSuccess(List<ShakeCameraModel> data) {
                        setEnabledRadioGroup(true);
                        tv_distance.setEnabled(true);
                        mLoadingLl.setVisibility(View.INVISIBLE);
                        MediaPlayer player = MediaPlayer.create(mContext, R.raw.shake_match);
                        player.setLooping(false);
                        player.start();
                        ivProgressSpinner.clearAnimation();
                        mData.clear();
                        if (data != null && data.size() > 0) {
                            mData.addAll(data);
                            jumpTv.setText("附近有" + data.size() + "个" + shakeName);
                            jumpTv.setEnabled(true);
                        } else {
                            jumpTv.setText("附近没有" + shakeName);
                            jumpTv.setEnabled(false);
                        }
                        jumpTv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        setEnabledRadioGroup(true);
                        tv_distance.setEnabled(true);
                        ivProgressSpinner.clearAnimation();
                        mLoadingLl.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "连接服务器失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mShakeListener.setOnShakeListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mShakeListener.setOnShakeListener(listener);
    }

    private void initAnimation() {
        mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                this.getApplicationContext(), R.anim.pullrefresh_rotting_anim);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
    }

    private void initView() {
        mImgUp = findViewById(R.id.shakeImgUp);
        mImgDn = findViewById(R.id.shakeImgDown);
        mLoadingLl = findViewById(R.id.ll_loading);
        jumpTv = findViewById(R.id.tv_jump);
        tv_loading = findViewById(R.id.tv_loading);
        ivProgressSpinner = findViewById(R.id.imageview_progress_spinner);
        tv_distance = findViewById(R.id.tv_distance);
        rg_distance = findViewById(R.id.rg_distance);
        rg_shake_type = findViewById(R.id.rg_shake_type);
        ivLineUp = findViewById(R.id.img_line_top);
        ivLineDown = findViewById(R.id.img_line_down);
        rbtnWifi = findViewById(R.id.rbtn_wifi);
        ps = findViewById(R.id.ps);
    }

    private void initLocation() {
        LocationMapTask locationTask = LocationMapTask.getInstance(this);
        locationTask.setOnLocationGetListener(this);
    }

    @Override
    public void onLocationGet(PositionEntity entity) {
        latitue = entity.latitue;
        longitude = entity.longitude;
        Logger.d("dong", "经纬度: " + latitue + "  " + longitude);
    }

    @Override
    public void onRegecodeGet(PositionEntity entity) {

    }

    /**
     * 设置RadioGroup是否可选择
     *
     * @param enabled false不可选择  true可选择
     */
    private void setEnabledRadioGroup(boolean enabled) {
        for (int i = 0; i < rg_shake_type.getChildCount(); i++) {
            rg_shake_type.getChildAt(i).setEnabled(enabled);
        }
    }

    /**
     * 定义摇一摇动画动画
     */
    public void startAnim() {
        ivLineUp.setVisibility(View.VISIBLE);
        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        mytranslateanimup0.setDuration(500);
        TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        mytranslateanimup1.setDuration(500);
        mytranslateanimup1.setStartOffset(1000);
        animup.addAnimation(mytranslateanimup0);
        animup.addAnimation(mytranslateanimup1);
        mImgUp.startAnimation(animup);

        ivLineDown.setVisibility(View.VISIBLE);
        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        mytranslateanimdn0.setDuration(500);
        TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        mytranslateanimdn1.setDuration(500);
        mytranslateanimdn1.setStartOffset(1000);
        animdn.addAnimation(mytranslateanimdn0);
        animdn.addAnimation(mytranslateanimdn1);
        mImgDn.startAnimation(animdn);
    }

    /**
     * 设置声音和振动
     */
    @SuppressLint("MissingPermission")
    private void startVibrato() {
        MediaPlayer player = MediaPlayer.create(this, R.raw.shake_sound_male);
        player.setLooping(false);
        player.start();

        //定义震动
        mVibrator.vibrate(new long[]{500, 200, 500, 200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
    }


    @Override
    protected void onDestroy() {
        if (shakeTask != null && shakeTask.getStatus() != AsyncTask.Status.FINISHED)
            shakeTask.cancel(true);
        if (mShakeListener != null) {
            mShakeListener.stop();
        }
        super.onDestroy();
    }
}
