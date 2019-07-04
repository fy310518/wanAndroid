package com.gcstorage.parkinggather.carinfo;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.main.ParkingInfoEntity;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * DESCRIPTION：驻车采集 查看详情 viewpager
 * Created by fangs on 2019/7/2 16:43.
 */
public class ViewPagerItemHolder extends Holder<ParkingInfoEntity.DataBean> {

    CarGatherInfoActivity activity;
    private PhotoView imageView;

    public ViewPagerItemHolder(View itemView, CarGatherInfoActivity activity) {
        super(itemView);
        this.activity = activity;
    }

    @Override
    protected void initView(View view) {
        imageView = view.findViewById(R.id.subImageView);
        imageView.setOnClickListener(v -> activity.toggleStateChange());
    }

    @Override
    public void updateUI(ParkingInfoEntity.DataBean data) {
        ImgLoadUtils.loadImage(data.getCarImg(), R.drawable.default_loading_image, imageView);
    }
}
