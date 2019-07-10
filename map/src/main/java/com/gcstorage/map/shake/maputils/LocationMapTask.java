package com.gcstorage.map.shake.maputils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.gcstorage.framework.utils.Logger;
import com.gcstorage.map.HeartBeatMapService;
import com.ishowmap.api.location.IMLocation;
import com.ishowmap.api.location.IMLocationClient;
import com.ishowmap.api.location.IMLocationClientOption;
import com.ishowmap.api.location.IMLocationListener;
import com.leador.api.maps.LocationSource;
import com.leador.api.services.core.LatLonPoint;
import com.leador.api.services.geocoder.GeocodeSearch;
import com.leador.api.services.geocoder.RegeocodeQuery;

/**
 * 使用的立得Gis地图, 封装的地图定位类 2019/1/22 0022.
 * 如果需要使用高德地图定位类:只需要LocationMapTask  切换成为 LocationTask
 */

public class LocationMapTask implements LocationSource, IMLocationListener {

    private OnLocationChangedListener mListener;
    private static LocationMapTask locationTask;
    private OnLocationGetListener mOnLocationGetlisGetListener;  //定位回调的监听
    private OnLocationListener mLocationListener;
    private LocationManager lManager;
    private Context mContext;
    private static long LOCATION_TIME = 5 * 1000;   //设置定位间隔时间
    //定位成功的实体类
    private PositionEntity entity = new PositionEntity();
    private String mMapSelection = "1";

    public void setOnLocationGetListener(OnLocationGetListener onGetLocationListener) {
        mOnLocationGetlisGetListener = onGetLocationListener;
    }

    public void setOnLocationListener(
            OnLocationListener onGetLocationListener) {
        mLocationListener = onGetLocationListener;
    }

    public static LocationMapTask getInstance(Context context) {
        if (locationTask == null) {
            locationTask = new LocationMapTask(context);
        }
        return locationTask;
    }

    //初始化操作
    public LocationMapTask(Context context) {
        //获取LocationManager
        mContext = context;
        //系统原生的定位
        if (lManager == null) {
            lManager = (LocationManager) context.getApplicationContext()
                    .getSystemService(Context.LOCATION_SERVICE);
        }
        startGISLocation();
    }

    public void setLoctionMode(String type) {
        mMapSelection = type;
    }

    public IMLocationClientOption locationOption; //定位参数设置类,通过这个类可以对定位的相关参数进行设置。
    public IMLocationClient locationClient;       //定位服务类。此类提供单次定位、持续定位、最后位置相关功能。


    //===========================使用立得GIS定位 84坐标系=========================
    private void startGISLocation() {
        Logger.e("dong", "startGISLocation== ");
        locationClient = new IMLocationClient(mContext);
        locationOption = new IMLocationClientOption();
        // 设置定位模式为低功耗模式:http://dev.ishowchina.com/android/location/question.html
        locationOption.setLocationMode(IMLocationClientOption.IMLocationMode.Device_Sensors);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位模式为低功耗模式,设置定位时间
        locationOption.setInterval(5000);
        //设置是否只定位一次 默认值：false
        locationOption.setOnceLocation(false);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationClient.startLocation();
    }

    /**
     * http://dev.ishowchina.com/android/map/guide.html
     * 第一步：设置我的位置图层相关属性用
     * 第二步：启动定位，得到位置注入监听器
     * 第三步，获取到我的位置，显示到地图上
     * @param location
     */

