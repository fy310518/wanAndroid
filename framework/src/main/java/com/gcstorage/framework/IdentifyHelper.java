package com.gcstorage.framework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.widget.Toast;

import com.fri.idcard.IDcardData;
import com.fri.idcard.NfcReadService;
import com.gcstorage.framework.utils.Logger;

import static com.fri.idcard.IDCardErrorCode.MESSAGE_DN_READ_ERROR;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_DN_READ_INFO;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_EXCH_READ_ERROR;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_IDCARD_HANDLE_START;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_IDCARD_READ_TAG_ID;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_IDDATA_DN_QUERY;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_IDDATA_EXCH_QUERY;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_IDDATA_STEP_EXCH;
import static com.fri.idcard.IDCardErrorCode.MESSAGE_SERVICE_INIT;

/**
 * Created by zjs on 2018/11/6.
 */

public class IdentifyHelper {

    private static volatile IdentifyHelper helper;
    private Context context;
    private AudioManager audioManager;
    private NfcReadService mNfcRead;
    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock mWakeLock;

    private static final String TAG = IdentifyHelper.class.getName();

    private IdentifyHelper(Context context) {
        this.context = context;
    }

    public static IdentifyHelper getHelper(Context context) {
        if (helper == null) {
            synchronized (IdentifyHelper.class) {
                if (helper == null) {
                    helper = new IdentifyHelper(context);
                }
            }
        }
        return helper;
    }

    public void initNFC(Handler mHandler) {
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        // 设置身份证读取对象
        mNfcRead = new NfcReadService((Activity) context, mHandler, null);
        // 设备身份证读取的方式
        // setMethod(1), 方法1， 查询基础数据，包括姓名、身份证号、有效期限, 查询失败则通过SAM模块解码
        // setMethod(2), 方法2， 查询身份证的全部信息，如果没有全部信息的情况下，
        //                      则会查询基础信息，如果基础信息也没有，则通过SAM模块解码。
        // setMethod(3), 方法3，通过SAM模块解码身份证信息。
//        mNfcRead.setQueryServerAddr("20.0.10.3","5080");
//        mNfcRead.setExchServerAddr("20.0.10.2","8018");
        mNfcRead.setMethod(3);
        mNfcRead.onCreate();
    }

    public void playMediaEnd() {
        play(R.raw.nfc50);
    }

    public void playMediaMoreStep() {
        play(R.raw.nfc22);
    }

    public void playMediaError() {
        play(R.raw.nfc24);
    }

    /**
     * 播放声音
     *
     * @param res
     */
    private void play(int res) {
        mMediaPlayer = MediaPlayer.create(context, res);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mMediaPlayer.start();
    }

    /**
     * activity onResume 恢复,调用该方法
     */
    public void onResume() {
        if(mNfcRead != null){
            mNfcRead.onResume();
        }
        Logger.d("dong", " 初始化完成，可以读卡, ......");
        PowerManager pManager = ((PowerManager) context.getSystemService(Context.POWER_SERVICE));
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();
    }

    public void onPause(){
        if(mNfcRead != null){
            mNfcRead.onPause();
        }
    }

    /**
     * activity onDestroy 销毁,调用该方法
     */
    public void onDestroy() {
        // 关闭NFC读卡功能
        if (mNfcRead != null) {
            mNfcRead.onDestroy();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

        helper = null;
    }

    /**
     *
     */
    public void keycodeVolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
    }

    /**
     *
     */
    public void keycodeVolumeDown() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
    }
}
