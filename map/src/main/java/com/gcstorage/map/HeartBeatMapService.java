package com.gcstorage.map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fy.baselibrary.utils.cache.SpfAgent;
import com.gcstorage.map.shake.maputils.LocationMapTask;
import com.gcstorage.map.shake.maputils.OnLocationGetListener;
import com.gcstorage.map.shake.maputils.PositionEntity;
import com.gcstorage.parkinggather.Constant;
import com.leador.api.services.geocoder.GeocodeResult;
import com.leador.api.services.geocoder.GeocodeSearch;
import com.leador.api.services.geocoder.RegeocodeAddress;
import com.leador.api.services.geocoder.RegeocodeResult;

/**
 * 定位服务心跳包 2019/1/22 0022.
 */
public class HeartBeatMapService extends Service {
    private Context mContext;
    private Runnable mRunnable;
    private Handler handler;

    // 每30s发送心跳
    public static int NOOP_DELAY_MILLIS = 5 * 1000;

    private static final String TAG = "HeartBeatMapService";
    private LocationMapTask mLocationTask;
    private static String mAlarm;
    private static String mToken;

    //启动 定位服务
    public static void startMapService(Context context, String alarm, String token) {
        mAlarm = alarm;
        mToken = token;
        if (SpfAgent.getBoolean(Constant.baseSpf, Constant.addressKEY, true)) {
            Intent mapServiceIntent = new Intent(context, HeartBeatMapService.class);
            context.startService(mapServiceIntent);
        }
    }

    //启动 定位服务
    public static void startMapService(Context context) {
        if (SpfAgent.getBoolean(Constant.baseSpf, Constant.addressKEY, true)) {
            Intent mapServiceIntent = new Intent(context, HeartBeatMapService.class);
            context.startService(mapServiceIntent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mLocationTask = LocationMapTask.getInstance(mContext);
        mLocationTask.setOnLocationGetListener(myOnLocationGetListener);

        Log.e(TAG, "gps_w===========创建服务");
        // 发送心跳，上传经纬度信息
        if (handler == null) {
            handler = new Handler();
        }
        // 隔一段时间再次调用run方法
        mRunnable = new Runnable() {
            @Override
            public void run() {
//                if (code.equals("-1")) T.showLong("定位失败，空旷位置定位信号更好哦~");

                // 发送心跳，上传经纬度信息
                heartBeat();
                // 隔一段时间再次调用run方法
                if (handler != null) {
                    if (!SpfAgent.getBoolean(Constant.baseSpf, Constant.addressKEY, true)) {
//                        stopForeground(true);//停止服务
                        stopSelf();
                    } else {
                        handler.postDelayed(this, NOOP_DELAY_MILLIS);
                    }
                }
            }
        };
        if (handler != null) {
            handler.postDelayed(mRunnable, 0);
        }

//        NotifyUtils.createNotificationChannel(this, "addressService", "定位服务", NotificationManager.IMPORTANCE_LOW,
//                false, false);
//        Notification notification = NotifyUtils.FyBuild.init()
//                .setChannel(3, "addressService")
//                .setIcon(R.drawable.ic_launcher, R.color.appHeadBg)
//                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
//                .createManager(this)
//                .getFBuilder()
//                .build();
//        startForeground(0, notification);//启动为前台服务
    }

    /**
     * 请求接口，发送接口
     */
    private void heartBeat() {
        Log.e(TAG, "gps_w == isLocationShare= 5秒定位一次" + Constant.code + " --- " + Constant.sLongitude + "==" + Constant.sLatitude);
//        mLocationTask.startSingleLocate();
        // 读取本地配置文件，判断是否带上地理位置信息

        String alarm = mAlarm;
        String token = mToken;
        if (alarm == null) {
            Log.e(TAG, "心跳包获取警号未null");
            return;
        }
    }

    private OnLocationGetListener myOnLocationGetListener = new OnLocationGetListener() {
        @Override
        public void onLocationGet(PositionEntity entity) {
            Log.e(TAG, "========address=l======>" + entity);
            Constant.sLatitude = entity.latitue;
            Constant.sLongitude = entity.longitude;

            mLocationTask.reverseCode(HeartBeatMapService.this, Constant.sLatitude, Constant.sLongitude, new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 0) {
                        if (regeocodeResult.getRegeocodeAddressList() != null && regeocodeResult.getRegeocodeAddressList().size() != 0) {
                            RegeocodeAddress addressLD = regeocodeResult.getRegeocodeAddressList().get(0);
                            Constant.code = "0";
                            Constant.address = addressLD.getFormatAddress();
//                            StoreApplication.instance;
                        }
                    }

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    Log.e(TAG, "onGeocodeSearched()");
                }
            });
        }

        @Override
        public void onRegecodeGet(PositionEntity entity) {

        }
    };

    @Override
    public void onDestroy() {
        // 结束定位
        // 销毁定位资源
        mLocationTask.onDestroy();

        // 从Handler中移除Runnable
        if (handler != null) {
            handler.removeCallbacks(mRunnable);
        }
        super.onDestroy();

        //还原数据
        Constant.sLatitude = 0.0; // 纬度
        Constant.sLongitude = 0.0; // 经度
        Constant.address = "未获取到位置信息"; // 经度
        Constant.code = "-1"; // - 1 表示没有获取到经纬度

        Log.e(TAG, "onDestroy()");
    }
}
