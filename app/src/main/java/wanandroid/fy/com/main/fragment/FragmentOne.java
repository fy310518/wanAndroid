package wanandroid.fy.com.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.HeaderAndFooterWrapper;
import com.fy.baselibrary.rv.adapter.OnListener;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.TintUtils;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.ArticleBean;
import wanandroid.fy.com.entity.BannerBean;
import wanandroid.fy.com.entity.Bookmark;
import wanandroid.fy.com.utils.LocalImageHolderView;
import wanandroid.fy.com.web.WebViewActivity;

/**
 * 首页 // todo 主体 上拉加载更多 没有实现
 * Created by fangs on 2017/12/12.
 */
public class FragmentOne extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
    @BindView(R.id.rvArticle)
    RecyclerView rvArticle;
    HeaderAndFooterWrapper adapter;
    ConvenientBanner<BannerBean> bannerView;
    List<BannerBean> bannerBeans;
    AdapterOne rvAdapter;
    /** 当前显示的页码(从 0 开始) */
    int pageNum;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_one;
    }

    @Override
    protected void baseInit() {
        initRvAdapter();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRvAdapter(){
        rvAdapter = new AdapterOne(getContext(), new ArrayList<>());
        rvAdapter.setChangeItemListener((position) -> adapter.notifyItemChanged(adapter.getHeadersCount() + position, ""));
        rvAdapter.setItemClickListner(view -> {
            ArticleBean.DatasBean article = (ArticleBean.DatasBean) view.getTag();

            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", new Bookmark(article.getTitle(), article.getLink()));
            JumpUtils.jump(FragmentOne.this, WebViewActivity.class, bundle);
        });

        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticle.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(getActivity()));

        adapter = new HeaderAndFooterWrapper(rvAdapter);
        rvArticle.setAdapter(adapter);

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
                    getData();
                } else if (type == EasyPullLayout.TYPE_EDGE_BOTTOM){

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
        Observable<List<BannerBean>> observable1 = RequestUtils.create(ApiService.class)
                    .getBannerList()
                    .compose(RxHelper.handleResult())
                    .observeOn(Schedulers.io());

        Observable<ArticleBean> observable2 = RequestUtils.create(ApiService.class)
                .getArticleList(pageNum)
                .compose(RxHelper.handleResult())
                .observeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<List<BannerBean>, ArticleBean, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(List<BannerBean> bannerBeans, ArticleBean articleBean) throws Exception {
                Map<String, Object> map = new HashMap<>();
                map.put("banner", bannerBeans);
                map.put("article", articleBean);
                return map;
            }
        }).doOnSubscribe(RequestUtils::addDispos)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetCallBack<Map<String, Object>>() {
                @Override
                protected void onSuccess(Map<String, Object> map) {
                    if (pageNum == 0) {
                        adapter.cleanHeader();

                        List<BannerBean> bannerdata = (List<BannerBean>) map.get("banner");
                        if (null != bannerdata && bannerdata.size() > 0){
                            bannerBeans = bannerdata;
                            setBanner();
                            adapter.addHeaderView(bannerView);
                        }

                        ArticleBean article = (ArticleBean) map.get("article");
                        List<ArticleBean.DatasBean> list = article.getDatas();
                        if (null != list) {
//                            DiffUtil.DiffResult diffResult = DiffUtil
//                                    .calculateDiff(new DiffCallBack(rvAdapter.getmDatas(), list), true);
//
//                            diffResult.dispatchUpdatesTo(adapter);
                            rvAdapter.setmDatas(list);
                            adapter.notifyDataSetChanged();
                        }

                    }
                }

                @Override
                protected void updataLayout(int flag) {
                    epl.stop();
                }
            });
    }

    private void setBanner(){
        bannerView = new ConvenientBanner<>(getContext());
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 350);
        bannerView.setLayoutParams(params);

        //开始自动翻页
        bannerView.setPages(LocalImageHolderView::new, bannerBeans)
                //设置指示器是否可见
//                .setPointViewVisible(true)
                //设置自动切换（同时设置了切换时间间隔）
                .startTurning(2000)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向（左、中、右）
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                //设置点击监听事件
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        BannerBean bannerBean = bannerBeans.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Bookmark", new Bookmark(bannerBean.getTitle(), bannerBean.getUrl()));
                        JumpUtils.jump(FragmentOne.this, WebViewActivity.class, bundle);
                    }
                })
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);

        //设置翻页的效果，不需要翻页效果可用不设; 集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        setPageTransformer(Transformer.DefaultTransformer);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != bannerView) bannerView.startTurning(2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != bannerView) bannerView.stopTurning();
    }
}
