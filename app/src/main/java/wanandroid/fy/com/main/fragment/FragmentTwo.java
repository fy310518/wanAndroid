package wanandroid.fy.com.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.OnItemClickListner;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.TreeBean;

/**
 * Created by Administrator on 2017/12/12.
 * 数据
 */
public class FragmentTwo extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
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
        adapterTwo.setItemClickListner(new OnItemClickListner() {
            @Override
            public void onItemClick(View view) {
                TreeBean bean = (TreeBean) view.getTag();
                T.showLong(bean.getName());
            }
        });

        rvKnowledge.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvKnowledge.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(6)
                .setDraw(false)
                .create(getActivity()));

        rvKnowledge.setAdapter(adapterTwo);

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
                        epl.stop();
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

}
