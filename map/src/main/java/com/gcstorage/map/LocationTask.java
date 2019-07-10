package com.gcstorage.map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 获取定位信息
 * Created by zjs on 2019/2/14.
 */

public class LocationTask {

    private static volatile LocationTask mTask;
    private final AMapLocationClient mLocationClient;

    public static LocationTask getInstance(Context context){
        if(mTask == null){
            synchronized (LocationTask.class){
                if(mTask == null){
                    mTask = new LocationTask(context);
                }
            }
        }
        return mTask;
    }

    private LocationTask(Context context){
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        AMapLocationListener locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation) {
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 0) {
                        LocationInfo info = new LocationInfo();
                        info.setLongitude(aMapLocation.getLongitude());
                        info.setLatitude(aMapLocation.getLatitude());
                        info.setAddress(aMapLocation.getAddress());
                        info.setCity(aMapLocation.getCity());
                        info.setCountry(aMapLocation.getCountry());
                        info.setDistrict(aMapLocation.getDistrict());
                        info.setProvince(aMapLocation.getProvince());
                        info.setStreet(aMapLocation.getStreet());
                        info.setStreetNum(aMapLocation.getStreetNum());
                        if (listener != null) {
                            listener.onLocationGet(info);
                        }
                    } else {
                        if (listener != null) {
                            listener.onLocationError(aMapLocation.getErrorInfo());
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onLocationError("无信息");
                    }
                }
            }
        };
        mLocationClient.setLocationListener(locationListener);
    }

    /**
     * 开启定位
     */
    public void startLocation(){
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setHttpTimeOut(8000);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        if(mLocationClient != null){
            mLocationClient.setLocationOption(option);
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation(){
        if(mLocationClient != null){
            mLocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    public void destroy(){
        if(mLocationClient != null){
            mLocationClient.onDestroy();
            mTask = null;
        }
    }

    /**
     * 定位信息回调
     */
    public interface OnLocationListener{
        /**
         * 获取到的定位信息
         * @param info 定位信息
         */
        void onLocationGet(LocationInfo info);

        /**
         * 定位错误
         * @param errorMsg 错误信息
         */
        void onLocationError(String errorMsg);
    }

    private OnLocationListener listener;

    /**
     * 设置位置回调监听
     */
    public void setOnLocationListener(OnLocationListener listener){
        this.listener = listener;
    }
}
