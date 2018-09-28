package com.fy.baselibrary.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.R;

/**
 * 列表加载更多 视图（自定义组合控件）
 * Created by fangs on 2018/9/28 17:28.
 */
public class EasyBottomLoadMoreView extends RefreshAnimView{

    AppCompatImageView imgTurn;
    TextView tvLoadTip;

    public EasyBottomLoadMoreView(Context context) {
        super(context);
    }

    public EasyBottomLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyBottomLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_load_more, this, true);
        imgTurn = view.findViewById(R.id.imgTurn);
        tvLoadTip = view.findViewById(R.id.tvLoadTip);
    }

    @Override
    public void idle() {
        imgTurn.setVisibility(GONE);
    }

    @Override
    public void ready() {
        imgTurn.setVisibility(GONE);
    }

    @Override
    public void triggered() {
        imgTurn.setVisibility(VISIBLE);

        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.refresh_rotate);
        animator.setTarget(imgTurn);
        animator.start();
    }
}
