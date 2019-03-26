package com.gcstorage.circle.details;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.widget.refresh.OnRefreshLoadMoreListener;
import com.gcstorage.circle.CircleFragmentAdapter;
import com.gcstorage.circle.R;
import com.gcstorage.circle.R2;
import com.gcstorage.circle.bean.CircleListBean;
import com.gcstorage.circle.bean.CommentListBean;
import com.gcstorage.circle.bean.LyCircleListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * DESCRIPTION：帖子详情 activity
 * Created by fangs on 2019/3/21 10:17.
 */
public class CircleDetailsActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R2.id.rl_reward)
    RelativeLayout rlReward;
    @BindView(R2.id.integral)
    TextView integral;
    @BindView(R2.id.iv_cron)
    ImageView ivCron;

    @BindView(R2.id.rvHierarchy)
    RecyclerView rvHierarchy;
    CircleFragmentAdapter rvAdapter;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.circle_act_details;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        LyCircleListBean circleListBean = (LyCircleListBean) bundle.getSerializable("LyCircleListBean");

        initRvAdapter(circleListBean);

    }

    @SuppressLint("CheckResult")
    private void initRvAdapter(LyCircleListBean lyCircleListBean) {
        rvAdapter = new CircleFragmentAdapter(this, new ArrayList<>());
        rvAdapter.setDetails(true);
        rvAdapter.setIntegralAnimListener(() -> animation());//设置积分动画回调
        rvHierarchy.setLayoutManager(new LinearLayoutManager(this));
        rvHierarchy.setItemAnimator(new FadeItemAnimator());
        rvHierarchy.setAdapter(rvAdapter);

        Observable.create((ObservableOnSubscribe<List<CircleListBean>>) emitter -> {
            List<CircleListBean> adapterData = new ArrayList<>();
            List<CommentListBean> comment_list = lyCircleListBean.getComment_list();
            adapterData.add(new CircleListBean(lyCircleListBean));

            for (int i = 0; i < comment_list.size(); i++) {
                CommentListBean commentListBean = comment_list.get(i);
                adapterData.add(new CircleListBean(1, commentListBean, i == comment_list.size() - 1));
            }
            emitter.onNext(adapterData);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())//指定的是上游发送事件的线程
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<List<CircleListBean>>() {
              @Override
              public void accept(List<CircleListBean> dataList) throws Exception {
                  rvAdapter.setmDatas(dataList);
                  rvAdapter.notifyDataSetChanged();
              }
          });
    }




    private void animation() {
        rlReward.setVisibility(View.VISIBLE);
        ivCron.setBackgroundResource(R.drawable.animation_integral_cron);
        AnimationDrawable drawable = (AnimationDrawable) ivCron.getBackground();
        drawable.start();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(integral, "alpha", 0, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(integral, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(integral, "scaleY", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setDuration(500);
        set.start();
        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rlReward.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
