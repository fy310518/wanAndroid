package com.fy.wanandroid.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.baselibrary.base.fragment.BaseFragment;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.notify.L;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.Bookmark;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;
import com.fy.wanandroid.search.SearchActivity;
import com.fy.wanandroid.utils.FlexboxStickyDecoration;
import com.fy.wanandroid.web.WebViewActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 书签
 * Created by fangs on 2017/12/12.
 */
public class FragmentThree extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvBookmark)
    RecyclerView rvBookmark;
    AdapterThree rvAdapter;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_three;
    }

    @Override
    protected void baseInit() {
        initRv();

        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    private void initRv() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);

        rvBookmark.setLayoutManager(layoutManager);
        rvBookmark.addItemDecoration(new FlexboxStickyDecoration());
        rvAdapter = new AdapterThree(getContext(), new ArrayList<>());
        rvAdapter.setItemClickListner(view -> {
            Bookmark item = (Bookmark) view.getTag();

            if (item.getItemType() == Constant.StickyType) return;

            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(item.getLink())) {//进入具体的 web网页
                bundle.putSerializable("Bookmark", item);
                JumpUtils.jump(FragmentThree.this, WebViewActivity.class, bundle);
            } else {// 进入搜索页
                bundle.putString(Constant.queryKey, item.getName());
                JumpUtils.jump(FragmentThree.this, SearchActivity.class, bundle);
            }
        });
        rvBookmark.setAdapter(rvAdapter);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
        Observable<List<Bookmark>> observable1 = RequestUtils.create(ApiService.class)
                .getHotkeyList()
                .compose(RxHelper.handleResult())
                .observeOn(Schedulers.io());

        Observable<List<Bookmark>> observable2 = RequestUtils.create(ApiService.class)
                .getFriendList()
                .compose(RxHelper.handleResult())
                .observeOn(Schedulers.io());

        Observable.zip(observable1, observable2, (listBeanModule, listBeanModule2) -> {
            L.e("大王", "aaaa" + Thread.currentThread().getName());

            List<Bookmark> data = new ArrayList<>();
            if (listBeanModule.size() > 0) {
                data.add(new Bookmark("搜索热词", Constant.StickyType));
                data.addAll(listBeanModule);
            }

            if (listBeanModule2.size() > 0) {
                data.add(new Bookmark("常用网站", Constant.StickyType));
                data.addAll(listBeanModule2);
            }

            return data;
        }).compose(RxHelper.bindToLifecycle(getActivity()))
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetCallBack<List<Bookmark>>() {
              @Override
              protected void onSuccess(List<Bookmark> list) {
                  L.e("大王", "aaaa" + Thread.currentThread().getName());
                  L.e(list.toString());
                  rvAdapter.setmDatas(list);
                  rvAdapter.notifyDataSetChanged();
              }

              @Override
              protected void updateLayout(int flag) {
                  epl.stop();
              }
          });
    }
}