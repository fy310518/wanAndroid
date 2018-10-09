package com.fy.wanandroid.animation.revealeffect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.wanandroid.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 揭露动画 demo
 * Created by fangs on 2018/8/21 12:03.
 */
public class RevealEffectActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.imgAngel)
    AppCompatImageView imgAngel;

    @BindView(R.id.head)
    TextView head;
    @BindView(R.id.btn)
    Button btn;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.demo_revea_effect_layout;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn, R.id.head})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                runRevealEffect();
                break;
            case R.id.head:
                Bundle bundle = new Bundle();
                bundle.putInt("x", head.getWidth() / 2);
                bundle.putInt("y", head.getHeight() / 2);
                JumpUtils.jump(RevealEffectActivity.this, RevealEffectJumpActivity.class, bundle);
                break;
        }
    }

    private boolean flag;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void runRevealEffect() {
        //求出第2个和第3个参数
        int[] vLocation = new int[2];
        imgAngel.getLocationInWindow(vLocation);
        int centerX = imgAngel.getWidth() / 2;
        int centerY = imgAngel.getHeight() / 2;

        //求出要揭露 View 的对角线，来作为扩散圆的最大半径
        int hypotenuse = (int) Math.hypot(imgAngel.getWidth(), imgAngel.getHeight());

        if (flag) {//显示揭露对象
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(imgAngel, centerX, centerY, 0, hypotenuse);
            circularReveal.setDuration(1000);
            //注意：这里显示 head 调用并没有在监听方法里，并且是在动画开始前调用。
            imgAngel.setVisibility(View.VISIBLE);
            circularReveal.start();
            flag = false;
        } else {

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(imgAngel, centerX, centerY, hypotenuse, 0);
            circularReveal.setDuration(1000);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    imgAngel.setVisibility(View.GONE);

                }
            });
            circularReveal.start();
            flag = true;
        }
    }
}
