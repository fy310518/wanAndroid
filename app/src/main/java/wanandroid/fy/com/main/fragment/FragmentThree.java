package wanandroid.fy.com.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.utils.ConstantUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;
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
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.Bookmark;
import wanandroid.fy.com.search.SearchActivity;
import wanandroid.fy.com.utils.sticky.StickyItemDecoration;
import wanandroid.fy.com.web.WebViewActivity;

/**
 * 书签
 * Created by fangs on 2017/12/12.
 */
public class FragmentThree extends BaseFragment {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
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

        epl.setCurrentType(EasyPullLayout.TYPE_EDGE_TOP);
        epl.start();
    }

    private void initRv() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);

        rvBookmark.setLayoutManager(layoutManager);
        rvBookmark.addItemDecoration(new StickyItemDecoration());
        rvAdapter = new AdapterThree(getContext(), new ArrayList<>());
        rvAdapter.setItemClickListner(view -> {
            Bookmark item = (Bookmark) view.getTag();

            if (item.getItemType() == ConstantUtils.StickyType) return;

            Bundle bundle = new Bundle();
            bundle.putSerializable("Bookmark", item);

            if (!TextUtils.isEmpty(item.getLink())) {//进入具体的 web网页
                JumpUtils.jump(FragmentThree.this, WebViewActivity.class, bundle);
            } else {// 进入搜索页
                JumpUtils.jump(FragmentThree.this, SearchActivity.class, bundle);
            }
        });
        rvBookmark.setAdapter(rvAdapter);

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
                data.add(new Bookmark("搜索热词", ConstantUtils.StickyType));
                data.addAll(listBeanModule);
            }

            if (listBeanModule2.size() > 0) {
                data.add(new Bookmark("常用网站", ConstantUtils.StickyType));
                data.addAll(listBeanModule2);
            }

            return data;
        }).doOnSubscribe(RequestUtils::addDispos)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetCallBack<List<Bookmark>>() {
              @Override
              protected void onSuccess(List<Bookmark> list) {
                  L.e("大王", "aaaa" + Thread.currentThread().getName());
                  L.e(list.toString());
                  rvAdapter.setmDatas(list);
                  rvAdapter.notifyDataSetChanged();
                  epl.stop();
              }

              @Override
              protected void updataLayout(int flag) {
              }
          });
    }
}