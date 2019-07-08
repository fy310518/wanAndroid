package com.gcstorage.parkinggather.history;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.DensityUtils;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.CalendarBean;
import com.gcstorage.parkinggather.widget.datepicker.bizs.calendars.DPCManager;
import com.gcstorage.parkinggather.widget.datepicker.bizs.decors.DPDecor;
import com.gcstorage.parkinggather.widget.datepicker.cons.DPMode;
import com.gcstorage.parkinggather.widget.datepicker.utils.MeasureUtil;
import com.gcstorage.parkinggather.widget.datepicker.views.DatePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * DESCRIPTION：驻车  采集历史
 * Created by fangs on 2019/7/8 17:51.
 */
public class CollectHistoryActivity extends AppCompatActivity implements IBaseActivity {

    private Calendar calendar;
    private DPCManager instance;
    private Map<String,String> dataCount = new HashMap<>();

    @BindView(R.id.dp_history)
    DatePicker dpDatePicker;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.collect_history_activity;
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        instance = DPCManager.getInstance();
        instance.clearnDATE_CACHE();

        initView();
        getNetData();
    }

    private void getNetData() {
//        api.getCalendarList(GlobalUserInfo.getAlarm(context), GlobalUserInfo.getToken(context), new ActionCallbackListener<CalendarBean>() {
//            @Override
//            public void onSuccess(CalendarBean data) throws GCSQLiteException, IOException {
//                getDateList(data);
//                dismiss();
//            }
//
//            @Override
//            public void onFailure(String errorEvent, String message) {
//                ToastUtils.showToast(context,message);
//                dismiss();
//            }
//        });
    }

    private void getDateList(CalendarBean data) {
        List<String> list = new ArrayList<>();
        for (String s : data.getDateList()) {
            list.add(formatDateString(s));
        }
        for (CalendarBean.InfoListBean infoListBean : data.getInfoList()) {
            dataCount.put(formatDateString(infoListBean.getDate()),infoListBean.getCarnum()+"辆");
        }
        instance.clearnDATE_CACHE();
        instance.setDecorBG(list);
        dpDatePicker.invalidate();
    }

    private void initView() {
        dpDatePicker.setTodayDisplay(true);
        dpDatePicker.setFestivalDisplay(false);
        dpDatePicker.setHolidayDisplay(false);
        dpDatePicker.setCanSrollVer(false);
        dpDatePicker.setDeferredDisplay(true);
        dpDatePicker.setShowAnima(false);
        dpDatePicker.setBottomTextShow(true);
        dpDatePicker.setMode(DPMode.SINGLE);
        calendar = Calendar.getInstance();
        dpDatePicker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        dpDatePicker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.rgb(18, 183, 245));
                canvas.drawCircle(rect.centerX(), rect.centerY(), DensityUtils.dp2px(15), paint);
            }

            @Override
            public void drawBottomText(Canvas canvas, Rect rect, Paint paint, String data) {
                paint.setTextSize(DensityUtils.dp2px(12f));
                paint.setColor(Color.BLACK);
                float height = paint.getFontMetrics().bottom - paint.getFontMetrics().top;
                float offset = Math.abs(paint.ascent() + paint.descent()) + height/2;
                if(dataCount.containsKey(data)){
                    canvas.drawText(dataCount.get(data), rect.centerX(),
                            rect.centerY() + offset + DensityUtils.dp2px(19f) , paint);
                }
            }
        });

        dpDatePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                if(dataCount.containsKey(date)){
//                    String reFormatDate = reFormatDate(date);
//                    Intent intent = new Intent(CollectHistoryActivity.this, ParkCarMapActivity.class);
//                    intent.putExtra("time",reFormatDate);
//                    startActivity(intent);
                }
            }
        });
    }

    private String reFormatDate(String date) {
        String[] split = date.split("-");
        split[1] = addZero(split[1]);
        split[2] = addZero(split[2]);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("-");
        sb.append(split[1]);
        sb.append("-");
        sb.append(split[2]);
        return sb.toString();
    }

    private String addZero(String s) {
        if (s.length() < 2){
            return "0" + s;
        }
        return s;
    }

    private String formatDateString(String s) {
        String[] split = s.split("-");
        split[1] = delfirstZero(split[1]);
        split[2] = delfirstZero(split[2]);
        StringBuilder sb = new StringBuilder();
        sb.append(split[0]);
        sb.append("-");
        sb.append(split[1]);
        sb.append("-");
        sb.append(split[2]);
        return sb.toString();
    }

    private String delfirstZero(String str){
        if(str.startsWith("0")){
            return str.substring(1);
        }
        return str;
    }
}
