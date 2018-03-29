package com.fy.baselibrary.base.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.fy.baselibrary.base.PopupDismissListner;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.utils.DensityUtils;

/**
 * popupWindow 封装
 * Created by fangs on 2018/3/21.
 */
public abstract class CommonPopupWindow extends PopupWindow {

    Context mContext;
    @LayoutRes
    protected int layoutId;
    View view;

    int mWidth, mHeight;//弹窗的宽和高

    boolean isShowAnim;
    int anim;//动画Id
    boolean isHide = true;
    float bgAlpha = 0.5f;

    PopupDismissListner dismissListner;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    public int getWidth() {
        return view.getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return view.getMeasuredHeight();
    }

    /** 设置 PopupWindow 布局 */
    protected abstract int initLayoutId();

    /** 渲染数据到View中 */
    public abstract void convertView(ViewHolder holder);

    public CommonPopupWindow() {
    }

    /**
     * 绘制 Popup UI
     * @param context
     */
    public CommonPopupWindow onCreateView(Context context) {
        mContext = context;

        layoutId = initLayoutId();

        view = LayoutInflater.from(context).inflate(layoutId, null);
        DensityUtils.measureWidthAndHeight(view);

        convertView(ViewHolder.createViewHolder(context, view));

        initParams(view);


        return this;
    }

    /**
     * 初始化 PopupWindow 样式
     */
    protected void initParams(View view) {
        setContentView(view);

        if (mWidth == 0 || mHeight == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            setWidth(DensityUtils.dp2px(mContext, mWidth));
            setHeight(DensityUtils.dp2px(mContext, mHeight));
        }


        //设置动画
        if (isShowAnim)setAnimationStyle(anim);
        setHide(isHide);
        bgAlpha(bgAlpha);
    }

    /**
     * 点击 window外的区域 是否消失
     * @param touchable 是否可点击
     */
    private void setHide(boolean touchable) {
        setBackgroundDrawable(new ColorDrawable(0x000000));//设置透明背景
        setOutsideTouchable(touchable);//设置outside可点击
        setFocusable(touchable);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bgAlpha(1.0f);// popupWindow隐藏时恢复屏幕正常透明度
        if (null != dismissListner) dismissListner.onDismiss();
    }



    /**
     * 设置宽度和高度 如果不设置 默认是wrap_content
     *
     * @param width  宽(dp)
     * @param height 高(dp)
     * @return Builder
     */
    public CommonPopupWindow setWidthAndHeight(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    /**
     * 是否可点击Outside消失
     *
     * @param touchable 是否可点击
     * @return CommonPopupWindow
     */
    public CommonPopupWindow setOutside(boolean touchable) {
        isHide = touchable;
        return this;
    }

    /**
     * 设置 popupWindow 背景透明度
     * @param bgAlpha  0.0-1.0   0表示完全透明
     * @return
     */
    public CommonPopupWindow setBgAlpha(float bgAlpha) {
        this.bgAlpha = bgAlpha;
        return this;
    }

    /**
     * 设置动画
     * @return CommonPopupWindow
     */
    public CommonPopupWindow setAnim(int anim) {
        isShowAnim = true;
        this.anim = anim;
        return this;
    }

    /**
     * 设置 窗口 dismiss 监听
     *
     * @param dismissListner
     * @return
     */
    public CommonPopupWindow setDismissListner(PopupDismissListner dismissListner) {
        this.dismissListner = dismissListner;
        return this;
    }

}
