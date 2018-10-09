package com.fy.wanandroid.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.wanandroid.R;

import butterknife.BindView;

/**
 * 测试 某些功能
 * Created by fangs on 2018/9/11 15:20.
 */
public class TestActivity extends AppCompatActivity implements IBaseActivity{

    @BindView(R.id.avTile)
    ArcView avTitle;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.arc_view_layout;
    }

    @StatusBar(statusColor = R.color.statusBar, statusAlpha = 105, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        avTitle.setLeftText("75", "分", "大王");
    }

}
