package com.fy.wanandroid.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.wanandroid.R;

/**
 * 测试 某些功能
 * Created by fangs on 2018/9/11 15:20.
 */
public class TestActivity extends AppCompatActivity implements IBaseActivity{

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.arc_view_layout;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.StatusBuilder.init()
                .setStatusColor(R.color.black, 255)
                .setNavColor(R.color.statusBar, 0)
                .setTransparentBar(activity);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void reTry() {}

}
