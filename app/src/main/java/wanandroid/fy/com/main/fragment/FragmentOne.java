package wanandroid.fy.com.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.OnItemClickListner;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.ArticleBean;
import wanandroid.fy.com.entity.Bookmark;
import wanandroid.fy.com.web.WebViewActivity;

/**
 * 首页 // todo 头部 banner 没有实现, 主体 下拉加载更多 没有实现
 * Created by fangs on 2017/12/12.
 */
public class FragmentOne extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;
    AdapterOne rvAdapter;
    int pageNum;

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
        rvAdapter = new AdapterOne(getContext(), new ArrayList<>());
        rvAdapter.setItemClickListner(view -> {
            ArticleBean articleBean = (ArticleBean) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", new Bookmark(articleBean.getTitle(), articleBean.getLink()));
            JumpUtils.jump(FragmentOne.this, WebViewActivity.class, bundle);
        });

        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticle.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getActivity()));

        rvArticle.setAdapter(rvAdapter);

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
                    pageNum = 0;
                    getArticleList();
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

    private void getArticleList(){
        RequestUtils.create(ApiService.class)
                .getArticleList(pageNum)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<List<ArticleBean>>() {
                    @Override
                    protected void onSuccess(List<ArticleBean> data) {
                        if (data.size() > 0) {
                            rvAdapter.setmDatas(data);
                            rvAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }
}
