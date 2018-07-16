package com.fy.baselibrary.startactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.TintUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 欢迎页
 * 注意：1、欢迎页，背景图片名称必须为“loading”，必须放在drawable 类型目录如：drawable-xhdpi
 *      2、使用此欢迎页项目应用id 需要和项目包名一致
 * Created by fangs on 2017/12/12.
 */
public class StartUpActivity extends AppCompatActivity implements IBaseActivity {

    private int skip = 5;
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
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
        StatusBarContentColor.setStatusTextColor(this, true, true);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        loadImg = findViewById(R.id.loadImg);
        int loadImgId = ResourceUtils.getDrableId(this, "loading");
        loadImg.setImageDrawable(TintUtils.getDrawable(loadImgId));

        Drawable back = TintUtils.getTintDrawable(R.drawable.shape_tag, R.color.alphaBlack);
        tvSkip = findViewById(R.id.tvSkip);
        tvSkip.setOnClickListener(this);
        tvSkip.setText(ResourceUtils.getReplaceStr(R.string.skip, skip));
        tvSkip.setBackground(back);

        hideLoadView();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSkip) {
            intoMainAct();
        }
    }

    @Override
    public void reTry() {

    }

    @SuppressLint("CheckResult")
    private void hideLoadView() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(skip + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> tvSkip.setText(ResourceUtils.getReplaceStr(R.string.skip, skip - aLong)))
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(aLong -> {
                    if (aLong == 4L)intoMainAct();
                });
    }

    private void intoMainAct() {
        JumpUtils.jump(this,  AppUtils.getLocalPackageName() + ".main.MainActivity", null);
        finish();
    }
}
