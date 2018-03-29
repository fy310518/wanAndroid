package com.fy.baselibrary.statuslayout;

import android.view.View;

/**
 * 状态View显示隐藏监听事件
 * <p/>
 * Created by chenpengfei on 2016/12/15.
 */
public interface OnShowHideViewListener {

    void onShowView(View view, int id);

    void onHideView(View view, int id);
}
