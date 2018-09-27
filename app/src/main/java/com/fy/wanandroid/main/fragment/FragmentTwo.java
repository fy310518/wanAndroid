package com.fy.wanandroid.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.TreeBean;
import com.fy.wanandroid.hierarchy.HierarchyActivity;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 数据
 * Created by fangs on 2017/12/12.
 */
public class FragmentTwo extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvKnowledge)
    RecyclerView rvKnowledge;
    AdapterTwo adapterTwo;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_two;
    }

    @Override
    protected void baseInit() {
        initRv();
        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRv(){
        adapterTwo = new AdapterTwo(getContext(), new ArrayList<>());
        adapterTwo.setItemClickListner(view -> {
            TreeBean bean = (TreeBean) view.getTag();

            Bundle bundle = new Bundle();
            bundle.putSerializable("TreeBean", bean);
            JumpUtils.jump(FragmentTwo.this, HierarchyActivity.class, bundle);
        });

        rvKnowledge.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvKnowledge.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getActivity()));

        rvKnowledge.setAdapter(adapterTwo);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData(){
        RequestUtils.create(ApiService.class)
                .getTreeList()
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<List<TreeBean>>() {
                    @Override
                    protected void onSuccess(List<TreeBean> treeBeanList) {
                        adapterTwo.setmDatas(treeBeanList);
                        adapterTwo.notifyDataSetChanged();
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        epl.stop();
                    }
                });
    }

}
