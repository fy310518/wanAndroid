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
import com.fy.baselibrary.utils.ResUtils;
import com.fy.wanandroid.R;

import butterknife.BindView;

/**
 * 关于
 * Created by fangs on 2018/4/8.
 */
public class AboutActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

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
        MdStatusBar.StatusBuilder.init()
                .setStatusColor(R.color.statusBar, 0)
                .setNavColor(R.color.statusBar, 0)
                .setColorBar(activity);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        tvAppInformation.setText(AppUtils.getAppName() + "\n" + AppUtils.getVersionName());

        tvAbout.setText(Html.fromHtml(ResUtils.getStr(R.string.about_content)));
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {

    }
}
