package wanandroid.fy.com.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.JumpUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import wanandroid.fy.com.R;
import wanandroid.fy.com.main.MainActivity;

/**
 * 欢迎页
 * Created by fangs on 2017/12/12.
 */
public class StartUpActivity extends AppCompatActivity implements IBaseActivity {

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
        hideLoadView();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reTry() {

    }

    //延迟200 毫秒 隐藏 加载图片控件
    private void hideLoadView() {
        //显示欢迎页，并设置点击事件（但是不设置点击回调）
//        loadImg.setVisibility(View.VISIBLE);
        Observable.timer(2500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    JumpUtils.jump(StartUpActivity.this, MainActivity.class, null);
                });
    }
}
