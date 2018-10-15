package com.fy.baselibrary.statuslayout;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.fy.baselibrary.R;
import com.fy.baselibrary.utils.L;

/**
 * 多状态布局 替换逻辑工具类
 * Created by fangs on 2017/12/18.
 */
public class LoadSirUtil {

    /**
     * 替换逻辑
     * @param target
     * @return
     */
    public static TargetContext getTargetContext(Object target) {
        Context context;
        ViewGroup contentParent;
        View content;

        if (target instanceof View) {
            View view = (View) target;
            context = view.getContext();
            content = view;
            contentParent = (ViewGroup) view.getParent();

            //此处判断只是为了不崩溃
            if (null == contentParent){
                if (target instanceof ViewGroup){
                    ViewGroup vg = (ViewGroup) target;
                    contentParent = vg.getParent() == null ? vg : (ViewGroup) vg.getParent();
                } else {
                    throw new IllegalArgumentException("Must have a parent view");
                }
            }
        } else {
            throw new IllegalArgumentException("Must have a parent view");
        }

        int childCount = contentParent == null ? 0 : contentParent.getChildCount();

        return new TargetContext(context, contentParent, content, childCount);
    }

    /**
     * 设置 多状态视图 管理器
     * @param contextObj
     * @param target
     */
    public static StatusLayoutManager initStatusLayout(Object contextObj, Object target){

        Context context;
        if (contextObj instanceof Activity) {
            context = (Activity) contextObj;
        } else if (contextObj instanceof Fragment) {
            context = ((Fragment) contextObj).getContext();
        } else {
            throw new IllegalArgumentException("The Context must be is Activity or Fragment.");
        }

        //获取 点击重试回调接口
        StatusLayout.OnRetryListener listener = contextObj instanceof StatusLayout.OnRetryListener ?
                (StatusLayout.OnRetryListener) contextObj : () -> L.e("retry()");

        StatusLayoutManager slManager = StatusLayoutManager.newBuilder(context, target)
                .errorView(R.layout.state_include_error)
                .netWorkErrorView(R.layout.state_include_networkerror)
                .emptyDataView(R.layout.state_include_emptydata)
                .retryViewId(R.id.tvTry)
                .onRetryListener(listener)
                .build();
        slManager.showContent();

        return slManager;
    }
}
