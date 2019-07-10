package com.gcstorage.parkinggather.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.ParkingInfo;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.NetCallBack;

import java.util.List;

import butterknife.BindView;

/**
 * DESCRIPTION：驻车采集地图展示
 * Created by fangs on 2019/7/9 16:15.
 */
public class ParkCarMapActivity extends AppCompatActivity implements IBaseActivity {

    String time;

    ParkCarMapFragment parkCarMapFragment;

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.tv_carcount)
    TextView tv_carcount;

    @BindView(R.id.tv_starttime)
    TextView tv_starttime;
    @BindView(R.id.tv_endtime)
    TextView tv_endtime;



    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.parking_map_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        time = bundle.getString("time", "0");

        toolbarTitle.setText(time);

        parkCarMapFragment = new ParkCarMapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main, parkCarMapFragment);
        transaction.commit();

        getData(time);
    }

    //根据日期查询当天所有采集车辆数据 日期
    private void getData(String date){
        RequestUtils.create(ApiService.class)
                .serchParkingBydata(SpfAgent.getString(Constant.baseSpf, Constant.userId), date)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<ParkingInfo>() {
                    @Override
                    protected void onSuccess(ParkingInfo calendar) {
                        if (null == calendar) return;
                        List<ParkingInfoEntity.DataBean> imageInfoList = calendar.getData();
                        if (null == imageInfoList || imageInfoList.isEmpty()) return;

                        ParkingInfoEntity.DataBean start = imageInfoList.get(0);
                        ParkingInfoEntity.DataBean end   = imageInfoList.get(imageInfoList.size() - 1);

                        String startTime = TimeUtils.Long2DataString(TimeUtils.timeString2long(start.getTime(), "yyyy-MM-dd HH:mm:ss"), "HH:mm:ss");
                        tv_starttime.setText(startTime);
                        String endTime = TimeUtils.Long2DataString(TimeUtils.timeString2long(end.getTime(), "yyyy-MM-dd HH:mm:ss"), "HH:mm:ss");
                        tv_endtime.setText(endTime);

                        tv_carcount.setText(imageInfoList.size() + "");

                        parkCarMapFragment.drawPolygon(imageInfoList);
                    }
                });
    }
}
