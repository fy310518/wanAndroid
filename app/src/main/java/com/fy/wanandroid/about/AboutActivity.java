package com.fy.wanandroid.about;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.wanandroid.R;

import butterknife.BindView;

/**
 * 关于
 * Created by fangs on 2018/4/8.
 */
public class AboutActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.tvAppInformation)
    TextView tvAppInformation;
    @BindView(R.id.tvAbout)
    TextView tvAbout;


    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_about;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        tvAppInformation.setText(AppUtils.getAppName() + "\n" + AppUtils.getVersionName());

        tvAbout.setText(Html.fromHtml(ResourceUtils.getStr(R.string.about_content)));
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reTry() {

    }
}