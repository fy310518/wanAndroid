package com.fy.baselibrary.rv.divider.sticky;

import android.view.View;

/**
 *
 * Created by 吸附 on 2018/1/16.
 */
public class ExampleStickyView implements StickyView {

    @Override
    public boolean isStickyView(View view) {
        return (Boolean) view.getTag();
    }

    @Override
    public int getStickViewType() {
        return StickyView.StickyType;
    }
}
