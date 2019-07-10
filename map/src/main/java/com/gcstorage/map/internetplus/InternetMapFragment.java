package com.gcstorage.map.internetplus;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcstorage.map.BaseMapsFragment;
import com.gcstorage.map.shake.activity.ApplyShakeResultGisActivity;
import com.gcstorage.map.shake.activity.ShakeMapGisFragment;
import com.gcstorage.map.shake.api.ShakeCameraModel;
import com.gcstorage.map.utils.ScreenUtil;
import com.ishowmap.api.location.IMLocation;
import com.jeff.map.R;
import com.leador.api.maps.CameraUpdateFactory;
import com.leador.api.maps.MapController;
import com.leador.api.maps.Projection;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.CameraPosition;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.Marker;
import com.leador.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class InternetMapFragment extends BaseMapsFragment implements MapController.OnMapLoadedListener {

    private Marker mCheckedMarker;
    private List<Marker> mMarkers;
    private List<InternetPlusBean> mData;

    public InternetMapFragment() {
        mMarkers = new ArrayList<>();
        // Required empty public constructor
    }

    public static InternetMapFragment newInstance(List<InternetPlusBean> data) {
        InternetMapFragment fragment = new InternetMapFragment();
        Bundle args = new Bundle();
//        args.putSerializable("data", data);
        args.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = getArguments().getParcelableArrayList("data");
            if (mData != null && mData.size() != 0) {
                Log.e("dong", "地图摄像头数据:" + mData.size());
            }
        }
        moveToCurrentLocation(); //定位到用户的当前
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shake_video_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);//创建地图,很关键
        lMap.setPointToCenter(ScreenUtil.getScreenWidth(getContext()) / 2, ScreenUtil.getScreenHeight(getContext()) / 4);
        lMap.setOnMapLoadedListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mData != null && mData.size() != 0) {
            addMarkers(mData);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onStubLocationChanged(IMLocation amapLocation) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        super.onCameraChangeFinish(cameraPosition);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        super.onInfoWindowClick(marker);
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (lMap != null) {
            moveToCurrentLocation(marker);
            jumpPoint(marker);
        }
        return true;
    }

    /**
     * 定位到当前位置
     *
     * @param marker
     */
    private void moveToCurrentLocation(Marker marker) {
        if (mCheckedMarker != null) {
            mCheckedMarker.remove();
            mCheckedMarker = null;
        }
        String num = (String) marker.getObject();
        LatLng latLng = marker.getPosition();
        lMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        if (TextUtils.isEmpty(num)) {
            return;
        }
        ((MapVideoInletActivity) getActivity()).clickMarker();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.anchor(0.5f, 1f);
        markerOptions.icon(BitmapDescriptorFactory.
                fromView(getMarkerView(R.mipmap.marker_shake_blue, (String) marker.getObject())));
        mCheckedMarker = lMap.addMarker(markerOptions);
    }

    public void moveCheckedMarker(int positon) {
        if (mMarkers != null && mMarkers.size() != 0) {
            moveToCurrentLocation(mMarkers.get(positon));
        }
    }

    /**
     * 把获取到的摄像头信息显示到地图上
     *
     * @param cameras 摄像头数据
     */
    private void addMarkers(List<InternetPlusBean> cameras) {
        if (cameras == null) {
            return;
        }
        mMarkers.clear();
        for (int i = 0; i < cameras.size(); i++) {
            addMarker(cameras.get(i), String.valueOf(i + 1));
        }
    }

    /**
     * 将单个摄像头信息通过标记的形式，添加到地图上
     *
     * @param camera 摄像头数据
     */
    private void addMarker(InternetPlusBean camera, String num) {
        if (camera == null) {
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(Double.valueOf(camera.getLatitude()),
                Double.valueOf(camera.getLongitude()));
        markerOptions.position(latLng);
        markerOptions.anchor(0.5f, 1f);
        markerOptions.icon(BitmapDescriptorFactory.
                fromView(getMarkerView(R.mipmap.marker_shake_red, num)));
        Marker marker = lMap.addMarker(markerOptions);
        marker.setObject(num);
        mMarkers.add(marker);
    }

    /**
     * 获取自定义标记
     *
     * @param pm_val
     * @return
     */
    private View getMarkerView(int imgResId, String pm_val) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.marker_shake, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        iv_icon.setImageResource(imgResId);
        tv_val.setText(pm_val);
        return view;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = lMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void setSearchData(List<InternetPlusBean> searchData) {
        this.mData = searchData;
        mMarkers.clear();
//        mMarkers.remove()
        addMarkers(mData);
    }
}
