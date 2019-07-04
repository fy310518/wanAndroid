package com.gcstorage.parkinggather.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.gcstorage.parkinggather.R;

/**
 * DESCRIPTION：登录 activity
 * Created by fangs on 2019/7/1 16:33.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity {

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.login_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

    }

}
