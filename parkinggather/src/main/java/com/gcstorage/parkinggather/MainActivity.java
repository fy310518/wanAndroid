package com.gcstorage.parkinggather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fy.baselibrary.application.IBaseActivity;
import com.ishowmap.api.location.IMLocation;
import com.ishowmap.api.location.IMLocationClient;
import com.ishowmap.api.location.IMLocationClientOption;
import com.ishowmap.api.location.IMLocationListener;
import com.leador.api.maps.CameraUpdateFactory;
import com.leador.api.maps.LeadorException;
import com.leador.api.maps.LocationSource;
import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.leador.api.maps.model.LatLng;

/**
 * DESCRIPTION：我秀中国 demo
 * Created by fangs on 2019/7/9 18:53.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity,
        LocationSource, IMLocationListener {

    private MapView mapView;
    private MapController lMap;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.fragment_callpeople;
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.leador_map);
        mapView.onCreate(savedInstanceState);//创建地图
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (lMap == null) {
            try {
                lMap = mapView.getMap();
            } catch (LeadorException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 方法重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    public LocationSource.OnLocationChangedListener mListener;
    public IMLocationClientOption locationOption; //定位参数设置类,通过这个类可以对定位的相关参数进行设置。
    public IMLocationClient locationClient;       //定位服务类。此类提供单次定位、持续定位、最后位置相关功能。

    public static String address = "";
    public static double latitude = -1;
    public static double longitude = -1;
    public static long lastTime = 0L; //刷新经纬度时候也需要刷新此时间(毫秒数)

    private boolean isFirstFocus = false;

    /** 激活定位 */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if(locationClient==null){
            locationClient = new IMLocationClient(this.getApplicationContext());
            locationOption = new IMLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setInterval(2000);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {}

    @Override
    public void onLocationChanged(IMLocation imLocation) {
        if (imLocation != null && imLocation.getErrorCode() != 0) {
            //定位发生异常
            Log.e("onLocationChanged", imLocation.getErrorInfo());
            return;
        }
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (mListener != null && imLocation != null) {
            mListener.onLocationChanged(imLocation);
            //mapView.set
            latitude = imLocation.getLatitude();
            longitude = imLocation.getLongitude();
            float r = imLocation.getAccuracy();
            if (!isFirstFocus) {
                lMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(imLocation.getLatitude(), imLocation.getLongitude())));
                if (r > 200) {
                    lMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                } else {
                    lMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                }
                isFirstFocus = true;
            }
        }
    }
}
