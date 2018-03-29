package com.fy.baselibrary.statuslayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * 辅助 多状态布局 类
 */
public class TargetContext implements Serializable{
    private Context context;
    private ViewGroup parentView;
    private View oldContent;
    private int childIndex;

    public TargetContext(Context context, ViewGroup parentView, View oldContent, int childIndex) {
        this.context = context;
        this.parentView = parentView;
        this.oldContent = oldContent;
        this.childIndex = childIndex;
    }

    public Context getContext() {
        return context;
    }

    View getOldContent() {
        return oldContent;
    }

    int getChildIndex() {
        return childIndex;
    }

    ViewGroup getParentView() {
        return parentView;
    }
}
