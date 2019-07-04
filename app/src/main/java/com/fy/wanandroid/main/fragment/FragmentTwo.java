package com.fy.wanandroid.main.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.base.fragment.BaseFragment;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    public View setStatusView(){return rvKnowledge;}

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_two;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void baseInit() {
        initRv();

        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxHelper.bindToLifecycle(mContext))
                .subscribe(aLong -> {
                    epl.start(EasyPullLayout.TYPE_EDGE_TOP);
//                    RefreshAnimView view = epl.getAnimView(EasyPullLayout.TYPE_EDGE_TOP);
//                    if (null != view) view.idle();
                });
    }

    @Override
    public void onRetry() {
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
                .compose(RxHelper.bindToLifecycle(getActivity()))
                .subscribe(new NetCallBack<List<TreeBean>>(this) {
                    @Override
                    protected void onSuccess(List<TreeBean> treeBeanList) {
                        adapterTwo.setmDatas(treeBeanList);
                        adapterTwo.notifyDataSetChanged();
                    }

                    @Override
                    protected void updateLayout(int flag) {
                        super.updateLayout(flag);
                        epl.stop();
                    }
                });
    }

}
