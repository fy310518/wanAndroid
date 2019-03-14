package com.gcstorage.circle;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.ResUtils;
import com.gcstorage.circle.widgets.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @DESCRIPTION： 战友圈首页
 * Created by fangs on 2019/3/11 9:34.
 */
public class CircleActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.circle_activity;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
//        Bundle bundle = getIntent().getExtras();
//        TreeBean bean = (TreeBean) bundle.getSerializable("TreeBean");
//        if (null == bean) return;
//        toolbar.setTitle(bean.getName());


        List<Fragment> fragmentList = new ArrayList<>();
        String[] titles = ResUtils.getStrArray(R.array.circleTitle);
        String[] modes = ResUtils.getStrArray(R.array.circleMode);
        for (int i = 0; i< titles.length; i++) {

            fragmentList.add(CircleTabFragment.getInstentce(titles[i], modes[i],
                    i != titles.length - 1 ? Constants.TYPE_CIRCLE_MODEPOST : Constants.TYPE_CIRCLE_SELFPUBLISH));
        }
//
        CircleTabPagerAdapter adapter = new CircleTabPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
