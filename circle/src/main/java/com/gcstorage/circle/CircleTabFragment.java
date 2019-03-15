package com.gcstorage.circle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.fy.baselibrary.widget.refresh.OnRefreshLoadMoreListener;
import com.gcstorage.circle.bean.LyCircleListBean;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * TabLayout Fragment
 * Created by fangs on 2018/4/24.
 */
public class CircleTabFragment extends BaseFragment {
    private String title;
    private String mode;
    private String type;

    @BindView(R2.id.rl_reward)
    RelativeLayout rlReward;
    @BindView(R2.id.integral)
    TextView integral;
    @BindView(R2.id.iv_cron)
    ImageView ivCron;

    @BindView(R2.id.epl)
    EasyPullLayout epl;
    @BindView(R2.id.rvHierarchy)
    RecyclerView rvHierarchy;
    CircleFragmentAdapter rvAdapter;

    /** 当前显示的页码(从 0 开始) */
    int pageNum;

    public static CircleTabFragment getInstentce(String title, String mode, String type){
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("mode", mode);
        args.putString("type", type);

        CircleTabFragment fragment = new CircleTabFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int setContentLayout() {
        return R.layout.circle_fragment;
    }

    @Override
    protected void baseInit() {
        init();

        initRvAdapter();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void init(){
        Bundle bundle = getArguments();
        if (null != bundle) {
            title = bundle.getString("title");
            mode = bundle.getString("mode");
            type = bundle.getString("type");
        }
    }

    private void initRvAdapter() {
        rvAdapter = new CircleFragmentAdapter(getActivity(), new ArrayList<>());
        rvAdapter.setIntegralAnimListener(() -> animation());//设置积分动画回调
//        rvAdapter.setItemClickListner(view -> {
//            ArticleBean.DatasBean article = (ArticleBean.DatasBean) view.getTag();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Bookmark", new Bookmark(article.getTitle(), article.getLink()));
//            JumpUtils.jump(this, WebViewActivity.class, bundle);
//        });
//
        rvHierarchy.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHierarchy.setItemAnimator(new FadeItemAnimator());
        rvHierarchy.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getContext()));

        rvHierarchy.setAdapter(rvAdapter);

        epl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageNum++;
                getData();
            }

            @Override
            public void onRefresh() {
                pageNum = 0;
                getData();
            }
        });
    }

    private void getData() {
        ArrayMap<String, Object> param = new ArrayMap<>();
        param.put("action", "postlist");
        param.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        param.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));
        param.put("type", type);
        param.put("mode", mode);
        param.put("userid", "");
        param.put("handle", pageNum + "");//上下拉取数据：0下拉获取最新的，1上拉获取历史的
        param.put("postid", "");//分页帖子id，没有则从最新的帖子开始查看

        RequestUtils.create(ApiService.class)
                .lypostlist(param)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(mContext))
                .subscribe(new NetCallBack<List<LyCircleListBean>>() {
                    @Override
                    protected void onSuccess(List<LyCircleListBean> dataList) {
                        L.e("大王");
                        if (null == dataList || dataList.size() == 0) return;

                        if (pageNum == 0) {
                            rvAdapter.setmDatas(dataList);
                            rvAdapter.notifyDataSetChanged();
                        } else {
                            int notifyPosition = rvAdapter.getItemCount();
                            rvAdapter.addData(dataList);
                            rvAdapter.notifyItemRangeChanged(notifyPosition, dataList.size());
                        }
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        super.updataLayout(flag);
                        epl.stop();
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
