package hodgepodge.fy.com.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.rv.adapter.HeaderAndFooterWrapper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.ListItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import hodgepodge.fy.com.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改：一般情况下都不需要自动刷新 只有自己发布消息回来本界面时才自动刷新
 * Created by Administrator on 2017/12/12.
 */
public class FragmentFour extends BaseFragment {

    @BindView(R.id.rvDemo)
    RecyclerView rvDemo;
    DemoAdapter adapter;
    HeaderAndFooterWrapper headerAdapter;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_four;
    }

    @Override
    protected void baseInit() {

        initRv();

//        Observable.timer(2000, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        List<String> data = new ArrayList<>();
//                        for (int i = 40; i < 50; i++){
//                            data.add("***" + i);
//                        }
//
//                        DemoAdapter adapter = (DemoAdapter) headerAdapter.getmInnerAdapter();
//                        adapter.addData(data);
//                        headerAdapter.notifyDataSetChanged();
//
//                    }
//                });
    }


    private void initRv(){
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++){
            data.add("---" + i);
        }

        adapter = new DemoAdapter(mContext, data);
        adapter.setItemClickListner(view -> {
            String item = (String) view.getTag();
            int position = adapter.getmDatas().indexOf(item);

            adapter.addData(position, "大王");
            adapter.notifyItemInserted(position);
        });
        rvDemo.setLayoutManager(new LinearLayoutManager(mContext));
        rvDemo.addItemDecoration(new ListItemDecoration(mContext, 0));
        rvDemo.setItemAnimator(new FadeItemAnimator());

//        headerAdapter = new HeaderAndFooterWrapper(adapter);
//        TextView t1 = new TextView(mContext);
//
//        t1.setText("Header 1---");
//        headerAdapter.addHeaderView(t1);

        rvDemo.setAdapter(adapter);
    }


}
