package com.gcstorage.app.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.ArrayMap;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.ServerException;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.retrofit.observer.RequestBaseObserver;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.camera.CameraUtils;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.fy.baselibrary.utils.notify.T;
import com.fy.luban.Luban;
import com.gcstorage.app.main.utills.Utils;
import com.gcstorage.app.main.view.ViewfinderView;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.bean.UploadFileEntity;
import com.gcstorage.parkinggather.request.ApiService;
import com.wintone.plateid.PlateCfgParameter;
import com.wintone.plateid.PlateRecognitionParameter;
import com.wintone.plateid.RecogService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称：plate_id_sample_service 类名称：MemoryCameraActivity 类描述： 视频扫描界面 扫描车牌并识别
 * （与视频流的拍照识别同一界面） 创建人：张志朋 创建时间：2016-1-29 上午10:55:28 修改人：user 修改时间：2016-1-29
 * 上午10:55:28 修改备注：
 */
public class MemoryCameraActivity extends FragmentActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private String[] fieldvalue = new String[14];
    int[] fieldname = {R.string.plate_number, R.string.plate_color,
            R.string.plate_color_code, R.string.plate_type_code,
            R.string.plate_reliability, R.string.plate_brightness_reviews,
            R.string.plate_move_orientation, R.string.plate_leftupper_pointX,
            R.string.plate_leftupper_pointY, R.string.plate_rightdown_pointX,
            R.string.plate_rightdown_pointY, R.string.plate_elapsed_time,
            R.string.plate_light, R.string.plate_car_color};


    private Camera camera;
    private SurfaceView surfaceView;
    private static final String PATH = Environment
            .getExternalStorageDirectory().toString() + "/DCIM/Camera/";
    // private TextView resultEditText;
    private ImageView take_pic;
    private TextView back;
    private CheckBox flash_camera;
    private ViewfinderView myview;
    private RelativeLayout re;
    private int width, height;
    private TimerTask timer;
    private int preWidth = 0;
    private int preHeight = 0;
    private String number = "", color = "";
    private SurfaceHolder holder;
    private int iInitPlateIDSDK = -1;
    private int nRet = -1;
    private int imageformat = 6;// NV21 -->6
    private int bVertFlip = 0;
    private int bDwordAligned = 1;

    private int rotation = 0;
    private static int tempUiRot = 0;
    private Bitmap bitmap, bitmap1;
    private Vibrator mVibrator;
    private PlateRecognitionParameter prp = new PlateRecognitionParameter();

    private boolean setRecogArgs = true;// 刚进入此界面后对识别车牌函数进行参数设置
    private boolean isCamera;// 判断是预览识别还是视频识别 true:视频识别 false:预览识别
    private boolean recogType;// 记录进入此界面时是拍照识别还是视频识别 true:视频识别 false:拍照识别
    private byte[] tempData;
    private byte[] picData;
    private Timer time;
    private boolean cameraRecogUtill = false; // cameraRecogUtill
    // true:拍照识别采用拍摄照片（整图）根据路径识别，不受扫描框限制
    // false:采用视频流 单帧识别模式 识别扫描框内的车牌
    private String path;// 圖片保存的路徑
    public RecogService.MyBinder recogBinder;
    private boolean isAutoFocus = true; // 是否开启自动对焦 true:开启，定时对焦 false:不开起
    // ，只在图片模糊时对焦
    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.MyBinder) service;
            iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();

            if (iInitPlateIDSDK != 0) {
                nRet = iInitPlateIDSDK;
                String[] str = {"" + iInitPlateIDSDK};
                getResult(str);
            }
            // recogBinder.setRecogArgu(recogPicPath, imageformat,
            // bGetVersion, bVertFlip, bDwordAligned);
            PlateCfgParameter cfgparameter = new PlateCfgParameter();
            cfgparameter.armpolice = 4;
            cfgparameter.armpolice2 = 16;
            cfgparameter.embassy = 12;
            cfgparameter.individual = 0;
            // cfgparameter.nContrast = 9;
            cfgparameter.nOCR_Th = 0;
            cfgparameter.nPlateLocate_Th = 5;
            cfgparameter.onlylocation = 15;
            cfgparameter.tworowyellow = 2;
            cfgparameter.tworowarmy = 6;
            cfgparameter.szProvince = "";
            cfgparameter.onlytworowyellow = 11;
            cfgparameter.tractor = 8;
            cfgparameter.bIsNight = 1;
            if (cameraRecogUtill) {
                imageformat = 0;
            }
            recogBinder.setRecogArgu(cfgparameter, imageformat, bVertFlip,
                    bDwordAligned);

            // fieldvalue = recogBinder.doRecog(recogPicPath, width,
            // height);

        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            re.removeView(myview);
            setRotationAndView(msg.what);
            re.addView(myview);
            initCamera(holder, rotation);
            super.handleMessage(msg);
        }
    };

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiRot = getWindowManager().getDefaultDisplay().getRotation();// 获取屏幕旋转的角度
        System.out.println("旋转角度——————" + uiRot);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_carmera);

        isCamera = getIntent().getBooleanExtra("camera", false);

        recogType = getIntent().getBooleanExtra("camera", false);
        if (isCamera) {
            if (cameraRecogUtill) {
                cameraRecogUtill = false;
            }
        }
        RecogService.initializeType = recogType;

        findiew();
        setRotationAndView(uiRot);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBatInfoReceiver, filter);
    }

    // 设置相机取景方向和扫面框
    private void setRotationAndView(int uiRot) {
        setScreenSize(this);
        System.out.println("屏幕宽：" + width + "     屏幕高：" + height);
        rotation = Utils.setRotation(width, height, uiRot, rotation);
        System.out.println("rotation------" + rotation);
        if (rotation == 90 || rotation == 270) // 竖屏状态下
        {
            myview = new ViewfinderView(MemoryCameraActivity.this, width,
                    height, false);
            setLinearButton();
        } else { // 横屏状态下
            myview = new ViewfinderView(MemoryCameraActivity.this, width,
                    height, true);
            setHorizontalButton();
        }
    }

    @SuppressLint("NewApi")
    private void findiew() {
        surfaceView = findViewById(R.id.surfaceViwe_video);
        flash_camera = findViewById(R.id.flash_camera);
        back = findViewById(R.id.back);
        take_pic = findViewById(R.id.take_pic_btn);
        re = findViewById(R.id.memory);
        // hiddenVirtualButtons(re);
        holder = surfaceView.getHolder();
        holder.addCallback(MemoryCameraActivity.this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (isCamera) {
            take_pic.setVisibility(View.GONE);
        } else {
            take_pic.setVisibility(View.VISIBLE);
        }

        // 竖屏状态下返回按钮
        back.setOnClickListener(arg0 -> {
            closeCamera();
            finish();
        });

        // 闪光灯监听事件
        flash_camera.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                CameraUtils.openFlashLight(camera);
                flash_camera.setText("轻触关闭");
            } else {
                CameraUtils.closeFlashLight(camera);
                flash_camera.setText("轻触打开");
            }
        });

        // 拍照按钮
        take_pic.setOnClickListener(arg0 -> isCamera = true);
    }

    // 设置竖屏方向按钮布局
    private void setLinearButton() {
        int back_w;
        int back_h;
        int flash_w;
        int flash_h;
        int Fheight;
        int take_h;
        int take_w;
        RelativeLayout.LayoutParams layoutParams;
        back.setVisibility(View.VISIBLE);
        back_h = (int) (height * 0.066796875);
        back_w = (int) (back_h * 1);
        layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);

        Fheight = (int) (width * 0.75);
        layoutParams.topMargin = (int) (((height - Fheight * 0.8 * 1.585) / 2 - back_h) / 2);
        layoutParams.leftMargin = (int) (width * 0.10486111111111111111111111111111);
