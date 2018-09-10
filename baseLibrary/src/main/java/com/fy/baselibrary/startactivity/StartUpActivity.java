package com.fy.baselibrary.startactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.application.ConfigUtils;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.TintUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 欢迎页
 * 注意：1、欢迎页，背景图片不能是 svg图片
 *      2、使用此欢迎页项目应用id 需要和项目包名一致
 * Created by fangs on 2017/12/12.
 */
public class StartUpActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    private int skip = 3;
    TextView tvSkip;
    ImageView loadImg;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_start_up;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.StatusBuilder.init()
                .setStatusColor(R.color.transparent, 0)
                .setNavColor(R.color.transparent, 0)
                .setTransparentBar(activity);
        StatusBarContentColor.setStatusTextColor(this, true, true);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        loadImg = findViewById(R.id.loadImg);
        int drawableId = ConfigUtils.getLoadImg();
        if (drawableId > 0) loadImg.setImageDrawable(TintUtils.getDrawable(drawableId, 0));

        Drawable back = TintUtils.getTintDrawable(R.drawable.shape_ellipse_rect, 0, R.color.alphaBlack);
        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
        tvSkip.setText(ResUtils.getReplaceStr(R.string.skip, skip));
        tvSkip.setBackground(back);

        hideLoadView();
    }

    @ClickFilter()
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSkip) {
            intoMainOrLogin();
        }
    }

    @Override
    public void reTry() {}

    @SuppressLint("CheckResult")
    private void hideLoadView() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(skip + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> tvSkip.setText(ResUtils.getReplaceStr(R.string.skip, skip - aLong)))
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(aLong -> {
                    if (aLong == 2L)intoMainOrLogin();
                });
    }

    /**
     * 根据条件 判断进入登录页还是主界面
     */
    private void intoMainOrLogin() {
        if (Constant.isMustAppLogin && !SpfUtils.getSpfSaveBoolean(Constant.isLogin)) {
            JumpUtils.jump(this, AppUtils.getLocalPackageName() + ".login.LoginActivity", null);
        } else {
            JumpUtils.jump(this, AppUtils.getLocalPackageName() + ".main.MainActivity", null);
        }

        finish();
    }
}
