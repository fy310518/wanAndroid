package com.fy.baselibrary.statuslayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;

/**
 * 多状态视图 管理类
 * Created by fangs on 2017/12/15.
 */
public class StatusLayoutManager implements Serializable{

    /** 内容id */
    public static final int LAYOUT_CONTENT_ID = 0;

    /** 异常id */
    public static final int LAYOUT_ERROR_ID = 1;

    /** 网络异常id */
    public static final int LAYOUT_NETWORK_ERROR_ID = 2;

    /** 空数据id */
    public static final int LAYOUT_EMPTYDATA_ID = 3;

    /** 请求失败 标记 */
    public static final int REQUEST_FAIL = 1006;

    /** 存放布局集合 */
    private SparseArray<View> layoutSparseArray = new SparseArray();

    TargetContext targetContext;
    final Context context;

    int netWorkErrorRetryViewId;
    int errorRetryViewId;
    int emptyDataRetryViewId;

    final int retryViewId;

    final StatusLayout.OnRetryListener onRetryListener;

    public StatusLayoutManager(Builder builder) {
        this.context = builder.context;
        this.targetContext   = builder.targetContext;

        this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
        this.emptyDataRetryViewId = builder.emptyDataRetryViewId;
        this.errorRetryViewId = builder.errorRetryViewId;
        this.retryViewId = builder.retryViewId;
        this.onRetryListener = builder.onRetryListener;
    }

    public boolean addLayoutResId(@LayoutRes int layoutResId, int id) {
        View resView = LayoutInflater.from(context).inflate(layoutResId, null);
        layoutSparseArray.put(id, resView);

        return true;
    }

    /** 显示内容 */
    public void showContent() {
        showHideViewById(LAYOUT_CONTENT_ID);
    }

    /** 显示空数据 */
    public void showEmptyData() {
        if(inflateLayout(LAYOUT_EMPTYDATA_ID))
            showHideViewById(LAYOUT_EMPTYDATA_ID);
    }

    /** 显示网络异常 */
    public void showNetWorkError() {
        if(inflateLayout(LAYOUT_NETWORK_ERROR_ID))
            showHideViewById(LAYOUT_NETWORK_ERROR_ID);
    }

    /** 显示异常 */
    public void showError() {
        if(inflateLayout(LAYOUT_ERROR_ID))
            showHideViewById(LAYOUT_ERROR_ID);
    }

    /**
     * 根据ID 显示或隐藏布局
     * @param id
     */
    private void showHideViewById(int id) {
        View vgBody = targetContext.getContent();
        vgBody.setVisibility(id == LAYOUT_CONTENT_ID ? View.VISIBLE : View.GONE);

        if (targetContext.getParentView().getChildCount() > targetContext.getChildCount()){
            //显示 指定id的 view 前 清理最后的view
            targetContext.getParentView().removeViewAt(targetContext.getParentView().getChildCount() - 1);
        }

        if (id == LAYOUT_CONTENT_ID) return;


        for (int i = 0; i < layoutSparseArray.size(); i++) {
            int key = layoutSparseArray.keyAt(i);
            //显示该view
            if (key == id) {
                View valueView = layoutSparseArray.valueAt(i);
                targetContext.getParentView().addView(valueView, vgBody.getLayoutParams());
            }
        }
    }

    private boolean inflateLayout(int id) {
        boolean isShow = true;
        if (layoutSparseArray.get(id) != null) return isShow;
        switch (id) {
            case LAYOUT_NETWORK_ERROR_ID:
                isShow = addLayoutResId(netWorkErrorRetryViewId, LAYOUT_NETWORK_ERROR_ID);
                break;
            case LAYOUT_ERROR_ID:
                isShow = addLayoutResId(errorRetryViewId, LAYOUT_ERROR_ID);
                break;
            case LAYOUT_EMPTYDATA_ID:
                isShow = addLayoutResId(emptyDataRetryViewId, LAYOUT_EMPTYDATA_ID);
                break;
        }

        retryLoad(layoutSparseArray.get(id), id);
        return isShow;
    }

    /** 重试加载 */
    public void retryLoad(View view, int id) {
        View retryView = view.findViewById(retryViewId != 0 ? retryViewId : id);

        if (retryView == null || onRetryListener == null) return;

        retryView.setOnClickListener(v -> onRetryListener.onRetry());
    }

    public static Builder newBuilder(Context context, Object target) {
        return new Builder(context, target);
    }


    public static final class Builder {

        TargetContext targetContext;
        private Context context;

        private int netWorkErrorRetryViewId;//网络错误 布局文件ID
        private int emptyDataRetryViewId;//空数据 布局文件ID
        private int errorRetryViewId;// 请求错误（失败）布局文件ID

        private int retryViewId;//请求错误或网络错误时候 的刷新按钮

        private StatusLayout.OnRetryListener onRetryListener;

        /**
         *
         * @param context
         * @param target    activity 或者 View
         */
        public Builder(Context context, Object target) {
            this.context = context;
            this.targetContext = LoadSirUtil.getTargetContext(target);
        }

        public Builder netWorkErrorView(int netWorkErrorRetryViewId) {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId;
            return this;
        }

        public Builder emptyDataView(int emptyDataRetryViewId) {
            this.emptyDataRetryViewId = emptyDataRetryViewId;
            return this;
        }

        public Builder errorView(int errorRetryViewId) {
            this.errorRetryViewId = errorRetryViewId;
            return this;
        }

        /**
         * 设置出现错误时 显示界面的 刷新按钮view id
         * @param retryViewId
         * @return
         */
        public Builder retryViewId(int retryViewId) {
            this.retryViewId = retryViewId;
            return this;
        }

        public Builder onRetryListener(StatusLayout.OnRetryListener onRetryListener) {
            this.onRetryListener = onRetryListener;
            return this;
        }

        /**
         * 返回 多状态视图 管理类
         * @return
         */
        public StatusLayoutManager build() {
            return new StatusLayoutManager(this);
        }
    }

}
