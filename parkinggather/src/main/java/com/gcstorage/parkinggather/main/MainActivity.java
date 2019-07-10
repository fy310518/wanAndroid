package com.gcstorage.parkinggather.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.anim.FadeItemAnimator;
import com.fy.baselibrary.rv.divider.GridItemDecoration;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.SpanUtils;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshLoadMoreListener;
import com.gcstorage.app.main.MemoryCameraActivity;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.DataEntry;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.bean.StatisticsEntity;
import com.gcstorage.parkinggather.bean.WeatherEntity;
import com.gcstorage.parkinggather.carinfo.CarGatherInfoActivity;
import com.gcstorage.parkinggather.history.CollectHistoryActivity;
import com.gcstorage.parkinggather.ranking.RankingListActivity;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.NetCallBack;
import com.gcstorage.parkinggather.util.PGAppUtils;
import com.gcstorage.parkinggather.widget.BarGraphView;
import com.gongwen.marqueen.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function3;

/**
 * DESCRIPTION：驻车采集 主界面
 * Created by fangs on 2019/7/1 17:00.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int listHeadNum = 2;//列表头 数目

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
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.menuLayout:

                JumpUtils.jump(this, CollectHistoryActivity.class, bundle);
                break;
            case R.id.iv_collect://拍照（驻车采集）
                Intent cameraintent = new Intent(MainActivity.this, MemoryCameraActivity.class);
                cameraintent.putExtra("camera", false);
                startActivity(cameraintent);
                break;
            case R.id.ll_personal_rank://个人排行
                bundle.putBoolean("type", true);
                JumpUtils.jump(this, RankingListActivity.class, bundle);
                break;
            case R.id.ll_depart_rank://分局排行
                bundle.putBoolean("type", false);
                JumpUtils.jump(this, RankingListActivity.class, bundle);
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

    MarqueeView mv_person_info;
    List<SpannableString> txt;
    private void setStatistics(StatisticsEntity statisticsEntity){
        View statistics = getLayoutInflater().inflate(R.layout.parking_gather_statistics_item, null);
        LinearLayout ll_personal_rank = statistics.findViewById(R.id.ll_personal_rank);
        LinearLayout ll_depart_rank = statistics.findViewById(R.id.ll_depart_rank);
        ll_personal_rank.setOnClickListener(this);
        ll_depart_rank.setOnClickListener(this);

        mv_person_info = statistics.findViewById(R.id.mv_person_info);
        gatherAdapter.setMv_person_info(mv_person_info);
        setUsedInfo(statisticsEntity);

        //统计图
        BarGraphView bgh_count_use = statistics.findViewById(R.id.bgh_count_use);
        List<StatisticsEntity.CollectInfoListBean> collectInfoList = statisticsEntity.getCollectInfoList();
        initBarGraph(bgh_count_use, collectInfoList);

        // 最新采集
        TextView tv_count = statistics.findViewById(R.id.tv_count);
        tv_count.setText(SpanUtils.getBuilder()
                .setFgColor(R.color.txtSuperColor)
                .append("最新采集\n", null)
                .setFgColor(R.color.txtSecondColor)
                .append("全市采集 ", null)
                .setFgColor(R.color.txtBlueColor)
                .append(statisticsEntity.getCityTotalCollectnum(), null)
                .setFgColor(R.color.txtSecondColor)
                .append(" 辆", null)
                .create());

        gatherAdapter.addHeaderView(statistics);
        gatherAdapter.notifyItemRangeChanged(0, gatherAdapter.getHeadersCount());
    }

    private void setUsedInfo(StatisticsEntity data) {
        txt = new ArrayList<>();
        TextMargeeAdapter marqueeFactory = new TextMargeeAdapter(this);
        mv_person_info.setMarqueeFactory(marqueeFactory);

        List<StatisticsEntity.UsedInfoBean> usedInfo = data.getUsedInfo();
        String city = "我采集的 %s, 已被%s使用";
        if (usedInfo.size() == 0) {
            SpannableString spannableString = setPersonalUseCount(data.getTotalCollectnum(), data.getUsedCollectnum());
            txt.add(spannableString);
        }
        for (StatisticsEntity.UsedInfoBean infoBean : usedInfo) {
            SpannableString spannableString = setPersonalUseCount(data.getTotalCollectnum(), data.getUsedCollectnum());
            txt.add(spannableString);
            SpannableString perText = new SpannableString(String.format(city, infoBean.getHphm().toUpperCase(), infoBean.getName()));
            txt.add(perText);
        }
        marqueeFactory.setData(txt);
        if (txt.size() != 1) {
            mv_person_info.startFlipping();
        }
    }

    private SpannableString setPersonalUseCount(String totalCollectnum, String usedCollectnum) {
        String city = "我采集了 %s 辆,有 %s 条信息被人采用了";
        if (TextUtils.isEmpty(usedCollectnum)) {
            usedCollectnum = "0";
        }
        SpannableString perText = new SpannableString(String.format(city, totalCollectnum, usedCollectnum));
        perText.setSpan(new TextAppearanceSpan(this, R.style.spannable_text_big_style), 5, 5 + totalCollectnum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        perText.setSpan(new TextAppearanceSpan(this, R.style.spannable_text_big_style), 5 + totalCollectnum.length() + 5
                , 5 + totalCollectnum.length() + 5 + usedCollectnum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return perText;
    }

    // 柱状图
    private void initBarGraph(BarGraphView bghCountUse, List<StatisticsEntity.CollectInfoListBean> collectInfoList) {
        //配置柱状图中的参数
        bghCountUse.setxAxisMean("时间");
        bghCountUse.setyAxisMean("数量");
        bghCountUse.setAxisMeanColor(Color.BLACK);
        bghCountUse.setMeanTextSize(DensityUtils.dp2px(10));
        bghCountUse.setAxisTextColor(ResUtils.getColor(R.color.text_color_gray_a6));
        bghCountUse.setAxisTextSize(DensityUtils.dp2px(10));
        bghCountUse.setValueTextSize(DensityUtils.dp2px(12));
        bghCountUse.setAxisValueColor(Color.BLACK);
        bghCountUse.setRectColor(ResUtils.getColor(R.color.txtBlueColor));

        if (null == collectInfoList || collectInfoList.isEmpty()) return;

        List<DataEntry> barGraphdata = new ArrayList<>();
        String st = TimeUtils.Long2DataString(System.currentTimeMillis(), "yyyy-MM-dd");
        for (StatisticsEntity.CollectInfoListBean collectInfoListBean : collectInfoList) {
            String time = collectInfoListBean.getName();

            String day = time.substring(time.lastIndexOf("-") + 1) + "日";

            if (time.equals(st)) {
                day = "今天";
            }

            barGraphdata.add(new DataEntry(day, Integer.parseInt(collectInfoListBean.getCollectnum())));
            bghCountUse.setAxisValue(barGraphdata);
        }
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

    private void getParkingList() {
        Observable<WeatherEntity> weather = RequestUtils.create(ApiService.class)
                .getWeather("武汉")
                .compose(RxHelper.handleResult());

        Observable<StatisticsEntity> parkingCollect = RequestUtils.create(ApiService.class)
                .parkingCollect(SpfAgent.getString(Constant.baseSpf, Constant.userId))
                .compose(RxHelper.handleResult());

        Observable<ParkingInfoEntity> parkingList = RequestUtils.create(ApiService.class)
                .getParkingList("10", pageNum + "")
                .compose(RxHelper.handleResult());

        Observable.zip(weather, parkingCollect, parkingList, new Function3<WeatherEntity, StatisticsEntity, ParkingInfoEntity, ArrayMap<String, Object>>() {
            @Override
            public ArrayMap<String, Object> apply(WeatherEntity weatherEntity, StatisticsEntity statisticsEntity, ParkingInfoEntity parkingInfoEntity) throws Exception {
                ArrayMap<String, Object> map = new ArrayMap<>();
                if (pageNum == 1) {
                    map.put("weather", weatherEntity);
                    map.put("StatisticsEntity", statisticsEntity);
                }
                map.put("parkingList", parkingInfoEntity);

                return map;
            }
        }).compose(RxHelper.bindToLifecycle(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetCallBack<ArrayMap<String, Object>>() {
                    @Override
                    protected void onSuccess(ArrayMap<String, Object> map) {
                        WeatherEntity weatherEntity = (WeatherEntity) map.get("weather");
                        StatisticsEntity statisticsEntity = (StatisticsEntity) map.get("StatisticsEntity");
                        ParkingInfoEntity parkingInfoEntity = (ParkingInfoEntity) map.get("parkingList");

                        if (pageNum == 1) {
                            if (null != weatherEntity && null != statisticsEntity) {
                                setWeather(weatherEntity);
                                setStatistics(statisticsEntity);
                            }

                            if (null != parkingInfoEntity) {
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