//        back.setLayoutParams(layoutParams);

        flash_h = (int) (height * 0.066796875);
        flash_w = (int) (flash_h * 1);
        layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);

        Fheight = (int) (width * 0.75);
        layoutParams.topMargin = (int) (((height - Fheight * 0.8 * 1.585) / 2 - flash_h) / 2);
        layoutParams.rightMargin = (int) (width * 0.10486111111111111111111111111111);
//        flash_btn.setLayoutParams(layoutParams);

        take_h = (int) (height * 0.105859375);
        take_w = (int) (take_h * 1);
        layoutParams = new RelativeLayout.LayoutParams(take_w, take_h);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);

        layoutParams.bottomMargin = (int) (width * 0.10486111111111111111111111111111);
//        take_pic.setLayoutParams(layoutParams);
    }

    // 设置横屏屏方向按钮布局
    private void setHorizontalButton() {
        int back_w;
        int back_h;
        int flash_w;
        int flash_h;
        int Fheight;
        int take_h;
        int take_w;
        RelativeLayout.LayoutParams layoutParams;
        back.setVisibility(View.GONE);
        back_w = (int) (width * 0.066796875);
        back_h = (int) (back_w * 1);
        layoutParams = new RelativeLayout.LayoutParams(back_w, back_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        Fheight = height;

        Fheight = (int) (height * 0.75);
        layoutParams.leftMargin = (int) (((width - Fheight * 0.8 * 1.585) / 2 - back_h) / 2);
        layoutParams.bottomMargin = (int) (height * 0.10486111111111111111111111111111);
//        back_btn.setLayoutParams(layoutParams); todo

        flash_w = (int) (width * 0.066796875);
        flash_h = (int) (flash_w * 1);
        layoutParams = new RelativeLayout.LayoutParams(flash_w, flash_h);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                RelativeLayout.TRUE);

        Fheight = (int) (height * 0.75);
        layoutParams.leftMargin = (int) (((width - Fheight * 0.8 * 1.585) / 2 - back_h) / 2);
        layoutParams.topMargin = (int) (height * 0.10486111111111111111111111111111);
        flash_camera.setLayoutParams(layoutParams);

        take_h = (int) (width * 0.105859375);
        take_w = (int) (take_h * 1);
        layoutParams = new RelativeLayout.LayoutParams(take_w, take_h);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
                RelativeLayout.TRUE);

        layoutParams.rightMargin = (int) (height * 0.10486111111111111111111111111111);
        take_pic.setLayoutParams(layoutParams);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            camera.setPreviewDisplay(holder);
            if (timer == null) {
                timer = new TimerTask() {
                    public void run() {
                        // isSuccess=false;
                        if (camera != null) {
                            try {
                                camera.autoFocus(new AutoFocusCallback() {
                                    public void onAutoFocus(boolean success,
                                                            Camera camera) {
                                        // isSuccess=success;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
            time = new Timer();
            time.schedule(timer, 500, 2500);
            initCamera(holder, rotation);
            re.addView(myview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
        }
    }



    int nums = -1;
    int switchs = -1;
    private byte[] intentNV21data;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // 实时监听屏幕旋转角度
        int uiRot = getWindowManager().getDefaultDisplay().getRotation();// 获取屏幕旋转的角度
        if (uiRot != tempUiRot) {
            //System.err.println("uiRot:" + uiRot);
            Message mesg = new Message();
            mesg.what = uiRot;
            handler.sendMessage(mesg);
            tempUiRot = uiRot;
        }

        if (setRecogArgs) {
            Intent authIntent = new Intent(MemoryCameraActivity.this,
                    RecogService.class);
            bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
            setRecogArgs = false;
        }
        if (iInitPlateIDSDK == 0) {
            prp.height = preHeight;//
            prp.width = preWidth;//
            // 开发码
            prp.devCode = Devcode.DEVCODE;

            if (cameraRecogUtill) {
                // 拍照识别 在使用根据图片路径识别时 执行下列代码
                if (isCamera) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Config.ARGB_8888;
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21,
                            preWidth, preHeight, null);
                    yuvimage.compressToJpeg(
                            new Rect(0, 0, preWidth, preHeight), 100, baos);
                    bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(),
                            0, baos.size(), options);
                    Matrix matrix = new Matrix();
                    matrix.reset();
                    if (rotation == 90) {
                        matrix.setRotate(90);
                    } else if (rotation == 180) {
                        matrix.setRotate(180);
                    } else if (rotation == 270) {
                        matrix.setRotate(270);
                        //
                    }
                    bitmap1 = Bitmap
                            .createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                    bitmap.getHeight(), matrix, true);
                    path = savePicture(bitmap1);
                    prp.pic = path;
                    fieldvalue = recogBinder.doRecogDetail(prp);
                    nRet = recogBinder.getnRet();
                    if (nRet != 0) {
                        feedbackWrongCode();
                    } else {
                        number = fieldvalue[0];
                        color = fieldvalue[1];
                        mVibrator = (Vibrator) getApplication()
                                .getSystemService(Service.VIBRATOR_SERVICE);
                        mVibrator.vibrate(100);
//                        closeCamera();
                        // 此模式下跳转 请到MemoryResultActivity 更改下代码 有注释注意查看
//                        Intent intent = new Intent(MemoryCameraActivity.this, MemoryResultActivity.class);
//                        intent.putExtra("number", number);
//                        intent.putExtra("color", color);
//                        intent.putExtra("path", path);
//                        // intent.putExtra("time", fieldvalue[11]);
//                        intent.putExtra("recogType", false);
//                        startActivity(intent);
//                        MemoryCameraActivity.this.finish();
                        showResultDialog(number, color, fieldvalue[13]);
                    }
                }
            } else {
                // System.out.println("视频流识别模式");
                prp.picByte = data;
                picData = data;
                if (rotation == 0) {
                    // 通知识别核心,识别前图像应先旋转的角度
                    prp.plateIDCfg.bRotate = 0;
                    setHorizontalRegion();
                } else if (rotation == 90) {

                    prp.plateIDCfg.bRotate = 1;
                    setLinearRegion();

                } else if (rotation == 180) {
                    prp.plateIDCfg.bRotate = 2;
                    setHorizontalRegion();
                } else if (rotation == 270) {
                    prp.plateIDCfg.bRotate = 3;
                    setLinearRegion();
                }
                if (isCamera) {
                    // 进行授权验证 并开始识别
                    fieldvalue = recogBinder.doRecogDetail(prp);

                    nRet = recogBinder.getnRet();

                    if (nRet != 0) {
                        String[] str = {"" + nRet};
                        getResult(str);
                    } else {
                        getResult(fieldvalue);
                        intentNV21data = data;
                    }

                }
            }
        }
    }

    // 设置横屏时的识别区域
    private void setHorizontalRegion() {
        prp.plateIDCfg.left = preWidth / 2 - myview.length * preHeight / height;
        prp.plateIDCfg.right = preWidth / 2 + myview.length * preHeight
                / height;
        prp.plateIDCfg.top = preHeight / 2 - myview.length * preHeight / height;
        prp.plateIDCfg.bottom = preHeight / 2 + myview.length * preHeight
                / height;
    }

    // 设置竖屏时的识别区域
    private void setLinearRegion() {
        prp.plateIDCfg.left = preHeight / 2 - myview.length * preWidth / height;
        prp.plateIDCfg.right = preHeight / 2 + myview.length * preWidth
                / height;
        prp.plateIDCfg.top = preWidth / 2 - myview.length * preWidth / height;
        prp.plateIDCfg.bottom = preWidth / 2 + myview.length * preWidth
                / height;
    }

    /**
     * @param @param holder
     * @param @param r 相机取景方向
     * @return void 返回类型
     * @throws
     * @Title: initCamera
     * @Description: TODO(初始化相机)
     */
    @TargetApi(14)
    private void initCamera(SurfaceHolder holder, int r) {
        Camera.Parameters parameters = camera.getParameters();
        List<Size> list = parameters.getSupportedPreviewSizes();
        Size size;
        int length = list.size();
        int previewWidth = 480;
        int previewheight = 640;
        int second_previewWidth = 0;
        int second_previewheight = 0;
        if (length == 1) {
            size = list.get(0);
            previewWidth = size.width;
            previewheight = size.height;
        } else {
            for (int i = 0; i < length; i++) {
                size = list.get(i);
                // System.out.println("宽   "+size.width+"   高"+size.height);
                if ((width < height && height * 3 == width * 4)
                        || (width > height && width * 3 == height * 4)) {
                    if (size.height <= 960 || size.width <= 1280) {

                        second_previewWidth = size.width;
                        second_previewheight = size.height;

                        if (previewWidth <= second_previewWidth
                                && second_previewWidth * 3 == second_previewheight * 4) {
                            previewWidth = second_previewWidth;
                            previewheight = second_previewheight;
                        }

                    }
                } else {
                    if ((width < height && height * 9 == width * 16)
                            || (width > height && width * 9 == height * 16)) {
                        if ((size.height <= 960 || size.width <= 1280)
                                && size.width * 9 == size.height * 16) {

                            second_previewWidth = size.width;
                            second_previewheight = size.height;
                            if (previewWidth <= second_previewWidth) {
                                previewWidth = second_previewWidth;
                                previewheight = second_previewheight;
                            }
                        }
                    } else {
                        if (size.height <= 960 || size.width <= 1280) {

                            second_previewWidth = size.width;
                            second_previewheight = size.height;
                            if (previewWidth <= second_previewWidth) {
                                previewWidth = second_previewWidth;
                                previewheight = second_previewheight;
                            }
                        }
                    }

                }
            }
        }
        preWidth = previewWidth;
        preHeight = previewheight;
        // preWidth = 960;
        // preHeight = 540;
        System.out.println("预览分辨率：" + preWidth + "    " + preHeight);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPreviewSize(preWidth, preHeight);

        if (parameters.getSupportedFocusModes().contains(
                parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                && !isAutoFocus) {
            isAutoFocus = false;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else
            // 5.1系统 因对焦问题程序崩溃解决办法
            if (parameters.getSupportedFocusModes().contains(
                    parameters.FOCUS_MODE_AUTO)) {
                isAutoFocus = true;
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        // parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // setDispaly(parameters,camera);
        // parameters.setExposureCompensation(0);

        camera.setParameters(parameters);
        if (rotation == 90 || rotation == 270) {
            if (width < 1080) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
            }
        } else {
            if (height < 1080) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
            }
        }

        camera.setDisplayOrientation(r);

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setPreviewCallback(MemoryCameraActivity.this);
        camera.startPreview();
    }


    /**
     * @param @param fieldvalue 调用识别接口返回的数据
     * @return void 返回类型
     * @Title: getResult
     * @Description: TODO(获取结果)
     * @throwsbyte[]picdata
     */
    private void getResult(String[] fieldvalue) {
        if (nRet != 0) {
            // 未通过验证 将对应错误码返回
            feedbackWrongCode();
        } else {
            // 通过验证 获取识别结果
            String result = "";
            String[] resultString;
            String timeString = "";
            String boolString = "";
            boolString = fieldvalue[0];

            if (boolString != null && !boolString.equals("")) {
                // 检测到车牌后执行下列代码
                resultString = boolString.split(";");
                int lenght = resultString.length;
                // Log.e("DEBUG", "nConfidence:" +
                // fieldvalue[4]);
                if (lenght > 0) {
                    String[] strarray = fieldvalue[4].split(";");
                    // 静态识别下 判断图像清晰度是否大于75
                    if (recogType ? true : Integer.valueOf(strarray[0]) > 75) {

                        tempData = recogBinder.getRecogData();

                        if (tempData != null) {

                            if (lenght == 1) {

                                if (fieldvalue[11] != null
                                        && !fieldvalue[11].equals("")) {
                                    int time = Integer.parseInt(fieldvalue[11]);
                                    time = time / 1000;
                                    timeString = "" + time;
                                } else {
                                    timeString = "null";
                                }

                                if (null != fieldname) {
                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inPreferredConfig = Config.ARGB_8888;
                                    options.inPurgeable = true;
                                    options.inInputShareable = true;
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    int Height = 0, Width = 0;
                                    if (rotation == 90 || rotation == 270) {
                                        Height = preWidth;
                                        Width = preHeight;
                                    } else if (rotation == 180 || rotation == 0) {
                                        Height = preHeight;
                                        Width = preWidth;
                                    }
                                    YuvImage yuvimage = new YuvImage(tempData,
                                            ImageFormat.NV21, Width, Height,
                                            null);
                                    yuvimage.compressToJpeg(new Rect(0, 0,
                                            Width, Height), 100, baos);

                                    bitmap = BitmapFactory.decodeByteArray(
                                            baos.toByteArray(), 0, baos.size(),
                                            options);

                                    bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
                                            bitmap.getWidth(),
                                            bitmap.getHeight(), null, true);
                                    path = savePicture(bitmap1);

                                    mVibrator = (Vibrator) getApplication()
                                            .getSystemService(
                                                    Service.VIBRATOR_SERVICE);
                                    mVibrator.vibrate(100);

                                    number = fieldvalue[0];
                                    color = fieldvalue[1];
//                                    closeCamera();

                                    showResultDialog(number, color, fieldvalue[13]);
//                                    Intent intent = new Intent( MemoryCameraActivity.this, MemoryResultActivity.class);
//                                    int left = Integer.valueOf(fieldvalue[7]);
//                                    int top = Integer.valueOf(fieldvalue[8]);
//                                    int w = Integer.valueOf(fieldvalue[9])
//                                            - Integer.valueOf(fieldvalue[7]);
//                                    int h = Integer.valueOf(fieldvalue[10])
//                                            - Integer.valueOf(fieldvalue[8]);
//                                    intent.putExtra("number", number);
//                                    intent.putExtra("color", color);
//                                    intent.putExtra("path", path);
//                                    intent.putExtra("left", left);
//                                    intent.putExtra("top", top);
//                                    intent.putExtra("width", w);
//                                    intent.putExtra("height", h);
//                                    intent.putExtra("time", fieldvalue[11]);
//                                    intent.putExtra("recogType", recogType);
//                                    new FrameCapture(intentNV21data, preWidth,
//                                            preHeight, "10");
//                                    startActivity(intent);
//                                    MemoryCameraActivity.this.finish();
                                }
                            } else {
                                String itemString = "";

                                mVibrator = (Vibrator) getApplication()
                                        .getSystemService(
                                                Service.VIBRATOR_SERVICE);
                                mVibrator.vibrate(100);

                                for (int i = 0; i < lenght; i++) {

                                    itemString = fieldvalue[0];
                                    resultString = itemString.split(";");
                                    number += resultString[i] + ";\n";

                                    itemString = fieldvalue[1];
                                    // resultString
                                    // =
                                    // itemString.split(";");
                                    color += resultString[i] + ";\n";
                                    itemString = fieldvalue[11];
                                    resultString = itemString.split(";");
                                }
                                showResultDialog(number, color, fieldvalue[13]);
//                                closeCamera();
//                                Intent intent = new Intent(
//                                        MemoryCameraActivity.this,
//                                        MemoryResultActivity.class);
//                                intent.putExtra("number", number);
//                                intent.putExtra("color", color);
//                                intent.putExtra("time", resultString);
//                                intent.putExtra("recogType", recogType);
//                                MemoryCameraActivity.this.finish();
//                                startActivity(intent);
                            }
                        }
                    }
                }
            } else {
                // 未检测到车牌时执行下列代码
                if (!recogType) {
                    // 预览识别执行下列代码 不是预览识别 不做处理等待下一帧
                    if (picData != null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Config.ARGB_8888;
                        options.inPurgeable = true;
                        options.inInputShareable = true;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        YuvImage yuvimage = new YuvImage(picData,
                                ImageFormat.NV21, preWidth, preHeight, null);
                        yuvimage.compressToJpeg(new Rect(0, 0, preWidth,
                                preHeight), 100, baos);
                        bitmap = BitmapFactory.decodeByteArray(
                                baos.toByteArray(), 0, baos.size(), options);

                        Matrix matrix = new Matrix();
                        matrix.reset();
                        if (rotation == 90) {
                            matrix.setRotate(90);
                        } else if (rotation == 180) {
                            matrix.setRotate(180);
                        } else if (rotation == 270) {
                            matrix.setRotate(270);
                            //
                        }
                        bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
                                bitmap.getWidth(), bitmap.getHeight(), matrix,
                                true);
                        path = savePicture(bitmap1);

                        if (fieldvalue[11] != null
                                && !fieldvalue[11].equals("")) {
                            int time = Integer.parseInt(fieldvalue[11]);
                            time = time / 1000;
                            timeString = "" + time;
                        } else {
                            timeString = "null";
                        }

                        if (null != fieldname) {
                            mVibrator = (Vibrator) getApplication()
                                    .getSystemService(Service.VIBRATOR_SERVICE);
                            mVibrator.vibrate(100);
//                            closeCamera();

                            number = fieldvalue[0];
                            color = fieldvalue[1];
                            if (fieldvalue[0] == null) {
                                number = "null";
                            }
                            if (fieldvalue[1] == null) {
                                color = "null";
                            }

                            T.showLong("请拍照正确车牌!");
                            isCamera = false;
//                            isDiscern = false;
//                            deletePicFile(path);
                            camera.setPreviewCallback(MemoryCameraActivity.this);
                            camera.startPreview();
//                            Intent intent = new Intent( MemoryCameraActivity.this, MemoryResultActivity.class);
//                            int left = prp.plateIDCfg.left;
//                            int top = prp.plateIDCfg.top;
//                            int w = prp.plateIDCfg.right - prp.plateIDCfg.left;
//                            int h = prp.plateIDCfg.bottom - prp.plateIDCfg.top;
//
//                            intent.putExtra("number", number);
//                            intent.putExtra("color", color);
//                            intent.putExtra("path", path);
//                            intent.putExtra("left", left);
//                            intent.putExtra("top", top);
//                            intent.putExtra("width", w);
//                            intent.putExtra("height", h);
//                            intent.putExtra("time", fieldvalue[11]);
//                            intent.putExtra("recogType", recogType);
//                            MemoryCameraActivity.this.finish();
//                            startActivity(intent);
                        }
                    }
                }
            }
        }

        nRet = -1;
        fieldvalue = null;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }

        }
        if (bitmap1 != null) {
            if (!bitmap1.isRecycled()) {
                bitmap1.recycle();
                bitmap1 = null;
            }

        }

        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (recogBinder != null) {
            unbindService(recogConn);
            recogBinder = null;
        }
        if (mBatInfoReceiver != null) {
            try {
                unregisterReceiver(mBatInfoReceiver);
            } catch (Exception e) {
            }

        }
    }

    public String savePicture(Bitmap bitmap) {
        String strCaptureFilePath = PATH + "plateID_" + pictureName() + ".jpg";
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strCaptureFilePath;
    }

    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date + "_");
        else {
            str = str + String.valueOf(date + "_");
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
        return str;
    }

    /**
     * 获取屏幕真实分辨率，不受虚拟按键影响
     */
    @SuppressLint("NewApi")
    private void setScreenSize(Context context) {
        int x, y;
        WindowManager wm = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else {
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else {
            x = display.getWidth();
            y = display.getHeight();
        }

        width = x;
        height = y;
    }

    //监听锁屏键的开关     因为小米平板在锁屏  解屏时生命周期为 onpause-onresume 锁屏时会自动释放camera onresume时不调用surfaceCreat
    //需要调用surfaceView.setVisibility(View.VISIBLE);才会执行surfacecraet
    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            final String action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) {
//				surfaceCreated(holder);
                surfaceView.setVisibility(View.VISIBLE);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {

                closeCamera();
                surfaceView.setVisibility(View.INVISIBLE);
                re.removeView(myview);
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        re.removeView(myview);
    }

    @Override
    public void onBackPressed() {
        closeCamera();
        super.onBackPressed();
    }

    //关闭相机
    private void closeCamera() {
        synchronized (this) {
            try {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (time != null) {
                    time.cancel();
                    time = null;
                }
                if (camera != null) {
                    camera.setPreviewCallback(null);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }

            } catch (Exception e) {
            }
        }
    }

    //toast 提示 识别错误码
    private void feedbackWrongCode() {
        String nretString = nRet + "";
        if (nretString.equals("-1001")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_readJPG_error));
        } else if (nretString.equals("-10001")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_noInit_function));
        } else if (nretString.equals("-10003")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_validation_faile));
        } else if (nretString.equals("-10004")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_serial_number_null));
        } else if (nretString.equals("-10005")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_disconnected_server));
        } else if (nretString.equals("-10006")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_obtain_activation_code));
        } else if (nretString.equals("-10007")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_noexist_serial_number));
        } else if (nretString.equals("-10008")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_serial_number_used));
        } else if (nretString.equals("-10009")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_unable_create_authfile));
        } else if (nretString.equals("-10010")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_check_activation_code));
        } else if (nretString.equals("-10011")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_other_errors));
        } else if (nretString.equals("-10012")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_not_active));
        } else if (nretString.equals("-10015")) {
            T.showLong(getString(R.string.recognize_result) + nRet + "\n" + getString(R.string.failed_check_failure));
        } else {
            T.showLong(getString(R.string.recognize_result) + nRet);
        }
    }

    //弹窗 显示识别结果
    private void showResultDialog(String number, String color, String carColor){
        camera.setPreviewCallback(null);
        camera.stopPreview();

        NiceDialog.init()
                .setLayoutId(R.layout.dialog_cofig)
                .setDialogConvertListener(new DialogConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, CommonDialog dialog) {

                        holder.getView(R.id.rLayout)
                                .setBackground(ShapeBuilder.create()
                                        .solid(R.color.white)
                                        .stroke(2, R.color.stroke)
                                        .radius(24)
                                        .build());

                        EditText et_car_num = holder.getView(R.id.et_car_num);
                        et_car_num.setText(number);

                        EditText et_car_color = holder.getView(R.id.et_car_color);
                        et_car_color.setText(color);

                        holder.setOnClickListener(R.id.negativeButton, v -> {
                            isCamera = false;
                            dialog.dismiss();
                            camera.setPreviewCallback(MemoryCameraActivity.this);
                            camera.startPreview();
                        });

                        holder.setOnClickListener(R.id.positiveButton, v -> {
                            isCamera = false;
                            dialog.dismiss();
                            camera.setPreviewCallback(MemoryCameraActivity.this);
                            camera.startPreview();

                            List<String> file = new ArrayList<>();
                            file.add(path);
                            uploadFiles(file, number, carColor);
                        });

                    }
                }).setHide(true)
                .show(getSupportFragmentManager(), "dialog_cofig");
    }

    public void uploadFiles(List<String> files, String number, String carColor) {
        Observable.just(files)
                .subscribeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        FileUtils.recursionDeleteFile(new File(FileUtils.getPath("luban", 1)));
                        File lubanPath = FileUtils.folderIsExists("luban", 1);
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(MemoryCameraActivity.this)
                                .load(list)
                                .ignoreBy(100)
                                .setTargetDir(lubanPath.getPath())
                                .get();
                    }
                }).flatMap(new Function<List<File>, ObservableSource<List<UploadFileEntity>>>() {
                    @Override
                    public ObservableSource<List<UploadFileEntity>> apply(List<File> files) throws Exception {
                        ArrayMap<String, Object> params = new ArrayMap<>();
                        params.put("uploadFile", "fileName");
                        params.put("filePathList", files);
                        params.put("UploadOnSubscribe", new UploadOnSubscribe());
                        params.put("alarm", "015176"); // 警号
                        params.put("token", "123"); // token

                        return RequestUtils.create(ApiService.class)
                                .uploadFile(params)
                                .compose(RxHelper.handleResult())
                                .compose(RxHelper.bindToLifecycle(MemoryCameraActivity.this));
                    }
                }).flatMap(new Function<List<UploadFileEntity>, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(List<UploadFileEntity> uploadFileEntities) throws Exception {

                        if (uploadFileEntities.isEmpty()) return Observable.error(new ServerException("图片上传失败", 10001));

                        UploadFileEntity fileEntity = uploadFileEntities.get(0);

                        ArrayMap<String, String> params = new ArrayMap<>();
                        params.put("carNum", number);//车牌号
                        params.put("carColor", carColor);//车辆颜色
                        params.put("longitude", "100.1");//经度
                        params.put("latitude", "99.2");//纬度
                        params.put("address", "江旺路10号");//拍照地址
                        params.put("carImg", fileEntity.getUrl());//车辆图片url
                        params.put("userId", SpfAgent.getString(Constant.baseSpf, Constant.userIdCard));//警察身份证号
                        params.put("name", SpfAgent.getString(Constant.baseSpf, Constant.userName));//警察名称
                        params.put("pic", SpfAgent.getString(Constant.baseSpf, Constant.userImg));//警员头像
                        return RequestUtils.create(ApiService.class)
                                .saveParkingInfo(params)
                                .compose(RxHelper.handleResult())
                                .compose(RxHelper.bindToLifecycle(MemoryCameraActivity.this));
                    }
                }).subscribe(new RequestBaseObserver<Object>() {
                    @Override
                    protected void onSuccess(Object commentBeans) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

}
