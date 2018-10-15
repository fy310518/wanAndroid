package com.fy.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fy.baselibrary.statuslayout.LoadSirUtil;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.statuslayout.StatusLayout;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.cache.ACache;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 * Created by fangs on 2017/4/26.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener, StatusLayout.OnRetryListener, StatusLayout.OnSetStatusView {
    public static final String TAG = "Fragment";

    protected ACache mCache;

    protected AppCompatActivity mContext;
    protected StatusLayoutManager slManager;

    protected View mRootView;
    protected Unbinder unbinder;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            if (setContentLayout() > 0){
                mRootView = inflater.inflate(setContentLayout(), container, false);
                unbinder = ButterKnife.bind(this, mRootView);
                slManager = LoadSirUtil.initStatusLayout(this, setStatusView());
            }

            baseInit();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        L.e(TAG, "onCreateView()");
        return mRootView;
    }

    @Override
    public void onClick(View view) {}

    @Override
    public void onRetry() {}

    @Override
    public View setStatusView(){return mRootView;}

    protected void baseInit() {}

    /**
     * 设置 fragment 视图
     * @return
     */
    protected abstract int setContentLayout();



    //Fragment生命周期管理
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            onPause();
        } else {// 重新显示到最前端中
            onResume();
        }
    }


    @Override//当Activity中的onCreate方法执行完后调用
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.e(TAG, "onActivityCreated()");
    }

    @Override//Fragment和Activity建立关联的时候调用
    public void onAttach(Context context) {
        super.onAttach(context);
        L.e(TAG, "onAttach()");

        this.mContext = (AppCompatActivity) context;
        mCache = ACache.get(mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e(TAG, "onCreate()");
    }


    @Override
    public void onStart() {
        super.onStart();
        L.e(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        L.e(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.e(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        L.e(TAG, "onStop()");
    }

    @Override//Fragment中的布局被移除时调用
    public void onDestroyView() {
        super.onDestroyView();
        L.e(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG, "onDestroy()");

        if (null != unbinder){
            unbinder.unbind();
        }
    }

    @Override//Fragment和Activity解除关联的时候调用
    public void onDetach() {
        super.onDetach();
        L.e(TAG, "onDetach()");
    }
}
