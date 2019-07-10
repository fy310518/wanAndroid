package com.gcstorage.parkinggather.history;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.gcstorage.map.BaseMapsFragment;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.carinfo.CarGatherInfoActivity;
import com.ishowmap.api.location.IMLocation;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.Marker;
import com.leador.api.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIPTION：地图
 * Created by fangs on 2019/7/9 17:59.
 */
public class ParkCarMapFragment extends BaseMapsFragment {

    List<ParkingInfoEntity.DataBean> imageInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_callpeople, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);

        setLocationSource();
        location();

        moveToCurrentLocation();
    }

    @Override
    public void onStubLocationChanged(IMLocation location) {

//        drawPolygon();
    }

    //设置绘制浮层的 数据
    public void drawPolygon(List<ParkingInfoEntity.DataBean> imageInfoList){
        this.imageInfoList = imageInfoList;

        drawPolygon();
    }

    //在 地图上面 绘制 线 和 浮层
    private void drawPolygon(){
        if (null == imageInfoList) return;

        List<LatLng> lineList = new ArrayList<>();
        for (ParkingInfoEntity.DataBean listBean : imageInfoList){
            lineList.add(createLatLng(listBean.getLatitude(), listBean.getLongitude()));
        }

        if (lineList.size() > 0){
            drawLine(lineList);
            addMakerToMap(lineList);
        }
    }

    public void addMakerToMap(List<LatLng> mLatLnglist) {
        if (mLatLnglist == null || mLatLnglist.size() == 0) {
            return;
        }

        int imgSize = DensityUtils.dp2px(60);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(imgSize, imgSize);

        RequestOptions requestOptions = new RequestOptions()
                .fallback(R.drawable.default_pic_icon)
                .error(R.drawable.default_pic_icon)
                .placeholder(R.drawable.default_pic_icon)
                .transforms(new RoundedCorners(15))
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        for (int i = 0; i < mLatLnglist.size(); i++) {
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER);
            img.setLayoutParams(lp);

            int finalI = i;
            Glide.with(getContext())
                    .load(imageInfoList.get(i).getCarImg())
                    .apply(requestOptions)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            img.setImageDrawable(resource);

                            lMap.addMarker(new MarkerOptions().position(mLatLnglist.get(finalI))
                                    .title(finalI + "")
                                    .icon(BitmapDescriptorFactory.fromView(img))
                //                    .visible(false)
                                    .draggable(true));
                        }
                    });
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null == imageInfoList) return true;

        int position = Integer.parseInt(marker.getTitle());

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("ParkingInfoEntity", new ParkingInfoEntity(imageInfoList));
        JumpUtils.jump(this, CarGatherInfoActivity.class, bundle);

        return true;
    }

}
