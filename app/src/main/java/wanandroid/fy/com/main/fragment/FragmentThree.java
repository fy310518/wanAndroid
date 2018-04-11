package wanandroid.fy.com.main.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.rv.divider.sticky.StickyView;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;
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
import wanandroid.fy.com.utils.SelectUtils;

/**
 * 书签
 * Created by fangs on 2017/12/12.
 */
public class FragmentThree extends BaseFragment {

    @BindView(R.id.rvBookmark)
    RecyclerView rvBookmark;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_three;
    }

    @Override
    protected void baseInit() {
        initRv();

        getData();
    }

    private void initRv() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        layoutManager.setJustifyContent(JustifyContent.SPACE_BETWEEN);

        rvBookmark.setLayoutManager(layoutManager);
        rvBookmark.setAdapter(new RvCommonAdapter<Bookmark>(getContext(), R.layout.fragment_two_item, new ArrayList<>()) {
            @Override
            public int getItemViewType(int position) {
                return mDatas.get(position).getItemType();
            }

            @Override
            public void convert(ViewHolder holder, Bookmark t, int position) {
                // todo 吸附
                TextView te = holder.getView(R.id.tvTag);
                te.setText(t.getName());
                te.setBackground(SelectUtils.getTagSelector(R.drawable.shape_tag));
                te.setOnClickListener(v -> T.showLong(v.getId() + ""));
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
                data.add(new Bookmark(listBeanModule.get(0).getName(), StickyView.StickyType));
                data.addAll(listBeanModule);
            }

            if (listBeanModule2.size() > 0) {
                data.add(new Bookmark(listBeanModule2.get(0).getName(), StickyView.StickyType));
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
                  RvCommonAdapter adapter = (RvCommonAdapter) rvBookmark.getAdapter();
                  adapter.setmDatas(list);
                  adapter.notifyDataSetChanged();
              }

              @Override
              protected void updataLayout(int flag) {

              }
          });
    }
}