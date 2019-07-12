package com.gcstorage.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.gcstorage.framework.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjs on 2018/10/17.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private Context mContext;

    private static final int DEFAULT_WIDTH = 720;
    private static final int DEFAULT_HEIGHT = 1280;
    private static final int DEFAULT_CAMERA_ID = 0;
    private static final int SCALE_1_1 = 0;
    private static final int SCALE_4_3 = 1;
    private static final int SCALE_16_9 = 2;

    private int mScreenWidth = DEFAULT_WIDTH;
    private int mScreenHeight = DEFAULT_HEIGHT;
    private SurfaceHolder mHolder;

    private int cameraId = DEFAULT_CAMERA_ID;

    private int[] scale = new int[]{1, 1};

    private Camera mCamera;
    private WindowManager wm;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PreviewScale, defStyleAttr, 0);
        int type = attr.getInt(R.styleable.PreviewScale_scale, 0);
        setScale(type);
        attr.recycle();
        this.mContext = context;
        getScreenMetric(context);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mScreenWidth;
        int height = mScreenWidth * scale[0] / scale[1];
        setMeasuredDimension(width, height);
    }

    private void setScale(int type) {
        switch (type) {
            case SCALE_1_1:
                scale[0] = 1;
                scale[1] = 1;
                break;
            case SCALE_4_3:
                scale[0] = 4;
                scale[1] = 3;
                break;
            case SCALE_16_9:
                scale[0] = 16;
                scale[1] = 9;
                break;
            default:
                scale[0] = 1;
                scale[1] = 1;
                break;
        }
    }

    private void getScreenMetric(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
            mScreenHeight = dm.heightPixels;
            mScreenWidth = dm.widthPixels;
        }
    }

    private void initView() {
        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mHolder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                //此处也可以设置摄像头参数
                Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
                Camera.Size size = getPreviewSize();
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);//设置预览尺寸
                }
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(parameters);
                setCameraDisplayOrientation();
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void focusOnTouch(int x, int y) {
        Rect rect = new Rect(x - 100, y - 100, x + 100, y + 100);
        int left = rect.left * 2000 / getMeasuredWidth() - 1000;
        int top = rect.top * 2000 / getMeasuredHeight() - 1000;
        int right = rect.right * 2000 / getMeasuredWidth() - 1000;
        int bottom = rect.bottom * 2000 / getMeasuredHeight() - 1000;
        // 如果超出了(-1000,1000)到(1000, 1000)的范围，则会导致相机崩溃
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        focusOnRect(new Rect(left, top, right, bottom));
    }


    protected void focusOnRect(Rect rect) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters(); // 先获取当前相机的参数配置对象
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); // 设置聚焦模式
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(rect, 1000));
                parameters.setFocusAreas(focusAreas);
            }
            mCamera.cancelAutoFocus(); // 先要取消掉进程中所有的聚焦功能
            mCamera.setParameters(parameters); // 一定要记得把相应参数设置给相机
            mCamera.autoFocus(this);
        }
    }

    public Camera getCamera() {
        return mCamera;
    }

    private Camera.Size getPreviewSize() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        if (previewSizes == null) {
            return null;
        }
        Camera.Size result = previewSizes.get(0);
        for (Camera.Size size : previewSizes) {
            if (isBest(size.width, size.height)) {
                Log.e("zjs", "w=" + size.width + ",h=" + size.height);
                result = size;
                break;
            }
        }
        for (Camera.Size size : parameters.getSupportedPictureSizes()){
            Log.e("zjs", "w=" + size.width + ",h=" + size.height);
        }
        return result;
    }

    private boolean isBest(int width, int height) {
        return width / scale[0] == height / scale[1];
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = wm.getDefaultDisplay().getRotation();
        //获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 前置摄像头
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // 后置摄像头
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
            mCamera = null;
            mHolder = null;
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }
}
