package com.gcstorage.parkinggather.carinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.ParkingInfoEntity;
import com.gcstorage.parkinggather.querycar.QueryCarActivity;
import com.gcstorage.parkinggather.request.ApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * DESCRIPTION：驻车采集 --》采集详情
 * Created by fangs on 2019/7/2 14:41.
 */
public class CarGatherInfoActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    /** 全屏状态 */
    public static final int STATE_FULLSCREEN = 0;
    /** 显示菜单状态 */
    public static final int STATE_SHOW_MENU = 1;
    protected int mCurrentPosition = 0;//跳转进 CarGatherInfoActivity 时的序号，第几个图片; 当前显示的图片下标

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.bannerViewPager)
    ConvenientBanner viewPager;

    @BindView(R.id.TopGroup)
    Group TopGroup;
    @BindView(R.id.iv_head_pic)
    ImageView iv_head_pic;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_depart)
    TextView tv_depart;

    @BindView(R.id.bottomGroup)
    Group bottomGroup;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_licence_number)
    TextView tv_licence_number;//车牌号
    @BindView(R.id.tv_jump_find_car)
    TextView tv_jump_find_car;//一键查车 按钮

    List<ParkingInfoEntity.DataBean> dates;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.car_gatherinfo_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle){
            mCurrentPosition = bundle.getInt("position", 0);
            ParkingInfoEntity dataBean = (ParkingInfoEntity) bundle.getSerializable("ParkingInfoEntity");
            assert dataBean != null;
            dates = dataBean.getData();

            toolbarTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, mCurrentPosition + 1, dates.size()));
            initInfo(dates.get(mCurrentPosition));
            initViewPager();
        }

        tv_jump_find_car.setBackground(ShapeBuilder.create().solid(R.color.transparent).radius(8).stroke(1, R.color.txtBlueColor).build());
    }

    @ClickFilter
    @OnClick({R.id.tv_jump_find_car})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump_find_car://一键查车
                if (null == dates || dates.isEmpty()) return;

                try {
                    ParkingInfoEntity.DataBean parkingInfo = dates.get(mCurrentPosition);
                    Bundle bundle = new Bundle();
    //                bundle.putSerializable("ParkingInfo", parkingInfo);
    //                JumpUtils.jump(this, QueryCarActivity.class, bundle);

                    bundle.putString("url", ApiService.queryPerson);
                    JumpUtils.jump(this, SpfAgent.getString(Constant.baseSpf, Constant.otherAppID) + ".com.gcstrage.h5App", bundle);
                } catch (Exception e){

                }
                break;
        }
    }

    //设置 采集的车辆信息
    private void initInfo(ParkingInfoEntity.DataBean dataBean){
        if (null == dataBean) return;
        ImgLoadUtils.loadRadiusImg(dataBean.getPic(), R.drawable.default_pic_icon, iv_head_pic);
        tv_name.setText(dataBean.getName());
        tv_depart.setText("");//todo

        tv_address.setText(dataBean.getAddress());
        tv_time.setText(dataBean.getDate() + " " + dataBean.getTime());

        tv_licence_number.setText(dataBean.getCarNum());
    }

    private void initViewPager(){
        viewPager.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new ViewPagerItemHolder(itemView, CarGatherInfoActivity.this);
            }

            @Override
            public int getLayoutId() {
                return R.layout.car_gatherinfo_item;
            }
        }, dates)
                .setOnPageChangeListener(new OnPageChangeListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mCurrentPosition = position;
                        toolbarTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, position + 1, dates.size()));

                        initInfo(dates.get(position));
                    }
                })
                .setFirstItemPos(mCurrentPosition);
    }

    /**
     * 当前屏幕状态 全屏or显示菜单
     */
    private int currentState = STATE_SHOW_MENU;

    /**
     * 隐藏 或显示 标题栏，底部栏
     */
    public void toggleStateChange() {
        if (currentState == STATE_SHOW_MENU) {
            currentState = STATE_FULLSCREEN;
            bottomGroup.setVisibility(View.GONE);
            TopGroup.setVisibility(View.GONE);
        } else {
            currentState = STATE_SHOW_MENU;
            bottomGroup.setVisibility(View.VISIBLE);
            TopGroup.setVisibility(View.VISIBLE);
        }
    }
}
