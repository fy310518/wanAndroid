package com.fy.baselibrary.statuslayout;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.fy.baselibrary.R;

/**
 * Description:TODO
 * Create Time:2017/9/4 16:24
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class LoadSirUtil {

    /**
     * 替换逻辑
     * @param target
     * @return
     */
    public static TargetContext getTargetContext(Object target) {
        ViewGroup contentParent;
        Context context;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(R.id.linearLRoot);
        } else if (target instanceof View) {
            View view = (View) target;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("The target must be within Activity, Fragment, View.");
        }

        int childIndex = 0;
        int childCount = contentParent == null ? 0 : contentParent.getChildCount();

        View oldContent;
        if (target instanceof View) {
            oldContent = (View) target;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    childIndex = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent != null ? contentParent.getChildAt(0) : null;
        }

        if (oldContent == null) {
            throw new IllegalArgumentException(String.format("enexpected error when register LoadSir in %s",
                    target.getClass().getSimpleName()));
        }

        return new TargetContext(context, contentParent, oldContent, childIndex);
    }
}
