package com.fy.baselibrary.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.widget.refresh.RefreshAnimView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 列表刷新 视图（自定义组合控件）
 * Created by fangs on 2017/11/22.
 */
public class TransformerView extends RefreshAnimView {

    ConstraintLayout topRefreshLayout;
    AppCompatImageView imgArrow;
    AppCompatImageView imgTurn;
    TextView tvLoadTip;
    TextView tvDate;

    public TransformerView(Context context) {
        this(context, null);
    }

    public TransformerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_transformer, this, true);

        topRefreshLayout = view.findViewById(R.id.topRefreshLayout);
        imgArrow = view.findViewById(R.id.imgArrow);
        imgTurn = view.findViewById(R.id.imgTurn);
        tvLoadTip = view.findViewById(R.id.tvLoadTip);
        tvDate = view.findViewById(R.id.tvDate);

        idle();
    }

    @Override
    public void idle() {
        tvLoadTip.setText(R.string.idle);
        tvDate.setText(TimeUtils.Long2DataString(System.currentTimeMillis(), "yyyy-MM-dd  hh:mm:ss"));

        imgTurn.setVisibility(GONE);
        imgArrow.setVisibility(VISIBLE);

        imgArrow.animate()
                .setInterpolator(new BounceInterpolator())
                .setDuration(300)
                .rotation(0)
                .start();
    }

    @Override
    public void ready() {
        tvLoadTip.setText(R.string.ready);

        imgTurn.setVisibility(GONE);
        imgArrow.setVisibility(VISIBLE);

        imgArrow.animate()
                .setInterpolator(new BounceInterpolator())
                .setDuration(300)
                .rotation(180)
                .start();


    }

    @Override
    public void triggered() {
        tvLoadTip.setText(R.string.data_loading);
        imgArrow.setVisibility(INVISIBLE);
        imgTurn.setVisibility(VISIBLE);

        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.refresh_rotate);
        animator.setTarget(imgTurn);
        animator.start();
    }

}
