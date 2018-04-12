package com.fy.baselibrary.rv.adapter;

import android.view.View;

/**
 * 自定义 RecycclerView item 点击接口
 * Created by fangs on 2017/11/20.
 */
public interface OnItemClickListner {

    /**
     * 条目点击事件 回调方法
     * @param view 当前点击的条目的实体类
     */
    void onItemClick(View view);
}
