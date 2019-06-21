package com.gc.unifiedlogin.login;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.gc.unifiedlogin.R;
import com.gc.unifiedlogin.sysnotify.H5AppActivity;
import com.gc.unifiedlogin.sysnotify.SysNotifyActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * describe: 统一登陆 --》登陆界面
 * Created by fangs on 2019/5/24 22:11.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.editId)
    TextInputEditText editId;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.login_activity;
    }

    @StatusBar(statusColor = R.color.white, navColor = R.color.statusBar)
    @NeedPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    @Override
    public void initData(Activity activity, Bundle bundle) {
        StatusBarContentColor.setStatusTextColor(this, true, false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setBackgroundColor(ResUtils.getColor(R.color.BgColor));
        toolbarTitle.setTextColor(ResUtils.getColor(R.color.txtSuperColor));
    }

    @Override
    public void onBackPressed() {
        JumpUtils.backDesktop(this);
    }

    @ClickFilter
    @OnClick({R.id.tvUpdatePhoto, R.id.btnLogin})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvUpdatePhoto://更新照片
                JumpUtils.jump(this, H5AppActivity.class, null);
                break;
            case R.id.btnLogin://刷脸登陆
                JumpUtils.jump(this, SysNotifyActivity.class, null);
                break;
        }
    }
}
