package com.fy.wanandroid.hierarchy;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.TreeBean;

/**
 * 知识体系 下的文章
 * Created by fangs on 2018/4/24.
 */
public class HierarchyActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_hierarchy;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        TreeBean bean = (TreeBean) bundle.getSerializable("TreeBean");
        if (null == bean) return;

        toolbar.setTitle(bean.getName());

        List<TreeBean.ChildrenBean> childrenList = bean.getChildren();
        List<Fragment> fragmentList = new ArrayList<>();
        StringBuilder strB = new StringBuilder();
        for (TreeBean.ChildrenBean childrenBean : childrenList) {
            strB.append(childrenBean.getName() + ";");
            fragmentList.add(TabFragment.getInstentce(childrenBean.getId()));
        }

        String[] mTitles = strB.toString().split(";");
        TabFmPagerAdapter adapter = new TabFmPagerAdapter(getSupportFragmentManager(), fragmentList, mTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {}
}