    /**
     * 第二步：启动定位，得到位置注入监听器
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        Logger.e("dong", "我的定位=activate");
        if (locationClient == null) {
            locationClient = new IMLocationClient(mContext.getApplicationContext());
            locationOption = new IMLocationClientOption();
            // 设置定位模式为低功耗模式:http://dev.ishowchina.com/android/location/question.html
            locationOption.setLocationMode(IMLocationClientOption.IMLocationMode.Device_Sensors);
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位模式为低功耗模式,设置定位时间
            locationOption.setInterval(5000);
            //设置是否只定位一次 默认值：false
            locationOption.setOnceLocation(false);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationClient.startLocation();
        }
    }


    @Override
    public void deactivate() {

    }

    /**
     * 销毁定位资源
     */
    public void onDestroy() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationOption = null;
            locationTask = null;
        }
        if (lManager != null) {
            //移除监听器
//            lManager.removeUpdates(mLocationListenerGPS);
        }
    }

    /**
     * 立得地图: 通过经纬度获取地理位置
     * 因为在内网使用gps定位时,无法获取到详细的地理位置信息,所有使用经纬度调用立得的逆编码接口,
     * 获取详情地理位置信息
     * double  va     30.62527478
     * double vb    114.23118119
     */
    public static void reverseCode(Context context, double va, double vb, GeocodeSearch.OnGeocodeSearchListener searchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(searchListener);
        LatLonPoint latLonPoint = new LatLonPoint(va, vb);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint);// 参数表示一个经纬度。
        query.setShowPoi(false);//是否显示指定位置附近的poi
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请
    }

    //使用谷歌提供的原生GPS定位
    @SuppressLint("MissingPermission")
    public void startSingleLocate() {
        /**监视地理位置变化
         * 参1:选择定位的方式
         * 参2:定位的间隔时间
         * 参3:当位置改变多少时进行重新定位
         * 参4:位置的回调监听
         */
        //https://blog.csdn.net/yyywyr/article/details/39063181
        //https://blog.csdn.net/qq_33689414/article/details/54136922
//        Logger.e("dong", "gps_w===========isLocationShare= 5秒定位一次  isLocationShare    lManager");
//        lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                LOCATION_TIME, 0, mLocationListenerGPS);
    }

    /**
     * LocationListern监听器,系统原生定位
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

//    LocationListener mLocationListenerGPS = new LocationListener() {
//        @Override   //当位置改变的时候调用
//        public void onLocationChanged(Location location) {
//
//            //经度
//            double longitude = location.getLongitude();
//            //纬度
//            double latitude = location.getLatitude();
//            entity.latitue = latitude;
//            entity.longitude = longitude;
//            Logger.d("dong", "警务度== " + longitude + " -- " + latitude);
////            reverseCode(mContext, longitude, latitude);
//            if (mOnLocationGetlisGetListener != null) {
//                mOnLocationGetlisGetListener.onLocationGet(entity);
//            }
//            if (mLocationListener != null) {
//                mLocationListener.onLocationGet(entity);
//            }
//
//        }
//
//        @Override   //当GPS状态发生改变的时候调用
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            switch (status) {
//                case LocationProvider.AVAILABLE: //"当前GPS为可用状态!"
//
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:  // "当前GPS不在服务内"
//
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE: //"当前GPS为暂停服务状态"
//
//                    break;
//            }
//        }
//
//        //GPS开启的时候调用
//        @Override
//        public void onProviderEnabled(String provider) {
//            Logger.d("dong", "onProviderEnabled== " + provider);
//        }
//
//        //GPS关闭的时候调用
//        @Override
//        public void onProviderDisabled(String provider) {
//            Logger.d("dong", "onProviderDisabled== " + provider);
//        }
//
//    };
    @Override
    public void onLocationChanged(IMLocation imLocation) {
        Logger.d("dong", "定位数据=" +imLocation.getLongitude() + " " +imLocation.getLatitude() );
        if (null == imLocation) {
            return;
        }
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (mListener != null) {
            mListener.onLocationChanged(imLocation);
        }
        if (imLocation != null) {
            entity.latitue = imLocation.getLatitude();
            entity.longitude = imLocation.getLongitude();
            if (mOnLocationGetlisGetListener != null) {
                mOnLocationGetlisGetListener.onLocationGet(entity);
            }
            if (mLocationListener != null) {
                mLocationListener.onLocationGet(entity);
            }
        }
    }
}
