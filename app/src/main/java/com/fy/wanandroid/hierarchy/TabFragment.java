package com.fy.wanandroid.hierarchy;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.fy.wanandroid.R;
import com.fy.wanandroid.api.ApiService;
import com.fy.wanandroid.entity.ArticleBean;
import com.fy.wanandroid.entity.Bookmark;
import com.fy.wanandroid.main.fragment.AdapterOne;
import com.fy.wanandroid.main.fragment.DiffCallBack;
import com.fy.wanandroid.web.WebViewActivity;

/**
 * TabLayout Fragment
 * Created by fangs on 2018/4/24.
 */
public class TabFragment extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
    @BindView(R.id.rvHierarchy)
    RecyclerView rvHierarchy;
    AdapterOne rvAdapter;

    /** 当前显示的页码(从 0 开始) */
    int pageNum;
    int cid;

    public static TabFragment getInstentce(int id){
        TabFragment fragment = new TabFragment();
        fragment.cid = id;
        return fragment;
    }

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_hierarchy;
    }

    @Override
    protected void baseInit() {
        initRvAdapter();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRvAdapter() {
        rvAdapter = new AdapterOne(getContext(), new ArrayList<>());
        rvAdapter.setChangeItemListener((position) -> rvAdapter.notifyItemChanged(position, ""));
        rvAdapter.setItemClickListner(view -> {
            ArticleBean.DatasBean article = (ArticleBean.DatasBean) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", new Bookmark(article.getTitle(), article.getLink()));
            JumpUtils.jump(this, WebViewActivity.class, bundle);
        });

        rvHierarchy.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHierarchy.setItemAnimator(new FadeItemAnimator());
        rvHierarchy.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getContext()));

        rvHierarchy.setAdapter(rvAdapter);

        epl.addOnPullListenerAdapter(new EasyPullLayout.OnPullListenerAdapter() {
            @Override
            public void onPull(int type, float fraction, boolean changed) {
                if (!changed) return;

                if (type == EasyPullLayout.TYPE_EDGE_TOP) {
                    if (fraction == 1f) topView.ready();
                    else topView.idle();
                }
            }

            @Override
            public void onTriggered(int type) {
                if (type == EasyPullLayout.TYPE_EDGE_TOP) {
                    topView.triggered(getContext());
                    getData();
                } else if (type == EasyPullLayout.TYPE_EDGE_BOTTOM) {

                }
            }

            @Override
            public void onRollBack(int rollBackType) {
                if (rollBackType == EasyPullLayout.ROLL_BACK_TYPE_TOP) {
                    topView.idle();
                }
            }
        });
    }

    private void getData() {
        RequestUtils.create(ApiService.class)
                .getTreeArticle(pageNum, cid)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<ArticleBean>() {
                    @Override
                    protected void onSuccess(ArticleBean articleBean) {
                        if (pageNum == 0) {
                            List<ArticleBean.DatasBean> list = articleBean.getDatas();
                            if (null != list) {
                                DiffUtil.DiffResult diffResult = DiffUtil
                                        .calculateDiff(new DiffCallBack(rvAdapter.getmDatas(), list), true);

                                diffResult.dispatchUpdatesTo(rvAdapter);
                                rvAdapter.setmDatas(list);
                            }
                        }
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        epl.stop();
                    }
                });
    }
}
