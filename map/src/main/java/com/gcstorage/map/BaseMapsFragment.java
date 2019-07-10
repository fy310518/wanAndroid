package com.gcstorage.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gcstorage.framework.utils.DipPxUtil;
import com.gcstorage.framework.utils.NumberUtils;
import com.ishowmap.api.location.IMLocation;
import com.ishowmap.api.location.IMLocationClient;
import com.ishowmap.api.location.IMLocationClientOption;
import com.ishowmap.api.location.IMLocationListener;
import com.jeff.map.R;
import com.leador.api.maps.CameraUpdateFactory;
import com.leador.api.maps.LeadorException;
import com.leador.api.maps.LocationSource;
import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.leador.api.maps.UiSettings;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.CameraPosition;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.Marker;
import com.leador.api.maps.model.MarkerOptions;
import com.leador.api.maps.model.Polygon;
import com.leador.api.maps.model.PolygonOptions;
import com.leador.api.maps.model.Polyline;
import com.leador.api.maps.model.PolylineOptions;
import com.leador.api.services.core.LatLonPoint;
import com.leador.api.services.geocoder.GeocodeSearch;
import com.leador.api.services.geocoder.RegeocodeQuery;

import java.util.ArrayList;
import java.util.List;

import com.gcstorage.map.shake.maputils.MapGISRes;

/**
 * 抽取立得GIs用户的地图类 2019/1/22 0022.
 */

