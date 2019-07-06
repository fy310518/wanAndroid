package com.gcstorage.parkinggather.main;

import android.content.Context;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;

import java.util.List;

/**
 * DESCRIPTION：驻车采集--》采集列表 adapter
 * Created by fangs on 2019/7/2 9:27.
 */
public class GatherRvAdapter extends RvCommonAdapter<ParkingInfoEntity.DataBean> {

    public GatherRvAdapter(Context context, List<ParkingInfoEntity.DataBean> dates) {
        super(context, R.layout.parking_gather_rv_item, dates);
    }

    @Override
    public void convert(ViewHolder holder, ParkingInfoEntity.DataBean dataBean, int position) {

        ImgLoadUtils.loadImage(dataBean.getCarImg(), R.drawable.default_loading_image, holder.getView(R.id.iv_recent_pic));
        ImgLoadUtils.loadRadiusImg(dataBean.getPic(), R.drawable.default_pic_icon, holder.getView(R.id.xcr_head_pic));

        holder.setText(R.id.tv_name, dataBean.getName());
        holder.setText(R.id.tv_time, dataBean.getDate() + " " + dataBean.getTime());

        holder.setText(R.id.tv_location, dataBean.getAddress());
        holder.setText(R.id.tv_licence_number, dataBean.getCarNum());


    }


}
