package com.fy.wanandroid.utils;

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
public class LocalImageHolderView extends Holder<BannerBean> {

    private ImageView imageView;
    private TextView tvBannerTitle;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.imgBanner);
        tvBannerTitle = itemView.findViewById(R.id.tvBannerTitle);
    }

    @Override
    public void updateUI(BannerBean banner) {
        ImgLoadUtils.loadImage(banner.getImagePath(), imageView);
        tvBannerTitle.setText(banner.getTitle());
    }

}
