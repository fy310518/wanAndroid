package com.gcstorage.scanface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.notify.T;
import com.gcstorage.scanface.bean.CompareResult;
import com.gcstorage.scanface.bean.DrawInfo;
import com.gcstorage.scanface.bean.FacePreviewInfo;
import com.gcstorage.scanface.bean.LgCodeBean;
import com.gcstorage.scanface.bean.LgTokenBean;
import com.gcstorage.scanface.bean.LgUserInfoBean;
import com.gcstorage.scanface.request.ApiService;
import com.gcstorage.scanface.request.NetCallBack;
import com.gcstorage.scanface.request.NetDialog;
import com.gcstorage.scanface.util.ConfigUtil;
import com.gcstorage.scanface.util.DrawHelper;
import com.gcstorage.scanface.util.ImageUtil;
import com.gcstorage.scanface.util.camera.CameraHelper;
import com.gcstorage.scanface.util.camera.CameraListener;
import com.gcstorage.scanface.util.face.FaceHelper;
import com.gcstorage.scanface.util.face.FaceListener;
import com.gcstorage.scanface.util.face.FaceServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * DESCRIPTION：虹软人脸比对界面
 * Created by fangs on 2019/3/21 15:30.
 */
public class ArcSoftDetectActivity extends AppCompatActivity implements IBaseActivity {
    private static final String TAG = "RegisterAndRecognize";
    private static final int MAX_DETECT_NUM = 10;
    /**
     * 当FR成功，活体未成功时，FR等待活体的时间
     */
    private static final int WAIT_LIVENESS_INTERVAL = 50;
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    /**
     * 优先打开的摄像头
     */
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /** 人脸引擎类，其中定义了人脸操作相关的函数，包含 SDK 的授权激活、引擎初始 化以及人脸处理相关方法*/
    private FaceEngine faceEngine;
    private FaceHelper faceHelper;
    private int afCode = -1;

    /**
     * 活体检测的开关
     */
    private boolean livenessDetect = false;

    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;

    @BindView(R2.id.img)
    ImageView img;
    Bitmap bitmap;

    String idCard = "";//账号，可能是警号可能是身份证

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_register_and_recognize;
    }

    @StatusBar(statusStrColor = "transparent", navStrColor = "transparent", statusOrNavModel = 1)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        //保持亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();

        previewView = findViewById(R.id.texture_preview);
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                activeEngine();//激活引擎
                initEngine();
                initCamera();
            }
        });

//        runNetRequest();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }
            //请求FR的回调
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId) {
                //FR成功
                if (faceFeature != null) {
                    FaceSimilar faceSimilar = new FaceSimilar();
                    int compareResult = faceEngine.compareFaceFeature(Constants.faceFeature, faceFeature, faceSimilar);

                    if (compareResult == ErrorInfo.MOK && faceSimilar.getScore() > 0.6f) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                T.showLong("刷脸比对成功，相似度：" + faceSimilar.getScore());

                            }
                        });
                    }
                }
            }
        };

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror);

                faceHelper = new FaceHelper.Builder()
                        .faceEngine(faceEngine)
                        .frThreadNum(MAX_DETECT_NUM)
                        .previewSize(previewSize)
                        .faceListener(faceListener)
                        .currentTrackId(ConfigUtil.getTrackId(ArcSoftDetectActivity.this.getApplicationContext()))
                        .build();
            }

            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
//                if (faceRectView != null) {
//                    faceRectView.clearFaceInfo();
//                }
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
                        drawInfoList.add(new DrawInfo(facePreviewInfoList.get(i).getFaceInfo().getRect(), GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, LivenessInfo.UNKNOWN,
                                name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));
                    }
//                    drawHelper.draw(faceRectView, drawInfoList);
                }

//                if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
//                    registerStatus = REGISTER_STATUS_PROCESSING;
//
//                    Observable.create(new ObservableOnSubscribe<Boolean>() {
//                        @Override
//                        public void subscribe(ObservableEmitter<Boolean> emitter) {
//                            boolean success = true;
////                            boolean success = FaceServer.getInstance()
////                                    .register(ArcSoftDetectActivity.this, nv21.clone(), previewSize.width, previewSize.height, "registered " + faceHelper.getCurrentTrackId());
//                            emitter.onNext(success);
//                        }
//                    })
//                            .subscribeOn(Schedulers.computation())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<Boolean>() {
//                                @Override
//                                public void onSubscribe(Disposable d) {
//
//                                }
//
//                                @Override
//                                public void onNext(Boolean success) {
//                                    String result = success ? "register success!" : "register failed!";
//                                    Toast.makeText(ArcSoftDetectActivity.this, result, Toast.LENGTH_SHORT).show();
//                                    registerStatus = REGISTER_STATUS_DONE;
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    Toast.makeText(ArcSoftDetectActivity.this, "register failed!", Toast.LENGTH_SHORT).show();
//                                    registerStatus = REGISTER_STATUS_DONE;
//                                }
//
//                                @Override
//                                public void onComplete() {
//
//                                }
//                            });
//                }

//                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {

                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        if (livenessDetect) {
                            livenessMap.put(facePreviewInfoList.get(i).getTrackId(), facePreviewInfoList.get(i).getLivenessInfo().getLiveness());
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求FR（可根据需要添加其他判断以限制FR次数），
                         * FR回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer)}中回传
                         */
                        if (requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == null
                                || requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == Constants.FAILED) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), Constants.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
//                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackId());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
    }

    /**
     * 初始化引擎
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);

        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * 激活引擎
     */
    @SuppressLint("CheckResult")
    public void activeEngine() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(ArcSoftDetectActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer activeCode) throws Exception {
                        if (activeCode == ErrorInfo.MOK) {
                            T.showLong(R.string.active_success);
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            T.showLong(R.string.already_activated);
                        } else {
                            T.showLong(ResUtils.getReplaceStr(R.string.active_failed, activeCode));
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }

        //faceHelper中可能会有FR耗时操作仍在执行，加锁防止crash
        if (faceHelper != null) {
            synchronized (faceHelper) {
                unInitEngine();
            }
            ConfigUtil.setTrackId(this, faceHelper.getCurrentTrackId());
            faceHelper.release();
        } else {
            unInitEngine();
        }
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.dispose();
            getFeatureDelayedDisposables.clear();
        }

        FaceServer.getInstance().unInit();
        super.onDestroy();
    }


    /**
     * 销毁引擎
     */
    private void unInitEngine() {
        if (afCode == ErrorInfo.MOK) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }


    private void init(){
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        idCard = bundle.getString("idCard", "");
    }

}
