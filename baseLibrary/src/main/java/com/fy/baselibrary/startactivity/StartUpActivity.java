package com.fy.baselibrary.startactivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.TintUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 欢迎页
 * Created by fangs on 2017/12/12.
 */
public class StartUpActivity extends AppCompatActivity implements IBaseActivity {

    private int skip = 5;
    TextView tvSkip;

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
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
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
            RequestUtils.clearDispos();
            intoMainAct();
        }
    }

    @Override
    public void reTry() {

    }

    private void hideLoadView() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(skip + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        tvSkip.setText(ResourceUtils.getReplaceStr(R.string.skip, skip - aLong));
                        L.e("interval", aLong + "s");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        intoMainAct();
                    }
                });
    }

    private void intoMainAct() {
        JumpUtils.jump(StartUpActivity.this, "wanandroid.fy.com.main.MainActivity", null);
        finish();
    }
}
