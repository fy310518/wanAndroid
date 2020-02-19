package com.fy.wanandroid.test;

import android.content.Context;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.wanandroid.R;

import java.util.List;

/**
 * describe： 测试二球给的 图片列表
 * Created by fangs on 2018/12/28 16:44.
 */
public class TestAdapter extends RvCommonAdapter<TestBean> {

    FrameLayout.LayoutParams layoutParams1;
    FrameLayout.LayoutParams layoutParams2;

    public TestAdapter(Context context, List<TestBean> datas) {
        super(context, R.layout.activity_test_item, datas);

        int pixels = (int) ((ScreenUtils.getScreenWidth() - 10) * 0.5);
        layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, pixels);
        layoutParams2 = new FrameLayout.LayoutParams(pixels, pixels);
    }

    @Override
    public void convert(ViewHolder holder, TestBean testBean, int position) {
        ImageView img = holder.getView(R.id.img);
        FrameLayout fl = holder.getView(R.id.fl);

        if (TextUtils.isEmpty(testBean.getRecommend_img_url())){
            fl.setLayoutParams(layoutParams2);
            ImgLoadUtils.loadCircularBead(testBean.getImg_url(),-1, img);
        } else {
            fl.setLayoutParams(layoutParams1);
            ImgLoadUtils.loadCircularBead(testBean.getRecommend_img_url(), -1, img);
        }
    }
}
