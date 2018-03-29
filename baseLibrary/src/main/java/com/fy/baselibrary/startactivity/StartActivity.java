package com.fy.baselibrary.startactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 不可见Activity 用于控制程序 退出(入口activity)
 * Created by fangs on 2017/4/26.
 */
public class StartActivity extends AppCompatActivity implements IBaseActivity {

    private static final String FLAG_EXIT = "FLAG_EXIT_APP";

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return 0;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        //rx 递归删除缓存的压缩文件
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            FileUtils.recursionDeleteFile(new File(FileUtils.getPath("head.img.temp")));
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> L.e("aaaab", "" + Thread.currentThread().getName() ));

        Intent intent = getIntent();
        if (null != savedInstanceState) {
            if (savedInstanceState.getBoolean(FLAG_EXIT)) {
                exitApp();
                return;
            }
        } else if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            L.e("StartActivity", "----- 1");
            exitApp();
        } else {
            L.e("StartActivity", "----- 2");
            isStartActivityOnly();
        }
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e("StartActivity", "onNewIntent- false");
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            L.e("StartActivity", "onNewIntent");
            exitApp();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putBoolean(FLAG_EXIT, true);
            L.e("StartActivity", "onSaveInstanceState");
        }
    }

    // 避免从桌面启动程序后，会重新实例化入口类的activity
    private void isStartActivityOnly() {
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    exitApp();
                    return;
                }
            }
        } else {
            startActivity(new Intent("hodgepodge.fy.com.main.MainActivity"));
        }
    }

    /**
     * 退出应用
     */
    private void exitApp() {
        finish();
        System.exit(0);
    }
}
