package com.fy.baselibrary.aop.background;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.utils.drawable.TintUtils;

import org.xmlpull.v1.XmlPullParserException;

/**
 * describe： 矢量图兼容 辅助类
 * Created by fangs on 2018/12/21 16:48.
 */
public class SvgDrawable implements ICreateDrawable{

    View view;
    TypedArray svgCompat;

    public SvgDrawable(View view, TypedArray svgCompat) {
        this.view = view;
        this.svgCompat = svgCompat;
    }

    @Override
    public void addDrawable() throws XmlPullParserException {
        @DrawableRes
        int svgDrawable = 0;
        int svgTintColor = 0;
        int drawableType = 0;
        int iconLocationType = 0;

        for (int i = 0; i < svgCompat.getIndexCount(); i++) {
            int attr = svgCompat.getIndex(i);
            if (attr == R.styleable.svgCompat_svgDrawable) {
                svgDrawable = svgCompat.getInteger(attr, 0);
            } else if (attr == R.styleable.svgCompat_svgTintColor){
                svgTintColor = svgCompat.getInteger(attr, 0);
            } else if (attr == R.styleable.svgCompat_drawableType){
                drawableType = svgCompat.getInt(attr, 0);
            } else if (attr == R.styleable.svgCompat_iconLocationType){
                iconLocationType = svgCompat.getInt(attr, 0);
            }
        }

        if(svgDrawable == 0)return;

        Drawable drawable;

        if (svgTintColor == 0){
            drawable = TintUtils.getDrawable(svgDrawable, 1);
        } else {
            drawable = TintUtils.getTintDrawable(svgDrawable, drawableType, svgTintColor);
        }

        if (iconLocationType == 0){
            view.setBackground(drawable);
        } else {
            if (view instanceof TextView){
                TintUtils.setTxtIconLocal((TextView) view, drawable, iconLocationType);
            }
        }
    }
}
