package com.fy.wanandroid.testdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.wanandroid.R;

/**
 * 测试 多状态布局 在fragment中的使用
 */
public class TestStatusFragmentActivity extends AppCompatActivity implements IBaseActivity {

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.test_status_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().add(R.id.statusContent, new TestStatusFragment()).commitAllowingStateLoss();
    }
}
