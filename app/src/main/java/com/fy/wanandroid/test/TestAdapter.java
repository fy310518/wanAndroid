package com.fy.wanandroid.test;

import android.content.Context;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.wanandroid.R;

import java.util.List;

/**
 * describe： todo 描述</br>
 * Created by fangs on 2018/12/28 16:44.
 */
public class TestAdapter extends RvCommonAdapter<TestBean> {

    public TestAdapter(Context context, List<TestBean> datas) {
        super(context, R.layout.activity_test_item, datas);
    }

    @Override
    public void convert(ViewHolder holder, TestBean testBean, int position) {
        ImageView img = holder.getView(R.id.img);
        ImgLoadUtils.loadCircularBead(mContext, testBean.getImg_url(), img);
    }
}
