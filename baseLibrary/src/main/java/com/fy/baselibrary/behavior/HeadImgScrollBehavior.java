package com.fy.baselibrary.behavior;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * 个人中心 头像 跟随 AppBarLayout 缩放
 * Created by fangs on 2018/1/5.
 */
public class HeadImgScrollBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private Context mContext;

    //头像的最终大小 px
    private float mCustomFinalHeight = 80;
    //标题栏 + 状态栏 高度
    private float mDependencyHeight;
    //头像布局 最大高度
    private float mMaxHeight;

    //图片 是否达到最大
    private boolean isImgBestMax = true;

    private float mImgMaxHeight;
    private float mStartAvatarX;
    private int mAvatarMaxHeight;

    public HeadImgScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mImgMaxHeight = DensityUtils.dp2px(80);
        mMaxHeight = DensityUtils.dp2px(200);
        mDependencyHeight = DensityUtils.dp2px(56) + ScreenUtils.getStatusHeight();
    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
//        return dependency instanceof AppBarLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
//
//        //初始化属性
//        if(mStartAvatarY == 0){
//            mStartAvatarY = dependency.getY();
//        }
//        if(mStartAvatarX == 0){
//            mStartAvatarX = child.getX();
//        }
//
//        if(mAvatarMaxHeight == 0){
//            mAvatarMaxHeight = child.getHeight();
//        }
//
//        //让 ImageView 跟随  AppBarLayout 垂直移动
//        child.setY(dependency.getY() + dependency.getHeight() / 2 - mCustomFinalHeight / 2);
//
////        设置头像大小
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//        layoutParams.height = (int) mCustomFinalHeight;
//        layoutParams.width = (int) mCustomFinalHeight;
//        child.setLayoutParams(layoutParams);
//
//        return true;
//    }

    //    返回值表明这次滑动我们要不要关心，我们要关心什么样的滑动？本例：当然是y轴方向上的
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    //    让设置 Behavior 的view 跟随 关心的view 滑动
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull ImageView child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

        if (target.getTop() > mDependencyHeight && target.getTop() < mMaxHeight) {
            if (!isImgBestMax) return;
            isImgBestMax = false;

            setAlplha(child, 1.0f, 0f);
        } else if (target.getTop() == mDependencyHeight){//达到最小
            if (isImgBestMax) return;
            isImgBestMax = true;
            float imgSize = target.getTop() / mMaxHeight * mImgMaxHeight;

            layoutParams.height = (int) mCustomFinalHeight;
            layoutParams.width = (int) mCustomFinalHeight;

            int topMargin = (int) ((DensityUtils.dp2px(56) - imgSize) * 0.5 + ScreenUtils.getStatusHeight());
            layoutParams.setMargins(0, topMargin, 0, 0);

            child.setLayoutParams(layoutParams);

            setAlplha(child, 0f, 1.0f);

        } else {//达到最大
            if (isImgBestMax) return;
            isImgBestMax = true;

            layoutParams.height = (int) mImgMaxHeight;
            layoutParams.width = (int) mImgMaxHeight;
            child.setLayoutParams(layoutParams);

            setAlplha(child, 0f, 1.0f);
        }

    }

    private void setAlplha(View child, float formAlpha, float toAlpha){
        ObjectAnimator a1 = ObjectAnimator.ofFloat(child, "alpha", formAlpha, toAlpha);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(1000);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.playTogether(a1);
        //其他组合方式
        animSet.start();
    }
}
