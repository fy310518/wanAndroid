package com.gcstorage.map.maker;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.PopupWindow;

public class mpopupwindow {

    public static void mpopupwindow(PopupWindow mpopup, View view){

        mpopup.setWidth(DrawerLayout.LayoutParams.MATCH_PARENT);
        mpopup.setHeight(DrawerLayout.LayoutParams.WRAP_CONTENT);
        mpopup.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mpopup.setFocusable(true);
        mpopup.setOutsideTouchable(true);
        mpopup.setContentView(view);
    }
}
