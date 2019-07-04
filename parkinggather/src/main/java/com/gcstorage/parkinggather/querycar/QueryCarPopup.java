package com.gcstorage.parkinggather.querycar;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.popupwindow.CommonPopupWindow;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.rv.divider.GridItemDecoration;
import com.fy.baselibrary.utils.ResUtils;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.util.PGAppUtils;

import java.util.Arrays;
import java.util.List;

/**
 * DESCRIPTION：选择省、直辖市 简称 弹窗
 * Created by fangs on 2019/7/3 14:48.
 */
public class QueryCarPopup extends CommonPopupWindow {

    private onPopupClickListener clickListener;

    @Override
    protected int initLayoutId() {
        return R.layout.query_car_popup_layout;
    }

    @Override
    public void convertView(ViewHolder holder) {

        RecyclerView queryPopupList = holder.getView(R.id.queryPopupList);
        queryPopupList.setLayoutManager(new GridLayoutManager(mContext, 7));
        queryPopupList.addItemDecoration(
                GridItemDecoration.Builder.init()
                        .setColumn(7)
                        .setmSpace(R.dimen.spacing_small)
                        .setDraw(false)
                        .create(mContext));

        String[] pAbbrArr = ResUtils.getStrArray(R.array.province_abbr_list);
        List<String> pAbbrList = Arrays.asList(pAbbrArr);

        queryPopupList.setAdapter(new RvCommonAdapter<String>(mContext, R.layout.query_car_popup_item, pAbbrList, queryPopupList) {
            @Override
            public void convert(ViewHolder holder, String str, int position) {
                CheckBox queryPopupItem = holder.getView(R.id.queryPopupItem);
                queryPopupItem.setBackground(PGAppUtils.getSelector(R.drawable.btn_shape_white_bg, 0, R.color.button_pressed, R.color.white));

                queryPopupItem.setText(str);
                queryPopupItem.setChecked(mSelectedPos == position);

                queryPopupItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder
                        ViewHolder couponVH = (ViewHolder) queryPopupList.findViewHolderForLayoutPosition(mSelectedPos);
                        if (couponVH != null) {//还在屏幕里
                            CheckBox ccBox = couponVH.getView(R.id.queryPopupItem);
                            if (null != ccBox) ccBox.setChecked(false);
                        } else {//add by 2016 11 22 for 一些极端情况，holder被缓存在Recycler的cacheView里，
                            //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
                            notifyItemChanged(mSelectedPos);
                        }

                        //设置新Item的勾选状态
                        mSelectedPos = position;
                        queryPopupItem.setChecked(true);

                        if (null != clickListener) clickListener.onClick(str);
                        dismiss();
                    }
                });
            }
        });
    }

    /**
     * 设置 回调接口
     * @param clickListener
     */
    public void setClickListener(onPopupClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 定义弹窗 点击事件 回调接口
     */
    interface onPopupClickListener{
        void onClick(String item);
    }
}
