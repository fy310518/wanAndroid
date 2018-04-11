package com.fy.baselibrary.rv.divider.sticky;

import android.view.View;

/**
 * 获取吸附View相关的信息
 * Created by 吸附 on 2018/1/16.
 */
public interface StickyView {

    /** 吸附 itemtype */
    int StickyType = 58;

    /**
     * 是否是吸附 view
     * @param view
     * @return
     */
    boolean isStickyView(View view);

    /**
     * 得到吸附 view的 itemType
     * @return
     */
    int getStickViewType();
}
