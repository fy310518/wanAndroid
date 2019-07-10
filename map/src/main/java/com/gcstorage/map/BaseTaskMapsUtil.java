package com.gcstorage.map;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcstorage.map.utils.Gps;
import com.gcstorage.map.utils.GpsUtils;
import com.gcstorage.map.utils.MapRes;
import com.jeff.map.R;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.Marker;
import com.leador.api.maps.model.MarkerOptions;
import com.leador.api.navi.LeadorNaviListener;
import com.leador.api.navi.model.LeadorLaneInfo;
import com.leador.api.navi.model.LeadorNaviCross;
import com.leador.api.navi.model.LeadorNaviLocation;
import com.leador.api.navi.model.LeadorNaviTrafficFacilityInfo;
import com.leador.api.navi.model.LeadorServiceFacilityInfo;
import com.leador.api.navi.model.NaviDirectionInfo;
import com.leador.api.navi.model.NaviInfo;

import java.util.ArrayList;
import java.util.List;

import com.gcstorage.map.shake.maputils.MarkerEntity;
import com.gcstorage.map.shake.weigh.XCRoundRectImageView;

//LeadorNaviListener  立得导航
public class BaseTaskMapsUtil extends BaseMapsFragment implements LeadorNaviListener {
    /**
     * 地图上面标记人员信息
     *
     * @param latLng 经纬度对象
     * @param i      图标类型,后期可以根据需求自己添加
     * @return
     */
    public Marker drawMember(LatLng latLng, int i) {
        int res = MapRes.tabRes[i];
        //根据类型选择不同的地图资源
        return addMarkerToMap(latLng, res);
    }


    /**
     * 将一批点绘制在地图上面
     *
     * @param latLngList
     * @param i
     * @return
     */
    public List<Marker> drawMemberList(List<LatLng> latLngList, int i) {
        List<Marker> listMarker = new ArrayList<>();
        listMarker.clear();
        int res = MapRes.tabRes[i];
        if (latLngList != null && latLngList.size() != 0) {
            for (int x = 0; x < latLngList.size(); x++) {
                LatLng latLng = latLngList.get(x);
                Marker markerToMap = addMarkerToMap(latLng, res);
                listMarker.add(markerToMap);
            }
        }
        return listMarker;
    }

    /**
     * 84转火星   解决偏移
     *
     * @param latLng
     * @param i
     * @return
     */
    public Marker drawMember84tohx(LatLng latLng, int i) {
        int res = MapRes.tabRes[i];
        //根据类型选择不同的地图资源
        Gps gps = GpsUtils.wgs84togcj02(latLng.longitude, latLng.latitude);
        LatLng latLnghx = new LatLng(gps.getWgLat(), gps.getWgLon());
        return addMarkerToMap(latLnghx, res);
    }

    /**
     * 画自定义
     */
    protected Marker drawCustomMarker(MarkerEntity markerEntity, LatLng latLng, String mode, int res) {
        MarkerOptions markerOption = new MarkerOptions();
        if (MarkerEntity.MARKER_MODE_CAMERA.equals(mode)) {
            markerOption.position(latLng)
                    .title("")
                    .snippet("")
                    .draggable(false)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromView(getCameraMarkerView(res)));
        } else {
            markerOption.position(latLng)
                    .title("")
                    .snippet("")
                    .draggable(false)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromView(getOtherMarkerView(res)));
        }
        Marker marker = lMap.addMarker(markerOption);
        marker.setVisible(true);
        return marker;
    }

    public void interviewMarkpopupWindow() {

    }

    /**
     * 绘制摄像头标记
     *
     * @param iconRes
     * @return
     */
    private View getMiddleMarkerView(int iconRes) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_middle_marker_view, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(iconRes);
        return view;
    }

    /**
     * 获取自定义标记
     *
     * @param pm_val
     * @return
     */
    private View getMarkerView(int imgResId, String pm_val) {
        View view = LayoutInflater.from(mapView.getContext()).inflate(R.layout.marker_shake, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        iv_icon.setImageResource(imgResId);
        tv_val.setText(pm_val);
        return view;
    }

    /**
     * 绘制摄像头标记
     *
     * @param iconRes
     * @return
     */
    private View getCameraMarkerView(int iconRes) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_camera_marker_view, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(iconRes);
        return view;
    }

    /**
     * 绘制其它标记
     *
     * @param iconRes
     * @return
     */
    private View getOtherMarkerView(int iconRes) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_other_marker_view, null);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(iconRes);
        return view;
    }

    //点击不同类型的数据,不同的弹框=======摄像头的弹框=======
    public View popupCamera(MarkerData data) {
//        MarkerEntity entity = (MarkerEntity) data.data;
        View itemView = View.inflate(getContext(), R.layout.popup_map_camera, null);
        return itemView;
    }

    /**
     * 人员类型弹框
     *
     * @param data
     * @return
     */
    public View popupPersonnel(MarkerData data) {
        View itemView = View.inflate(getContext(), R.layout.popup_map_personnel, null);
        return itemView;
    }

    /**
     * 无人机类型
     *
     * @param data
     * @return
     */
    public View uavCamera(MarkerData data) {
        View itemView = View.inflate(getContext(), R.layout.popup_map_uav, null);
        return itemView;
    }

    private Dialog dialog;

    //公用方法用来将dialog显示在屏幕底部
    public void showDialog(View itemView) {
        dialog = new Dialog(getContext(), R.style.BottomDialogTheme);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.PopupAnimation);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(itemView);
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(LeadorNaviLocation leadorNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam(int i) {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onUpdateTrafficFacility(LeadorNaviTrafficFacilityInfo[] leadorNaviTrafficFacilityInfos) {

    }

    @Override
    public void showCross(LeadorNaviCross leadorNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(LeadorLaneInfo[] leadorLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void updateServiceFacility(LeadorServiceFacilityInfo[] leadorServiceFacilityInfos) {

    }

    @Override
    public boolean isSpeaking() {
        return false;
    }

    @Override
    public void onGetLinkDetailInfo(String[] strings, int i) {

    }

    @Override
    public void onDirectionNaviInfo(NaviDirectionInfo naviDirectionInfo) {

    }
}
