package com.fy.wanandroid.animation.revealeffect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.wanandroid.R;

import butterknife.BindView;

/**
 * 揭露动画之转场 （注意：5.0以上）
 * Created by fangs on 2018/8/22 10:14.
 */
public class RevealEffectJumpActivity extends AppCompatActivity implements IBaseActivity{

    @BindView(R.id.linearLRoot)
    LinearLayout linearLRoot;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.demo_revea_effect_jump;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.StatusBuilder.init().setStatusColor(R.color.statusBar, 0)
                .setNavColor(R.color.statusBar, 0)
                .setColorBar(activity);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        linearLRoot.post(new Runnable() {
            @Override
            public void run() {
                runRevealEffect(bundle.getInt("x"), bundle.getInt("y"));
            }
        });
    }

    @Override
    public void reTry() {}

    private boolean flag = true;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void runRevealEffect(int centerX, int centerY) {
        //求出第2个和第3个参数
        int[] vLocation = new int[2];
        linearLRoot.getLocationInWindow(vLocation);
//        int centerX = linearLRoot.getWidth() / 2;
//        int centerY = linearLRoot.getHeight() / 2;

        //求出要揭露 View 的对角线，来作为扩散圆的最大半径
        int hypotenuse = (int) Math.hypot(linearLRoot.getWidth(), linearLRoot.getHeight());

        if (flag){//显示揭露对象
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(linearLRoot, centerX, centerY, 0, hypotenuse);
            circularReveal.setDuration(1000);
            //注意：这里显示 head 调用并没有在监听方法里，并且是在动画开始前调用。
            linearLRoot.setVisibility(View.VISIBLE);
            circularReveal.start();
            flag = false;
        } else {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(linearLRoot, centerX, centerY, hypotenuse, 0);
            circularReveal.setDuration(500);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    linearLRoot.setVisibility(View.GONE);
                    finish();
                }
            });
            circularReveal.start();
            flag = true;
        }
    }

    @Override
    public void onBackPressed() {
        flag = false;
        Bundle bundle = getIntent().getExtras();
        runRevealEffect(bundle.getInt("x"), bundle.getInt("y"));
    }
}
