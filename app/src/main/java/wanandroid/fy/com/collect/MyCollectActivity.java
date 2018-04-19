package wanandroid.fy.com.collect;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.JumpUtils;
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
import wanandroid.fy.com.main.fragment.AdapterOne;
import wanandroid.fy.com.main.fragment.DiffCallBack;
import wanandroid.fy.com.main.fragment.FragmentOne;
import wanandroid.fy.com.web.WebViewActivity;

/**
 * 我的收藏 列表
 * Created by fangs on 2018/4/19.
 */
public class MyCollectActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
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

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRvAdapter();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void reTry() {
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
                    topView.triggered(MyCollectActivity.this);
                    pageNum = 0;
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
                .getCollectList(pageNum)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<ArticleBean>() {
                    @Override
                    protected void onSuccess(ArticleBean articleBean) {
                        if (pageNum == 0) {
                            List<ArticleBean.DatasBean> list = articleBean.getDatas();
                            if (null != list) {
//                                DiffUtil.DiffResult diffResult = DiffUtil
//                                        .calculateDiff(new DiffCallBack(rvAdapter.getmDatas(), list), true);
//
//                                diffResult.dispatchUpdatesTo(rvAdapter);
                                rvAdapter.setmDatas(list);
                                rvAdapter.notifyDataSetChanged();
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
