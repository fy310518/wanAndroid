package com.fy.baselibrary.application;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;

import butterknife.ButterKnife;

/**
 * activity 生命周期回调 (api 14+)
 * 注意：使用本框架 activity 与 activity 之间传递数据 统一使用 Bundle
 * Created by fangs on 2017/5/18.
 */
public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public static String TAG = "ActivityCallbacks";

    RudenessScreenAdapter screenAdapter;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public BaseActivityLifecycleCallbacks(RudenessScreenAdapter screenAdapter) {
        this.screenAdapter = screenAdapter;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        L.e(TAG, activity.getClass().getName() + "--Create()");

        BaseActivityBean activityBean = new BaseActivityBean();

        IBaseActivity act = null;
        if (activity instanceof IBaseActivity) {
            act = (IBaseActivity) activity;

            if (act.setView() != 0){
                activity.setContentView(R.layout.activity_base);
                LinearLayout linearLRoot = activity.findViewById(R.id.linearLRoot);

                if (act.isShowHeadView())initHead(activity);

                View view = LayoutInflater.from(activity).inflate(act.setView(), null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
                linearLRoot.addView(view, params);

                StatusLayoutManager slManager = initSLManager(activity);
                if (null != slManager) activityBean.setSlManager(slManager);
            }

            act.setStatusBar(activity);
        }

        //设置 黄油刀 简化 Android 样板代码
        activityBean.setUnbinder(ButterKnife.bind(activity));

        activity.getIntent().putExtra("ActivityBean", activityBean);

        //基础配置 执行完成，再执行 初始化 activity 操作
        if (null != act) act.initData(activity, savedInstanceState);

        //暴力适配
        //通常情况下application与activity得到的resource虽然不是一个实例，但是displayMetrics是同一个实例，只需调用一次即可
        //为了面对一些不可预计的情况以及向上兼容，分别调用一次较为保险
        RudenessScreenAdapter.resetDensity(BaseApp.getAppCtx(), screenAdapter.designWidth);
        RudenessScreenAdapter.resetDensity(activity, screenAdapter.designWidth);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Start()");

        RudenessScreenAdapter.resetDensity(BaseApp.getAppCtx(), screenAdapter.designWidth);
        RudenessScreenAdapter.resetDensity(activity, screenAdapter.designWidth);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Resume()");

        RudenessScreenAdapter.resetDensity(BaseApp.getAppCtx(), screenAdapter.designWidth);
        RudenessScreenAdapter.resetDensity(activity, screenAdapter.designWidth);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        L.e(TAG, activity.getClass().getName() + "--Pause()");
        RequestUtils.clearDispos();
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
            if (null != activityBean.getOrientoinListener())activityBean.getOrientoinListener().disable();
        }
    }

    /**
     * 初始化 toolbar
     * @param activity
     */
    private void initHead(Activity activity) {

        ViewStub vStubTitleBar = activity.findViewById(R.id.vStubTitleBar);
        vStubTitleBar.inflate();

        //这里全局给Activity设置toolbar和title
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(activity.getTitle());
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity act = (AppCompatActivity) activity;
            //设置导航图标要在setSupportActionBar方法之后
            act.setSupportActionBar(toolbar);
//            在Toolbar左边显示一个返回按钮
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            设置返回按钮监听事件
            toolbar.setNavigationOnClickListener(v -> JumpUtils.exitActivity(act));
        }
    }


    /**
     * 设置 多状态视图 管理器
     */
    protected StatusLayoutManager initSLManager(Activity activity) {
        StatusLayoutManager slManager = null;

        if (activity instanceof IBaseActivity) {
            IBaseActivity act = (IBaseActivity) activity;

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
