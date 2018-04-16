package wanandroid.fy.com.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.HomeBean;
import wanandroid.fy.com.entity.TreeBean;

/**
 * 首页
 * Created by fangs on 2017/12/12.
 */
public class FragmentOne extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_one;
    }

    @Override
    protected void baseInit() {
        initRv();
        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRv(){

        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticle.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getActivity()));

//        rvArticle.setAdapter(adapterTwo);

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
//                    getArticleList();
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

    private void getArticleList(int pageNum){
        RequestUtils.create(ApiService.class)
                .getHomeList(pageNum)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<HomeBean>() {
                    @Override
                    protected void onSuccess(HomeBean login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }
}
