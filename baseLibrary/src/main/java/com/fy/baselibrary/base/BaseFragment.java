package com.fy.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fy.baselibrary.utils.cache.ACache;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 * Created by fangs on 2017/4/26.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "Fragment";

    protected ACache mCache;

    protected AppCompatActivity mContext;
    protected View mRootView;
    protected Unbinder unbinder;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(setContentLayout(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);

            baseInit();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        Log.e(TAG, "onCreateView()");
        return mRootView;
    }

    @Override
    public void onClick(View view) {
    }

    protected void baseInit() {
    }

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
        Log.e(TAG, "onActivityCreated()");
    }

    @Override//Fragment和Activity建立关联的时候调用
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach()");

        this.mContext = (AppCompatActivity) context;
        mCache = ACache.get(mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override//Fragment中的布局被移除时调用
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");

        if (null != unbinder){
            unbinder.unbind();
        }
    }

    @Override//Fragment和Activity解除关联的时候调用
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach()");
    }
}
