package com.fy.recorder;


import android.annotation.SuppressLint;
import android.content.Context;

import com.fy.baselibrary.utils.notify.L;
import com.fy.recorder.listener.RecordDataListener;
import com.fy.recorder.listener.RecordFftDataListener;
import com.fy.recorder.listener.RecordResultListener;
import com.fy.recorder.listener.RecordSoundSizeListener;
import com.fy.recorder.listener.RecordStateListener;

/**
 * 录音 管理类
 * Created by fangs on 2020/2/17 13:54.
 */
public class RecordManager {
    private static final String TAG = RecordManager.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private volatile static RecordManager instance;
    private Context context;//Application

    private RecordManager() {
    }

    public static RecordManager getInstance() {
        if (instance == null) {
            synchronized (RecordManager.class) {
                if (instance == null) {
                    instance = new RecordManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     * @param application Application
     */
    public void init(Context application) {
        this.context = application;
    }

    public void start() {
        if (context == null) {
            L.e(TAG, "未进行初始化");
            return;
        }
        L.i(TAG, "start...");
        RecordService.startRecording(context);
    }

    public void stop() {
        if (context == null) {
            return;
        }
        RecordService.stopRecording(context);
    }

    public void resume() {
        if (context == null) {
            return;
        }
        RecordService.resumeRecording(context);
    }

    public void pause() {
        if (context == null) {
            return;
        }
        RecordService.pauseRecording(context);
    }

    /**
     * 录音状态监听回调
     */
    public void setRecordStateListener(RecordStateListener listener) {
        RecordService.setRecordStateListener(listener);
    }

    /**
     * 录音数据监听回调
     */
    public void setRecordDataListener(RecordDataListener listener) {
        RecordService.setRecordDataListener(listener);
    }

    /**
     * 录音可视化数据回调，傅里叶转换后的频域数据
     */
    public void setRecordFftDataListener(RecordFftDataListener recordFftDataListener) {
        RecordService.setRecordFftDataListener(recordFftDataListener);
    }

    /**
     * 录音文件转换结束回调
     */
    public void setRecordResultListener(RecordResultListener listener) {
        RecordService.setRecordResultListener(listener);
    }

    /**
     * 录音音量监听回调
     */
    public void setRecordSoundSizeListener(RecordSoundSizeListener listener) {
        RecordService.setRecordSoundSizeListener(listener);
    }


    public boolean changeFormat(RecordConfig.RecordFormat recordFormat) {
        return RecordService.changeFormat(recordFormat);
    }


    public boolean changeRecordConfig(RecordConfig recordConfig) {
        return RecordService.changeRecordConfig(recordConfig);
    }

    public RecordConfig getRecordConfig() {
        return RecordService.getRecordConfig();
    }

    /**
     * 获取当前的录音状态
     *
     * @return 状态
     */
    public RecordHelper.RecordState getState() {
        return RecordService.getState();
    }

}
