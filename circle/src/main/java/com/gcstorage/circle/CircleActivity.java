package com.gcstorage.circle;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.popupwindow.CommonPopupWindow;
import com.fy.baselibrary.base.popupwindow.NicePopup;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.notify.T;
import com.gcstorage.circle.publish.CirclePublishActivity;
import com.gcstorage.circle.widgets.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * DESCRIPTION：战友圈首页
 * Created by fangs on 2019/3/11 9:34.
 */
public class CircleActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    CommonPopupWindow popupWindow;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_act_menu, menu);
        View menuLayout = menu.findItem(R.id.menuAdd).getActionView();
        menuLayout.setOnClickListener(this);
        return true;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

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

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        int i = v.getId();
        if (i == R.id.menuLayout) {
            showPopup(v);
            return;
        } else if (i == R.id.tv_dynamic){//动态
            bundle.putInt("title", R.string.newDynamic);

        }else if (i == R.id.tv_assist_notice){//协查通报
            bundle.putInt("title", R.string.bulletin);

        }else if (i == R.id.tv_case_analyse){//案件分享
            bundle.putInt("title", R.string.CaseSharing);

        }else if (i == R.id.tv_community_industry){//微课堂
            bundle.putInt("title", R.string.MicroClassroom);

        }

        //关闭 弹窗
        if (null != popupWindow && popupWindow.isShowing()) popupWindow.dismiss();

        JumpUtils.jump(this, CirclePublishActivity.class, bundle);
    }

    private void showPopup(View view){
        if (null != popupWindow && popupWindow.isShowing()) return;

        popupWindow = NicePopup.Builder.init()
                .setLayoutId(R.layout.circle_popup_publish)
                .setConvertListener(new NicePopup.PopupConvertListener(){
                    @Override
                    public void convertView(ViewHolder holder) {
                        holder.setOnClickListener(R.id.tv_dynamic, CircleActivity.this);
                        holder.setOnClickListener(R.id.tv_assist_notice, CircleActivity.this);
                        holder.setOnClickListener(R.id.tv_case_analyse, CircleActivity.this);
                        holder.setOnClickListener(R.id.tv_community_industry, CircleActivity.this);
                        holder.setOnClickListener(R.id.iv_close, CircleActivity.this);
                    }
                }).create()
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnim(R.style.AnimTop)
                .onCreateView(CircleActivity.this);

        popupWindow.showAsDropDown(view, 0, 0);
    }
}
