package com.fy.wanandroid.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.TreeBean;
import com.fy.wanandroid.hierarchy.HierarchyActivity;
import com.fy.wanandroid.main.fragment.AdapterTwo;
import com.fy.wanandroid.main.fragment.FragmentTwo;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * describe： todo 描述</br>
 * Created by fangs on 2018/12/28 16:39.
 */
public class TestListActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvKnowledge)
    RecyclerView rvKnowledge;
    TestAdapter testAdapter;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_test_layout;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRv();
        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }


    private void initRv(){
        testAdapter = new TestAdapter(this, new ArrayList<>());

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                TestBean testBean = testAdapter.getmDatas().get(position);
                return TextUtils.isEmpty(testBean.getRecommend_img_url()) ? 1 : 2;
            }
        });
        rvKnowledge.setLayoutManager(manager);
//        rvKnowledge.addItemDecoration(new ListItemDecoration.Builder()
//                .setmSpace(R.dimen.rv_divider_height)
//                .setDraw(false)
//                .create(this));

        rvKnowledge.setAdapter(testAdapter);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData(){
        RequestUtils.create(ApiService.class)
                .test()
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<List<TestBean>>(this) {
                    @Override
                    protected void onSuccess(List<TestBean> treeBeanList) {
                        testAdapter.setmDatas(treeBeanList);
                        testAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        super.updataLayout(flag);
                        epl.stop();
                    }
                });
    }
}