public abstract class BaseMapsFragment extends Fragment implements LocationSource, IMLocationListener,
        MapController.OnMarkerClickListener, MapController.OnCameraChangeListener, MapController.OnMapClickListener, MapController.OnInfoWindowClickListener {
    public MapController lMap;
    public MapView mapView;
    public LocationSource.OnLocationChangedListener mListener;
    public IMLocationClientOption locationOption; //定位参数设置类,通过这个类可以对定位的相关参数进行设置。
    public IMLocationClient locationClient;       //定位服务类。此类提供单次定位、持续定位、最后位置相关功能。

    public static String address = "";
    public static double latitude = -1;
    public static double longitude = -1;
    public static long lastTime = 0L; //刷新经纬度时候也需要刷新此时间(毫秒数)


    /**
     * 打开轨迹定位
     */
    public final static long OPEN_ROUTE_INTERVAL = 6 * 1000;
    public final static long OPEN_ROUTE_INTERVAL1 = 2 * 6 * 1000;

    /**
     * 定位间隔默认时间
     */
    public final static long DEFAULT_INTERVAL = 30 * 1000;
    public String TAG = "BaseMapsFragment";
    public GeocodeSearch geocoderSearch;    //逆地理编码: 从已知的地理坐标到对应的地址描述（如省市、街道等）的转换。
    private UiSettings uiSettings;
    private boolean mIsLocation = true;

    /**
     * 是否开启轨迹
     */
    protected volatile boolean isRouteOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.leador_map);
        init();
        //setLocationSource();
    }

    /**
     * 初始化地图控制器
     */
    private void init() {
        if (lMap == null) {
            try {
                lMap = mapView.getMap();

                lMap.setMapCenter(new LatLng(30.6131, 114.25713));
                lMap.setOnMarkerClickListener(this);
                lMap.setOnCameraChangeListener(this);
                lMap.setOnInfoWindowClickListener(this);
                lMap.setOnMapClickListener(this);
                lMap.getUiSettings().setZoomControlsEnabled(false);

            } catch (LeadorException e) {
                e.printStackTrace();
            }
        }
        //地理／逆地理编码
        geocoderSearch = new GeocodeSearch(getContext());
    }

    protected void setLocationSource() {
        lMap.setLocationSource(this);// 设置定位监听
        lMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        lMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        lMap.setMyLocationType(MapController.LOCATION_TYPE_LOCATE);
    }

    /**
     * 方法重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public Marker addMakerToMap(LatLng mLatLng) {
        Marker marker = lMap.addMarker(new MarkerOptions().position(mLatLng)
                .title("自定义mark")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.MARKER_COLOR_AZURE))
                .draggable(true));
        return marker;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRouteOpen) {
            startLocate(OPEN_ROUTE_INTERVAL);
        } else {
            startLocate(DEFAULT_INTERVAL);
        }
        Log.d(TAG, "onStart() called with: ");
    }

    /**
     * 方法重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private boolean isFirstFocus = false;

    public void location() {
        if (locationClient != null) {
            isFirstFocus = false;
            locationClient.startAssistantLocation();
        } else {
            Toast.makeText(getActivity(), "没有locationClient", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (locationClient == null) {
            locationClient = new IMLocationClient(getActivity().getApplicationContext());
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

    //定位回调监听，当定位完成后调用此方法。
    @Override
    public void onLocationChanged(IMLocation imLocation) {

        Log.d("dong", "设置定位onLocationChanged========" + imLocation.toString());
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
        onStubLocationChanged(imLocation);
    }


    /**
     * 立得定位后,执行该方法.
     * 定位后位置发生改变给子类回调使用,子类根据需求自己去实现
     *
     * @param location
     */
    public void onStubLocationChanged(IMLocation location) {

    }

    //是否定位到当前的位置
    public void setLocation(boolean b) {
        isFirstFocus = b;
    }

    //停止定位
    public void setStopLocation() {
    }

    /**
     * 定位到当前位置
     */
    public void moveToCurrentLocation() {
        if (longitude == -1 && latitude == -1) {
            Log.e("dong", "定位监听:当前位置 -1 ");
            return;
        }
        Log.e("dong", "定位监听:当前位置 " + longitude + "  " + latitude);
        LatLng latLng = new LatLng(latitude, longitude);
        lMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    /**
     * 开启多次定位
     */
    @SuppressLint("MissingPermission")
    public void startLocate(long interval) {
        Log.d(TAG, "startLocate");
        if (locationClient == null) {
            locationClient = new IMLocationClient(getActivity().getApplicationContext());
            locationOption = new IMLocationClientOption();
            // 设置定位模式为低功耗模式:http://dev.ishowchina.com/android/location/question.html
            locationOption.setLocationMode(IMLocationClientOption.IMLocationMode.Device_Sensors);
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位模式为低功耗模式,设置定位时间
            locationOption.setInterval(interval);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationClient.startLocation();
        }
    }

    /**
     * 结束定位
     */
    public void stopLocate() {
        Log.d(TAG, "stopLocate");
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    //地图上标记点,点击的监听
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    //在可视范围改变完成之后回调此方法。此方法的调用是在主线程中。
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    //在可视范围一系列动作改变完成之后（例如拖动、fling、缩放）回调此方法。此方法的调用是在主线程中。
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        float zoom = cameraPosition.zoom;
        Log.d("dong", "onCameraChangeFinish定位等级==" + zoom);
    }

    //地图点击的监听
    @Override
    public void onMapClick(LatLng latLng) {

    }

    //地图点击之后的弹框
    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    /**
     * 动态修改定位的间隔时间
     *
     * @param interval 间隔时间,单位:毫秒ms
     */
    public void changeLocateInterval(long interval) {
        stopLocate();
        //根据不同的时间选择不同定位方式
        if (interval == DEFAULT_INTERVAL) {
            startLocate(interval);
            isRouteOpen = false;
        } else if (interval == OPEN_ROUTE_INTERVAL) {
//            startRouteLocate(OPEN_ROUTE_INTERVAL);
            startLocate(OPEN_ROUTE_INTERVAL);
            isRouteOpen = true;
        }
    }

    /**
     * 销毁一组marker,加上图片等资源,该对象将被彻底释放
     *
     * @param markers
     */
    public void destroyMarkers(List<Marker> markers) {
        if (markers != null && markers.size() > 0) {
            for (Marker marker : markers) {
                marker.destroy();
            }
        }
    }


    /**
     * 移动到指定坐标位置,并设置缩放值
     *
     * @param latLng
     * @param zoom   缩放值
     */
    public void moveTo(LatLng latLng, float zoom) {
        if (latLng == null) {
            return;
        }
        lMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * 移动到指定坐标位置
     *
     * @param latLng
     */
    public void moveTo(LatLng latLng) {
        lMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 隐藏标记
     *
     * @param marker
     */
    public void hideMark(Marker marker) {
        marker.setVisible(false);
    }


    /**
     * 添加标记点到地图,
     *
     * @param mLatLnglist 传入的标记点经纬度
     * @return 并返回标记的数据
     */
    public List<Marker> addMakerToMaps(List<LatLng> mLatLnglist) {
        if (mLatLnglist == null || mLatLnglist.size() == 0) {
            return null;
        }
        List<Marker> markerList = new ArrayList<>();
        markerList.clear();
        for (int i = 0; i < mLatLnglist.size(); i++) {
            Marker marker = lMap.addMarker(new MarkerOptions().position(mLatLnglist.get(i))
                    .title("自定义mark" + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.MARKER_COLOR_AZURE))
                    .draggable(true));
            markerList.add(marker);
        }
        return markerList;
    }


    /**
     * 从地图删除一组标记
     *
     * @param markers
     */
    public void removeMarkers(List<Marker> markers) {
        if (markers != null && markers.size() > 0) {
            for (Marker marker : markers) {
                marker.remove();
            }
        }
    }

    /**
     * 重新绘制一条线
     *
     * @param polyline
     * @param latLngs
     */
    public void drawPolyline(Polyline polyline, ArrayList<LatLng> latLngs) {
        polyline.setPoints(latLngs);
    }

    /**
     * 改成立得地图了
     * 绘制线:地图上面打点,然后将点连成线. 连线规则是按照打点的顺序来的
     * 线的颜色后期有改动:可以自己定义  18/5/9
     */
    public Polyline drawLine(List<LatLng> lineList) {
        if (lineList.size() == 0) {
            return null;
        }
        //画灰色的线
//        PolylineOptions polylineOptions = new PolylineOptions().addAll(lineList).width(10).
//                color(Color.argb(255, 1, 1, 1));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions
                .width(DipPxUtil.dip2px(getContext(), 5))    //宽度
                .addAll(lineList)
                .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.clue_guiji_blue))
                .color(Color.argb(255, 255, 114, 0));   //颜色ff7200
        return lMap.addPolyline(polylineOptions);
    }


    /**
     * 高德对应说明: http://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-plane
     * 绘制多边形:通过地图标记,将标记连起来, 连线规则是按照打点的顺序来的
     *
     * @param regionList 绘制多边形所需要的点   18/5/9
     *                   线的颜色后期有改动:可以自己定义
     */
    public Polygon drawRadius(List<LatLng> regionList) {
        if (regionList.size() <= 2) {
            return null;
        }
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(regionList);
        polygonOptions.strokeWidth(10) // 多边形的边框
                .strokeColor(Color.argb(100, 100, 100, 100)) // 边框颜色
                .fillColor(Color.argb(100, 100, 100, 100));   // 多边形的填充色
        return lMap.addPolygon(polygonOptions);
    }

    /**
     * 创建经纬度对象 LatLng
     *
     * @param w lat
     * @param h lng
     * @return
     */
    public static LatLng createLatLng(String w, String h) {
        double gps_w = NumberUtils.convertToDouble(w, 0.0);
        double gps_h = NumberUtils.convertToDouble(h, 0.0);
        return new LatLng(gps_w, gps_h);
    }

    /**
     * 创建一条线，画线
     *
     * @return
     */
    public Polyline createPolyline() {
        //线的宽度，3dp转成px
        int widthValue = DipPxUtil.dip2px(getActivity(), 5);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions
                .width((float) widthValue)    //宽度
//                .color(Color.argb(255, 255, 114, 0));   //颜色ff7200
                .setUseTexture(true)
                .setCustomTexture(BitmapDescriptorFactory.fromResource(MapGISRes.clueGuiji[2]));
        return lMap.addPolyline(polylineOptions);
    }

    /**
     * 去导航
     *
     * @param position
     * @param latitude
     * @param longitude
     */
    public void goNavigation(String position, String latitude, String longitude) {
        //TODO 判断本地是否有高德app，获取经纬度，目的地名称
        //判断高德是否存在
        if (isAvilible(getContext(), "com.autonavi.minimap")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //将功能Scheme以URI的方式传入data
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=猎鹰&poiname=" + position + "&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2");
            intent.setData(uri);
            //启动该页面即可
            startActivity(intent);
            //ToastUtils.showShort(getContext(), "导航");
        } else {
            Toast.makeText(getContext(), "您的手机尚未安装高德地图，无法导航", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */

    private static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 创建一条纹理实线(布防圈）
     */
    public Polyline createOrderCustomPolyline(List<LatLng> latlongs) {
        //线的宽度，3dp转成px
//        int widthValue = DipPxUtil.dip2px(getContext(),5);
        int widthValue = 12;
        PolylineOptions polylineOptions = new PolylineOptions();
        Bitmap bitmap = null;
        polylineOptions
                .width((float) widthValue)    //宽度
                .setUseTexture(true)
                .setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.img_map_guard));
        Polyline polyline = lMap.addPolyline(polylineOptions);
        if (latlongs.size() > 0) {
            polyline.setPoints(latlongs);
        }
        return polyline;

    }

    /**
     * 添加标记到地图
     *
     * @param latLng
     * @param iconRes
     * @return
     */
    public Marker addMarkerToMap(LatLng latLng, @DrawableRes int iconRes) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng)
                .title("")
                .snippet("")
                .draggable(false)
                .anchor(0.5f, 0.7f)
                .icon(BitmapDescriptorFactory
                        .fromResource(iconRes));
        Marker marker = lMap.addMarker(markerOption);
        marker.setVisible(true);
        return marker;
    }

    /**
     * 创建一条纹理实线
     */
    public Polyline createCustomPolyline(int type) {
        //线的宽度，3dp转成px
//        int widthValue = DipPxUtil.dip2px(getContext(),5);
        int widthValue = 10;
        PolylineOptions polylineOptions = new PolylineOptions();
        Bitmap bitmap = null;
        polylineOptions
                .width((float) widthValue)    //宽度
                .setUseTexture(true)
                .setCustomTexture(BitmapDescriptorFactory.fromResource(MapGISRes.clueGuiji[type % 5]));
        return lMap.addPolyline(polylineOptions);
    }

    /**
     * 显示标记
     *
     * @param marker
     */
    public void showMark(Marker marker) {
        marker.setVisible(true);
    }

    /**
     * 获取当前位置的地址
     *
     * @param geocodeSearchListener 立得逆编码回调
     */
    public void getCurrentAddress(GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener) {
        Log.d(TAG, "getAddress 运行");
        LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint);// 参数表示一个经纬度。
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);
    }

    /**
     * 根据经纬度获取地址
     *
     * @param latLng                经纬度
     * @param geocodeSearchListener 逆编码回调
     */
    public void getAddress(LatLng latLng, GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener) {
        Log.d(TAG, "getAddress 运行");
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
        geocoderSearch.setOnGeocodeSearchListener(geocodeSearchListener);
    }
}