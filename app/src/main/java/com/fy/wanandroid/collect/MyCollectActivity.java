package com.fy.wanandroid.collect;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.ArticleBean;
import com.fy.wanandroid.entity.Bookmark;
import com.fy.wanandroid.main.fragment.AdapterOne;
import com.fy.wanandroid.main.fragment.DiffCallBack;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;
import com.fy.wanandroid.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的收藏 列表
 * Created by fangs on 2018/4/19.
 */
public class MyCollectActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;
    AdapterOne rvAdapter;
    /**
     * 当前显示的页码(从 0 开始)
     */
    int pageNum;


    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.fragment_main_one;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRvAdapter();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    @Override
    public void onClick(View v) {
    }

    private void initRvAdapter() {
        rvAdapter = new AdapterOne(this, new ArrayList<>());
        rvAdapter.setDelete(true);
        rvAdapter.setItemClickListner(view -> {
            ArticleBean.DatasBean article = (ArticleBean.DatasBean) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", new Bookmark(article.getTitle(), article.getLink()));
            JumpUtils.jump(this, WebViewActivity.class, bundle);
        });

        rvArticle.setLayoutManager(new LinearLayoutManager(this));
        rvArticle.setItemAnimator(new FadeItemAnimator());
        rvArticle.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(this));

        rvArticle.setAdapter(rvAdapter);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
                getData();
            }
        });
    }

    private void getData() {
        RequestUtils.create(ApiService.class)
                .getCollectList(pageNum)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
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
                    protected void updateLayout(int flag) {
                        epl.stop();
                    }
                });
    }
}
