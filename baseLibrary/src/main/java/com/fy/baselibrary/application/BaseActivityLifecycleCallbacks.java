package com.fy.baselibrary.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.load.down.DownManager;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * activity 生命周期回调 (api 14+)
 * 注意：使用本框架 activity 与 activity 之间传递数据 统一使用 Bundle
 * Created by fangs on 2017/5/18.
 */
public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public static String TAG = "ActivityCallbacks";
    int designWidth;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public BaseActivityLifecycleCallbacks(int designWidth) {
        this.designWidth = designWidth;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        L.e(TAG, activity.getClass().getName() + "--Create()   " + activity.getTaskId());

        if (!(activity instanceof StartActivity) && Constant.isRunState.equals("isRunState")) {
            Intent intent = new Intent(activity, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            //            activity.finish();
            return;
        } else {
            Constant.isRunState = "";
        }

        ScreenUtils.setCustomDensity(activity, designWidth);

        BaseActivityBean activityBean = new BaseActivityBean();
        IBaseActivity act = null;
        if (activity instanceof IBaseActivity) {
            act = (IBaseActivity) activity;

            if (act.setView() != 0) {
                activity.setContentView(R.layout.activity_base);
                LinearLayout linearLRoot = activity.findViewById(R.id.linearLRoot);

                if (act.isShowHeadView()) initHead(activity);

                View view = LayoutInflater.from(activity).inflate(act.setView(), null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
                linearLRoot.addView(view, params);

                StatusLayoutManager slManager = initSLManager(activity);
                if (null != slManager) activityBean.setSlManager(slManager);
            }

            act.setStatusBar(activity);
        }

//        注册屏幕旋转监听
        BaseOrientoinListener orientoinListener = new BaseOrientoinListener(activity);
        boolean autoRotateOn = (android.provider.Settings.System.getInt(activity.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1);

        //检查系统是否开启自动旋转
        if (autoRotateOn) {
            orientoinListener.enable();
        }

        activityBean.setOrientoinListener(orientoinListener);

        //设置 黄油刀 简化 Android 样板代码
        activityBean.setUnbinder(ButterKnife.bind(activity));

        activity.getIntent().putExtra("ActivityBean", activityBean);

        //基础配置 执行完成，再执行 初始化 activity 操作
        if (null != act) act.initData(activity, savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Start()");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Resume()");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Pause()");
        RequestUtils.clearDispos();
        DownManager.getInstentce().clieanDownData();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Stop()");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        L.e(TAG, activity.getClass().getName() + "--SaveInstanceState()");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Destroy()");

        BaseActivityBean activityBean = (BaseActivityBean) activity.getIntent()
                .getSerializableExtra("ActivityBean");

        if (null != activityBean) {
            //解绑定 黄油刀
            if (null != activityBean.getUnbinder()) activityBean.getUnbinder().unbind();
            //销毁 屏幕旋转监听
            if (null != activityBean.getOrientoinListener())
                activityBean.getOrientoinListener().disable();
        }
    }

    /**
     * 初始化 toolbar
     *
     * @param activity
     */
    private void initHead(Activity activity) {

        ViewStub vStubTitleBar = activity.findViewById(R.id.vStubTitleBar);
        vStubTitleBar.inflate();
        //这里全局给Activity设置toolbar和title mate
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        if (ConfigUtils.isTitleCenter()) {
            toolbar.setTitle("");
            TextView toolbarTitle = activity.findViewById(R.id.toolbarTitle);
            toolbarTitle.setText(activity.getTitle());
            toolbarTitle.setTextColor(ResUtils.getColor(ConfigUtils.getTitleColor()));
            toolbarTitle.setVisibility(View.VISIBLE);
        } else {
            toolbar.setTitle(activity.getTitle());
        }

        if (activity instanceof AppCompatActivity) {
            AppCompatActivity act = (AppCompatActivity) activity;
            //设置导航图标要在setSupportActionBar方法之后
            act.setSupportActionBar(toolbar);
            //在Toolbar左边显示一个返回按钮
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //替换toolbar 自带的返回按钮
            if (ConfigUtils.getBackImg() > 0) toolbar.setNavigationIcon(ConfigUtils.getBackImg());
            //设置返回按钮监听事件
            toolbar.setNavigationOnClickListener(v -> JumpUtils.exitActivity(act));
            if (ConfigUtils.getBgColor() > 0)
                toolbar.setBackgroundColor(ResUtils.getColor(ConfigUtils.getBgColor()));
        }
    }


    /**
     * 设置 多状态视图 管理器
     */
    protected StatusLayoutManager initSLManager(Activity activity) {
        StatusLayoutManager slManager = null;

        if (activity instanceof IreTryActivity) {
            IreTryActivity act = (IreTryActivity) activity;

            slManager = StatusLayoutManager.newBuilder(activity, activity)
                    .setShowHeadView(act.isShowHeadView())
                    .errorView(R.layout.state_include_error)
                    .netWorkErrorView(R.layout.state_include_networkerror)
                    .emptyDataView(R.layout.state_include_emptydata)
                    .retryViewId(R.id.tvTry)
                    .onRetryListener(act::reTry)
                    .build();

            slManager.showContent();
        }

        return slManager;
    }

}
