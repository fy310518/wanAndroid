package com.gcstorage.parkinggather.querycar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.TimeUtils;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.fy.baselibrary.utils.drawable.TintUtils;
import com.fy.baselibrary.utils.notify.T;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.util.PGAppUtils;
import com.gcstorage.parkinggather.util.TimeSelector;
import com.gcstorage.parkinggather.widget.PlateNumEditText;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * DESCRIPTION：一键查车（车辆查询）
 * Created by fangs on 2019/7/2 17:47.
 */
public class QueryCarActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.llEdit)
    View llEdit;

    @BindView(R.id.tv_province_abbr)
    TextView tv_province_abbr;
    @BindView(R.id.et_carnum)
    PlateNumEditText et_carnum;

    @BindView(R.id.txtStartTime)
    TextView txtStartTime;
    @BindView(R.id.txtEndTime)
    TextView txtEndTime;

    @BindView(R.id.radioLayout)
    RadioGroup radioLayout;
    @BindView(R.id.rBtnOne)
    RadioButton rBtnOne;
    @BindView(R.id.rBtnTwo)
    RadioButton rBtnTwo;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.query_car_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initView();

    }

    @OnClick({R.id.tv_province_abbr, R.id.txtEndTime, R.id.txtStartTime, R.id.btn_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_province_abbr://选择 车牌 所在的 省 直辖市简称
                showPopup(v);
                break;
            case R.id.txtStartTime:
                showDateSelect(txtStartTime);
                break;
             case R.id.txtEndTime:
                 showDateSelect(txtEndTime);
                break;
            case R.id.btn_search://底部搜索按钮

                break;
        }
    }

    private void initView(){
        //初始化 起止时间（间隔 一天）
        mStartDate = new Date(System.currentTimeMillis() - 24 * 3600 * 1000);
        String startTimeContent = ResUtils.getReplaceStr(R.string.startTime, TimeUtils.Data2String(mStartDate, "yyyy-MM-dd HH:mm"));
        txtStartTime.setText(startTimeContent);

        mEndDate = new Date(System.currentTimeMillis());
        String endTimeContent = ResUtils.getReplaceStr(R.string.endTime, TimeUtils.Data2String(mEndDate, "yyyy-MM-dd HH:mm"));
        txtEndTime.setText(endTimeContent);



        TintUtils.setTxtIconLocal(rBtnOne,
                PGAppUtils.getSelector(R.drawable.genderchoose_check, 0, R.color.button_pressed, R.color.transparent),
                3);
        TintUtils.setTxtIconLocal(rBtnTwo,
                PGAppUtils.getSelector(R.drawable.genderchoose_check, 0, R.color.button_pressed, R.color.transparent),
                3);
        rBtnOne.setChecked(true);
        llEdit.setBackground(ShapeBuilder.create().solid(R.color.transparent).radius(8).stroke(1, R.color.frameStrokeColor).build());
    }


    private Date mStartDate;
    private Date mEndDate;
    //显示时间选择器
    private void showDateSelect(TextView txt){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(Date time) {
                int strid;
                if (txt.getId() == R.id.txtStartTime){
                    mStartDate = time;
                    strid = R.string.startTime;
                } else {
                    mEndDate = time;
                    strid = R.string.endTime;
                }

                if (mStartDate.getTime() > mEndDate.getTime()) {
                    T.showLong("开始时间不能大于结束时间");
                    return;
                }

                String timeContent = ResUtils.getReplaceStr(strid, TimeUtils.Data2String(time, "yyyy-MM-dd HH:mm"));
                txt.setText(timeContent);
            }

            @Override
            public void city(String sheng, String city, String qu) {

            }
        }, "1900-01-01 00:00", "2100-12-31 23:59");

        if (null == mStartDate)  mStartDate = new Date();
        if (null == mEndDate)  mEndDate = new Date();

        timeSelector.setIsLoop(true);
        timeSelector.show(txt.getId() == R.id.txtStartTime ? mStartDate : mEndDate);
    }

    QueryCarPopup popup;
    //显示 弹窗
    private void showPopup(View view) {
        //得到button的左上角坐标
        int[] positions = new int[2];
        view.getLocationOnScreen(positions);

        if (null == popup) {
            popup = new QueryCarPopup();
            popup.setClickListener(item -> tv_province_abbr.setText(item));
            popup.setAnim(R.style.AnimTop)
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .onCreateView(this);
        }

        popup.bgAlpha()
                .showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY,
                        positions[0], positions[1] + view.getHeight());
    }
}
