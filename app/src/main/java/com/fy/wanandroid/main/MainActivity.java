package com.fy.wanandroid.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.base.fragment.FragmentChangeManager;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.AnimUtils;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.wanandroid.R;
import com.fy.wanandroid.about.AboutActivity;
import com.fy.wanandroid.collect.MyCollectActivity;
import com.fy.wanandroid.loadfile.DownFileActivity;
import com.fy.wanandroid.login.LoginActivity;
import com.fy.wanandroid.main.fragment.FragmentOne;
import com.fy.wanandroid.main.fragment.FragmentThree;
import com.fy.wanandroid.main.fragment.FragmentTwo;
import com.fy.wanandroid.search.SearchActivity;
import com.fy.wanandroid.utils.SelectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 体育联盟 主界面
 * Created by fangs on 2017/12/12.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    private AppCompatActivity mContext;
    private FragmentChangeManager fmManager;

    @BindView(R.id.radioGlayout)
    RadioGroup radioGlayout;
    @BindView(R.id.rBtnOne)
    RadioButton rBtnOne;
    @BindView(R.id.rBtnTwo)
    RadioButton rBtnTwo;
    @BindView(R.id.rBtnThree)
    RadioButton rBtnThree;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dlMain)
    DrawerLayout dlMain;
    @BindView(R.id.navView)
    NavigationView navView;
    TextView tvUserName;
    Button btnLoginOrExit;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.main_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;
        L.e(getTaskId() + "----> MainActivity");
        initNav();

        initFragment();
        initRadioGroup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        View headerView = navView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.tvUserName);
        btnLoginOrExit = headerView.findViewById(R.id.btnLoginOrExit);
        btnLoginOrExit.setOnClickListener(this);

        boolean isLogin = SpfAgent.getBoolean(Constant.baseSpf, Constant.isLogin);
        tvUserName.setText(isLogin ?
                SpfAgent.getString(Constant.baseSpf, Constant.userName) :
                ResUtils.getStr(R.string.notLogin));

        btnLoginOrExit.setText(isLogin ? R.string.exitLogin : R.string.clickLogin);

        MenuItem nightModel = navView.getMenu().findItem(R.id.atNightModel);
        nightModel.setIcon(SpfAgent.getBoolean(Constant.baseSpf, NightModeUtils.isNightMode) ? R.drawable.svg_daytime_model : R.drawable.svg_at_night_model);//图标
        int id = SpfAgent.getBoolean(Constant.baseSpf, NightModeUtils.isNightMode) ? R.string.daytimeMode : R.string.nightMode;
        nightModel.setTitle(id);
    }

    /**
     * 解决 activity 启动模式为 singleTask时，intent传值 接收不到问题
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        //获取 Intent而不是用 方法传递的Intent，是因为在 Application 中 设置了activity 生命周期回调时候 用到Intent 缓存了配置信息
        Intent intent1 = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            intent1 = intent1.putExtras(bundle);
            super.onNewIntent(intent1);
            setIntent(intent1);//intent传值 接收不到问题，关键在这句
        } else {
            super.onNewIntent(intent);
        }
    }

    //初始化底部导航按钮
    private void initRadioGroup() {
        //设置 选择器
        TintUtils.setTxtIconLocal(rBtnOne, SelectUtils.getSelector(R.drawable.svg_home_page, 1), 2);
        TintUtils.setTxtIconLocal(rBtnTwo, SelectUtils.getSelector(R.drawable.svg_knowledge_system, 1), 2);
        TintUtils.setTxtIconLocal(rBtnThree, SelectUtils.getSelector(R.drawable.svg_bookmark, 1), 2);

        radioGlayout.setOnCheckedChangeListener((group, checkedId) -> {
            int position = 0;
            boolean useDart = false;
            switch (checkedId) {
                case R.id.rBtnOne:
                    position = 0;
                    useDart = false;
                    break;
                case R.id.rBtnTwo:
                    position = 1;
                    useDart = true;
                    break;
                case R.id.rBtnThree:
                    position = 2;
                    useDart = false;
                    break;
            }
            fmManager.setFragments(position);
            StatusBarContentColor.setStatusTextColor(MainActivity.this, useDart, false);
        });

        rBtnOne.setChecked(true);
    }

    private void initFragment(){
        List<Fragment> mainFragments = new ArrayList<>();
        mainFragments.add(new FragmentOne());
        mainFragments.add(new FragmentTwo());
        mainFragments.add(new FragmentThree());
        fmManager = new FragmentChangeManager(getSupportFragmentManager(), R.id.main_fragemnet_container, mainFragments);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLayout://菜单按钮
                JumpUtils.jump(this, SearchActivity.class, null);
                break;
            case R.id.btnLoginOrExit://登录 or 退出登录
                boolean isLogin = SpfAgent.getBoolean(Constant.baseSpf, Constant.isLogin);
                if (isLogin) {
                    tvUserName.setText(R.string.notLogin);
                    btnLoginOrExit.setText(R.string.clickLogin);

                    ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                    mCache.clear();
                    new SpfAgent(Constant.baseSpf).clear(false);
                } else {
                    JumpUtils.jump(MainActivity.this, LoginActivity.class, null);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        如果DrawerLayout是打开状态则关闭
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed(); 	不要调用父类的方法
            JumpUtils.backDesktop(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icon_layout, menu);
        View menuLayout = menu.findItem(R.id.menuSchedule).getActionView();
        menuLayout.setOnClickListener(this);

        AppCompatImageView imgMenu = menuLayout.findViewById(R.id.imgMenu);
        imgMenu.setImageResource(R.drawable.svg_search);
        return true;
    }

    //    初始化导航视图
    private void initNav() {
        navView.setNavigationItemSelectedListener(item -> {
            dlMain.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.myCollect:
                    JumpUtils.jump(mContext, MyCollectActivity.class, null);
                    break;
                case R.id.myDownLoad:
                    JumpUtils.jump(mContext, DownFileActivity.class, null);
                    break;
                case R.id.atNightModel:
                    NightModeUtils.switchNightMode(mContext);
                    break;
                case R.id.about:
                    JumpUtils.jump(mContext, AboutActivity.class, null);
                    break;
                case R.id.exit:
                    JumpUtils.exitApp(this, StartActivity.class);
                    break;
            }
            return false;
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dlMain, toolbar, 0, 0);
        dlMain.addDrawerListener(toggle);
        toggle.syncState();//设置左上角显示三道横线
    }
}
