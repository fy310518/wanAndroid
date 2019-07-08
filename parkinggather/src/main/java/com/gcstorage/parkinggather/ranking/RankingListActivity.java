package com.gcstorage.parkinggather.ranking;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.GridItemDecoration;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.NetCallBack;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * DESCRIPTION：排行 列表 activity todo 待调试接口
 * Created by fangs on 2019/7/8 16:04.
 */
public class RankingListActivity extends AppCompatActivity implements IBaseActivity {

    boolean type;

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvRankingList)
    RecyclerView rvRankingList;
    RankingListAdapter adapter;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.ranking_list_activity;
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getBoolean("type", false);
        toolbarTitle.setText(type ? "个人排行榜" : "分局排行榜");

        initRv();
        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRv(){
        adapter = new RankingListAdapter(this, new ArrayList<>());

        rvRankingList.setLayoutManager(new LinearLayoutManager(this));
        rvRankingList.setItemAnimator(new FadeItemAnimator());
        rvRankingList.setAdapter(adapter);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type) dailyRanking();
                else dailyRankOrg();
            }
        });
    }

    // 获取 个人排行榜 数据
    private void dailyRanking(){
        RequestUtils.create(ApiService.class)
                .dailyRanking()
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<String>() {
                    @Override
                    protected void onSuccess(String t) {

                    }

                    @Override
                    protected void updateLayout(int flag) {
                        epl.stop();
                    }
                });
    }

    //部门排行榜
    private void dailyRankOrg(){
        RequestUtils.create(ApiService.class)
                .dailyRankOrg()
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<String>() {
                    @Override
                    protected void onSuccess(String t) {

                    }

                    @Override
                    protected void updateLayout(int flag) {
                        epl.stop();
                    }
                });
    }
}
