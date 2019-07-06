package com.gcstorage.parkinggather.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
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
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.bean.WeatherEntity;
import com.gcstorage.parkinggather.carinfo.CarGatherInfoActivity;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.BeanModule;
import com.gcstorage.parkinggather.request.NetCallBack;
import com.gcstorage.parkinggather.util.PGAppUtils;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

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
    private void setWeather(WeatherEntity weatherEntity){
        View weatherView = getLayoutInflater().inflate(R.layout.parking_gather_weather_head_item, null);
        TextView txtWeather = weatherView.findViewById(R.id.txtWeather);
        TextView txtAddress = weatherView.findViewById(R.id.txtAddress);

        //根据 天气 状态码 获取天气对应的图标
        TintUtils.setTxtIconLocal(txtWeather, TintUtils.getDrawable(PGAppUtils.getWeatherIcon(weatherEntity.getWeather_code()), 0), 1);
        txtWeather.setText(weatherEntity.getTemperaturescope());//设置温度 数据

        SpannableStringBuilder ssb = SpanUtils.getBuilder()
                .setFgColor(R.color.txtSecondColor)
                .setTextDpSize(R.dimen.txt_small)
                .append(weatherEntity.getWeather() + "\n", null)//天气
                .setFgColor(R.color.txtLight)
                .setTextDpSize(R.dimen.txt_small_much)
                .append(weatherEntity.getArea(), null)// 城市
                .create();

        txtAddress.setText(ssb);

        gatherAdapter.cleanHeader();
        gatherAdapter.addHeaderView(weatherView);
        gatherAdapter.notifyItemRangeChanged(0, 1);
    }

    private void getParkingList(){

        Observable<WeatherEntity> weather = RequestUtils.create(ApiService.class)
                .getWeather("武汉")
                .compose(RxHelper.handleResult());

        Observable<ParkingInfoEntity> parkingList = RequestUtils.create(ApiService.class)
                .getParkingList("10", pageNum + "")
                .compose(RxHelper.handleResult());

        Observable.zip(weather, parkingList, new BiFunction<WeatherEntity, ParkingInfoEntity, ArrayMap<String, Object>>() {
            @Override
            public ArrayMap<String, Object> apply(WeatherEntity weather, ParkingInfoEntity parkingList) throws Exception {
                ArrayMap<String, Object> map = new ArrayMap<>();
                if(pageNum == 1) map.put("weather", weather);
                map.put("parkingList", parkingList);
                return map;
            }
        }).compose(RxHelper.bindToLifecycle(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetCallBack<ArrayMap<String, Object>>() {
                    @Override
                    protected void onSuccess(ArrayMap<String, Object> map) {

                        WeatherEntity weatherEntity = (WeatherEntity) map.get("weather");
                        ParkingInfoEntity parkingInfoEntity = (ParkingInfoEntity) map.get("parkingList");

                        if (pageNum == 1) {
                            if (null != weatherEntity) setWeather(weatherEntity);

                            if (null != parkingInfoEntity){
                                totalPage = parkingInfoEntity.getTotalPage();
                                gatherAdapter.setmDatas(parkingInfoEntity.getData());
                                gatherAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (null == parkingInfoEntity) return;
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
