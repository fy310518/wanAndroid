package com.fy.baselibrary.application;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        L.d(TAG, "Create()");

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

//        注册屏幕旋转监听
        BaseOrientoinListener orientoinListener = new BaseOrientoinListener(activity);
        boolean autoRotateOn =
                (android.provider.Settings.System
                        .getInt(activity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
        //检查系统是否开启自动旋转
        if (autoRotateOn) orientoinListener.enable();

        activityBean.setOrientoinListener(orientoinListener);

        activity.getIntent().putExtra("ActivityBean", activityBean);

        //基础配置 执行完成，再执行 初始化 activity 操作
        if (null != act) act.initData(activity, savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "Start()");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "Resume()");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        L.d(TAG, "Pause()");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        L.d(TAG, "Stop()");
        RequestUtils.clearDispos();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        L.d(TAG, "SaveInstanceState()");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        L.d(TAG, "Destroy()");

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
        if (null != toolbar) { //找到 Toolbar 并且替换 Actionbar
            if (activity instanceof AppCompatActivity) {
                ((AppCompatActivity) activity).setSupportActionBar(toolbar);
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.setActionBar((android.widget.Toolbar) activity.findViewById(R.id.toolbar));
                    activity.getActionBar().setDisplayShowTitleEnabled(false);
                }
            }
        }

        //找到 Toolbar 的标题栏并设置标题名
        TextView tvTitle = activity.findViewById(R.id.tvTitle);
        if (null != tvTitle) tvTitle.setText(activity.getTitle());

        //找到 Toolbar 的返回按钮,并且设置点击事件,点击关闭这个 Activity
        TextView tvBack = activity.findViewById(R.id.tvBack);
        if (null != tvBack) tvBack.setOnClickListener(v ->
            JumpUtils.exitActivity((AppCompatActivity) activity));
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
