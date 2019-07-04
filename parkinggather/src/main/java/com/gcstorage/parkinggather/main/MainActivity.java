package com.gcstorage.parkinggather.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.OnListener;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.GridItemDecoration;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.SpanUtils;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshLoadMoreListener;
import com.gcstorage.app.main.MemoryCameraActivity;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.carinfo.CarGatherInfoActivity;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.NetCallBack;
import com.gcstorage.parkinggather.util.PGAppUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * DESCRIPTION：驻车采集 主界面
 * Created by fangs on 2019/7/1 17:00.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int listHeadNum = 1;//列表头 数目

    int pageNum = 1;//当前页
    int totalPage = 1;//总页数

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvParkingGather)
    RecyclerView rvParkingGather;
    GatherRvAdapter gatherAdapter;


    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.main_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.act_menu, menu);
        View menuLayout = menu.findItem(R.id.menuAdd).getActionView();
        menuLayout.setOnClickListener(this);
        AppCompatImageView rightIcon = menuLayout.findViewById(R.id.rightIcon);
        rightIcon.setImageResource(R.mipmap.img_history_search);
        rightIcon.setVisibility(View.VISIBLE);
        return true;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> JumpUtils.exitApp(this, StartActivity.class));

        initRv();
        epl.start(EasyPullLayout.TYPE_EDGE_TOP);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); 	不要调用父类的方法
        JumpUtils.exitApp(this, StartActivity.class);
    }

    @OnClick({R.id.iv_collect})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuLayout:
                T.showLong("历史记录");
                break;
            case R.id.iv_collect://拍照（驻车采集）
                Intent cameraintent = new Intent(MainActivity.this, MemoryCameraActivity.class);
                cameraintent.putExtra("camera", false);
                startActivity(cameraintent);
                break;
        }
    }

    private void initRv(){
        gatherAdapter = new GatherRvAdapter(this, new ArrayList<>());
        gatherAdapter.setItemClickListner(view -> {
            ParkingInfoEntity.DataBean dataBean = (ParkingInfoEntity.DataBean) view.getTag();
            int position = gatherAdapter.getmDatas().indexOf(dataBean);


            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("ParkingInfoEntity", new ParkingInfoEntity(gatherAdapter.getmDatas()));
            JumpUtils.jump(this, CarGatherInfoActivity.class, bundle);
        });
        rvParkingGather.setLayoutManager(new GridLayoutManager(this, 2));
        rvParkingGather.setItemAnimator(new FadeItemAnimator());
        rvParkingGather.addItemDecoration(
                GridItemDecoration.Builder.init()
                        .setColumn(2)
                        .setRvHeaderNum(listHeadNum)
                        .setmSpace(R.dimen.spacing_small)
                        .setDraw(false)
                        .create(this));

        rvParkingGather.setAdapter(gatherAdapter);

        epl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pageNum < totalPage) {
                    pageNum++;
                    getParkingList();
                } else {
                    T.showLong("没有更多信息了，去采集一些吧...");
                    epl.stop();
                }
            }

            @Override
            public void onRefresh() {
                pageNum = 1;
                getParkingList();
            }
        });
    }

    //获取天气 头部 布局，并设置数据
    private void setWeather(){
        View weatherView = getLayoutInflater().inflate(R.layout.parking_gather_weather_head_item, null);
        TextView txtWeather = weatherView.findViewById(R.id.txtWeather);
        TextView txtAddress = weatherView.findViewById(R.id.txtAddress);

        TintUtils.setTxtIconLocal(txtWeather, TintUtils.getDrawable(PGAppUtils.getWeatherIcon("00"), 0), 1);
        txtWeather.setText("温度");

        SpannableStringBuilder ssb = SpanUtils.getBuilder()
                .setFgColor(R.color.txtSecondColor)
                .setTextDpSize(R.dimen.txt_small)
                .append("小雨" + "\n", null)
                .setFgColor(R.color.txtLight)
                .setTextDpSize(R.dimen.txt_small_much)
                .append("武汉", null)
                .create();

        txtAddress.setText(ssb);

        gatherAdapter.cleanHeader();
        gatherAdapter.addHeaderView(weatherView);
    }

    private void getParkingList(){
        RequestUtils.create(ApiService.class)
                .getParkingList("10", pageNum + "")
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<ParkingInfoEntity>() {
                    @Override
                    protected void onSuccess(ParkingInfoEntity parkingInfoEntity) {

                        totalPage = parkingInfoEntity.getTotalPage();

                        if (pageNum == 1) {
                            setWeather();

                            gatherAdapter.setmDatas(parkingInfoEntity.getData());
                            gatherAdapter.notifyDataSetChanged();
                        } else {
                            int count = gatherAdapter.getItemCount();
                            gatherAdapter.addData(parkingInfoEntity.getData());
                            gatherAdapter.notifyItemRangeChanged(count, parkingInfoEntity.getData().size());
                        }
                    }
                    @Override
                    protected void updateLayout(int flag) {
                        epl.stop();
                    }
                });
    }
}
