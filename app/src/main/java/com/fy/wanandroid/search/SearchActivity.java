package com.fy.wanandroid.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;
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
 * 搜索
 * Created by fangs on 2018/4/13.
 */
public class SearchActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    SearchView searchView;

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;
    AdapterOne rvAdapter;
    /**
     * 当前显示的页码(从 0 开始)
     */
    int pageNum;
    String queryKey;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_search;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.StatusBuilder.init().setStatusColor(R.color.statusBar, 0)
                .setNavColor(R.color.statusBar, 0)
                .setColorBar(activity);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRvAdapter();

        Bundle bundle = getIntent().getExtras();
        queryKey = bundle.getString(Constant.queryKey);
        if (!TextUtils.isEmpty(queryKey)) epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_edit, menu);
        //得到SearchView对象，SearchView一些属性可以直接使用，比如：setSubmitButtonEnabled，setQueryHint等
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //如果想单独对SearchView定制，比如需要更换搜索图标等，可以通过一下代码实现。
        if(null != searchView) {
            searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            //默认刚进去就打开搜索栏
            searchView.setIconified(false);
            if (!TextUtils.isEmpty(queryKey))searchView.clearFocus();
            //设置输入文本的EditText
            SearchView.SearchAutoComplete et = searchView.findViewById(R.id.search_src_text);
            et.setText(queryKey);
            searchView.setQueryHint(queryKey);

            // 设置监听事件
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    T.showLong(query);
                    queryKey = query;
                    searchView.clearFocus();
                    getData();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    L.e("queryKey", " ---> " + newText);
                    return false;
                }
            });
        }

        return true;
    }

    private void initRvAdapter() {
        rvAdapter = new AdapterOne(this, new ArrayList<>());
        rvAdapter.setChangeItemListener((position) -> rvAdapter.notifyItemChanged(position, ""));
        rvAdapter.setItemClickListner(view -> {
            ArticleBean.DatasBean article = (ArticleBean.DatasBean) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", new Bookmark(article.getTitle(), article.getLink()));
            JumpUtils.jump(this, WebViewActivity.class, bundle);
        });

        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setItemAnimator(new FadeItemAnimator());
        rvSearch.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(this));

        rvSearch.setAdapter(rvAdapter);

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
                .query(pageNum, queryKey)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
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
                    protected void updataLayout(int flag) {
                        epl.stop();
                    }
                });
    }
}
