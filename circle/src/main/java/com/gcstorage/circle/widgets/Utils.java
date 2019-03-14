package com.gcstorage.circle.widgets;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author KCrason
 * @date 2018/5/6
 */
public class Utils {

    @SuppressLint("CheckResult")
    public static void timerTranslation(OnTimerResultListener onTimerResultListener) {
        Single.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            if (onTimerResultListener != null) {
                onTimerResultListener.onTimerResult();
            }
        });
    }

    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static void hideSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }
    }

    public static void startAlphaAnimation(View view, boolean isShowTranslation) {
        if (isShowTranslation && view != null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.5f, 1f);
            valueAnimator.addUpdateListener(animation -> view.setAlpha((Float) animation.getAnimatedValue()));
            valueAnimator.setDuration(500).start();
        }
    }



    public static SpannableStringBuilder makeSingleCommentSpan(Context context, String childUserName, String commentContent) {
        String richText = String.format("%s: %s", childUserName, commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        if (!TextUtils.isEmpty(childUserName)) {
            builder.setSpan(new TextClickSpan(context, childUserName), 0, childUserName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static SpannableStringBuilder makeReplyCommentSpan(Context context, String parentUserName, String childUserName, String commentContent) {
        String richText = String.format("%s回复%s: %s", parentUserName, childUserName, commentContent);
        SpannableStringBuilder builder = new SpannableStringBuilder(richText);
        int parentEnd = 0;
        if (!TextUtils.isEmpty(parentUserName)) {
            parentEnd = parentUserName.length();
            builder.setSpan(new TextClickSpan(context, parentUserName), 0, parentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(childUserName)) {
            int childStart = parentEnd + 2;
            int childEnd = childStart + childUserName.length();
            builder.setSpan(new TextClickSpan(context, childUserName), childStart, childEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
}
