package com.fy.wanandroid.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;

import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.BannerBean;

/**
 * 本地图片Holder
 * Created by fangs on 2017/7/6.
 */
public class LocalImageHolderView implements Holder<BannerBean> {

    private ImageView imageView;
    private TextView tvBannerTitle;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fm_one_banner, null);

        imageView = view.findViewById(R.id.imgBanner);
        tvBannerTitle = view.findViewById(R.id.tvBannerTitle);

        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerBean banner) {
        ImgLoadUtils.loadImage(banner.getImagePath(), imageView);
        tvBannerTitle.setText(banner.getTitle());
    }
}
