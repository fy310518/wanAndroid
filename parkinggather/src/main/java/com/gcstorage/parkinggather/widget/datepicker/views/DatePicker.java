package com.gcstorage.parkinggather.widget.datepicker.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.widget.datepicker.bizs.decors.DPDecor;
import com.gcstorage.parkinggather.widget.datepicker.bizs.languages.DPLManager;
import com.gcstorage.parkinggather.widget.datepicker.bizs.themes.DPTManager;
import com.gcstorage.parkinggather.widget.datepicker.cons.DPMode;
import com.gcstorage.parkinggather.widget.datepicker.utils.MeasureUtil;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * DatePicker
 *
 * @author AigeStudio 2015-06-29
 */
public class DatePicker extends LinearLayout {
    private DPTManager mTManager;// 主题管理器
    private DPLManager mLManager;// 语言管理器

    private MonthView monthView;// 月视图
    private TextView tvYear, tvMonth;// 年份 月份显示
    private TextView tvEnsure;// 确定按钮显示
    private float DEFAULT_MONTH_VIEW_HEIGHT = 4.8F / 9F;


    private OnDateSelectedListener onDateSelectedListener;// 日期多选后监听

    private int centerYear, centerMonth;
    private OnMonthSwitchListener  onMonthSwitchListener;


    /** 月份切换监听器 */
    public interface OnMonthSwitchListener{
        void onMonthPicked(int centerYear, int centerMonth);
    }

    /**
     * 日期单选监听器
     */
    public interface OnDatePickedListener {
        void onDatePicked(String date);
    }

    /**
     * 日期多选监听器
     */
    public interface OnDateSelectedListener {
        void onDateSelected(List<String> date);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTManager = DPTManager.getInstance();
        mLManager = DPLManager.getInstance();

        // 设置排列方向为竖向
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker);
        float month_view_height_scale = a.getFloat(R.styleable.DatePicker_monthViewHeight, DEFAULT_MONTH_VIEW_HEIGHT);
        a.recycle();
        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams llMonthParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // 标题栏根布局
        RelativeLayout rlTitle = new RelativeLayout(context);
        rlTitle.setBackgroundColor(mTManager.colorTitleBG());
        //        int rlTitlePadding = MeasureUtil.dp2px(context, 10);
        //        rlTitle.setPadding(rlTitlePadding, rlTitlePadding, rlTitlePadding, rlTitlePadding);

        // 周视图根布局
        LinearLayout llWeek = new LinearLayout(context);
        llWeek.setBackgroundColor(mTManager.colorTitleBG());
        llWeek.setOrientation(HORIZONTAL);
        int llWeekPadding = MeasureUtil.dp2px(context, 20);
        llWeek.setPadding(0, llWeekPadding, 0, 0);
        LayoutParams lpWeek = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpWeek.weight = 1;

        View titleView = View.inflate(getContext(), R.layout.datepicker_title_layout, null);

        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(MATCH_PARENT, MeasureUtil.dp2px(getContext(), 45));
        lpTitle.addRule(RelativeLayout.CENTER_IN_PARENT);

        tvYear = (TextView) titleView.findViewById(R.id.year);
        tvMonth = (TextView) titleView.findViewById(R.id.mouth);
        View right = titleView.findViewById(R.id.right);
        View left = titleView.findViewById(R.id.left);
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(0);
            }
        });
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(1);
            }
        });
        rlTitle.addView(titleView, lpTitle);

        // 标题栏子元素布局参数
        //        RelativeLayout.LayoutParams lpYear =
        //                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        //        lpYear.addRule(RelativeLayout.CENTER_VERTICAL);
        //        RelativeLayout.LayoutParams lpMonth =
        //                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        //        lpMonth.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams lpEnsure =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpEnsure.addRule(RelativeLayout.CENTER_VERTICAL);
        lpEnsure.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //
        //        // --------------------------------------------------------------------------------标题栏
        //        // 年份显示
        //        tvYear = new TextView(context);
        //        tvYear.setText("2015");
        //        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        //        tvYear.setTextColor(mTManager.colorTitle());
        //
        //        // 月份显示
        //        tvMonth = new TextView(context);
        //        tvMonth.setText("六月");
        //        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        //        tvMonth.setTextColor(mTManager.colorTitle());
        //
        //        // 确定显示
        tvEnsure = new TextView(context);
        tvEnsure.setText(mLManager.titleEnsure());
        tvEnsure.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvEnsure.setTextColor(mTManager.colorTitle());
        tvEnsure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onDateSelectedListener) {
                    onDateSelectedListener.onDateSelected(monthView.getDateSelected());
                }
            }
        });
        //
        //        rlTitle.addView(tvYear, lpYear);
        //        rlTitle.addView(tvMonth, lpMonth);
        rlTitle.addView(tvEnsure, lpEnsure);

        addView(rlTitle, llParams);

        // --------------------------------------------------------------------------------周视图
        for (int i = 0; i < mLManager.titleWeek().length; i++) {
            TextView tvWeek = new TextView(context);
            tvWeek.setText(mLManager.titleWeek()[i]);
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tvWeek.setTextColor(getResources().getColor(R.color.font_gray));
            llWeek.addView(tvWeek, lpWeek);
        }
        addView(llWeek, llParams);

        // ------------------------------------------------------------------------------------月视图
        monthView = new MonthView(context);
        monthView.setHeightScale(month_view_height_scale);
        monthView.setOnDateChangeListener(new MonthView.OnDateChangeListener() {
            @Override
            public void onMonthChange(int month) {
                centerMonth = month;

                DatePicker.this.tvMonth.setText(mLManager.titleMonth()[month - 1]);
                if (null != onMonthSwitchListener)onMonthSwitchListener.onMonthPicked(centerYear, centerMonth);
            }

            @Override
            public void onYearChange(int year) {
                centerYear = year;

                String tmp = String.valueOf(year);
                if (tmp.startsWith("-")) {
                    tmp = tmp.replace("-", mLManager.titleBC());
                }
                DatePicker.this.tvYear.setText(tmp);
            }
        });
        addView(monthView, llMonthParams);
    }

    private void changeDate(int i) {
        int centerMonth = monthView.getCenterMonth();
        int centerYear = monthView.getCenterYear();
        if (i == 0) {
            centerMonth++;
            if (centerMonth > 12) {
                centerMonth = 1;
                centerYear += 1;
            }
        } else if (i == 1) {
            centerMonth--;
            if (centerMonth < 1) {
                centerMonth = 12;
                centerYear -= 1;
            }
        }
        Log.e("zjs","centerMonth = " + centerMonth);
        monthView.setDateWithChange(centerYear,centerMonth);
    }

    /**
     * 设置初始化年月日期
     *
     * @param year  ...
     * @param month ...
     */
    public void setDate(int year, int month) {
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }
        monthView.setDate(year, month);
    }

    public void setDPDecor(DPDecor decor) {
        monthView.setDPDecor(decor);
    }

    /**
     * 设置日期选择模式
     *
     * @param mode ...
     */
    public void setMode(DPMode mode) {
        if (mode != DPMode.MULTIPLE) {
            tvEnsure.setVisibility(GONE);
        }
        monthView.setDPMode(mode);
    }

    public void setFestivalDisplay(boolean isFestivalDisplay) {
        monthView.setFestivalDisplay(isFestivalDisplay);
    }

    public void setCanSrollHor(boolean canSrollHor) {
        monthView.setCanSrollHor(canSrollHor);
    }

    public void setCanSrollVer(boolean canSrollVer) {
        monthView.setCanSrcollVer(canSrollVer);
    }

    public void setTodayDisplay(boolean isTodayDisplay) {
        monthView.setTodayDisplay(isTodayDisplay);
    }

    public void setHolidayDisplay(boolean isHolidayDisplay) {
        monthView.setHolidayDisplay(isHolidayDisplay);
    }

    public void setDeferredDisplay(boolean isDeferredDisplay) {
        monthView.setDeferredDisplay(isDeferredDisplay);
    }

    public void setDescribeDisplay(boolean isDescribeDisplay) {
        monthView.setDescribeDisplay(isDescribeDisplay);
    }

    public void setShowAnima(boolean isShowBg){
        monthView.setShowAnima(isShowBg);
    }

    public void setBottomTextShow(boolean isBottomTextShow){
        monthView.setBottomTextShow(isBottomTextShow);
    }

    /**
     * 设置单选监听器
     *
     * @param onDatePickedListener ...
     */
    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener) {
        if (monthView.getDPMode() != DPMode.SINGLE) {
            throw new RuntimeException(
                    "Current DPMode does not SINGLE! Please call setMode set DPMode to SINGLE!");
        }
        monthView.setOnDatePickedListener(onDatePickedListener);
    }

    /**
     * 设置多选监听器
     *
     * @param onDateSelectedListener ...
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        if (monthView.getDPMode() != DPMode.MULTIPLE) {
            throw new RuntimeException(
                    "Current DPMode does not MULTIPLE! Please call setMode set DPMode to MULTIPLE!");
        }
        this.onDateSelectedListener = onDateSelectedListener;
    }

    /**
     * 设置月份切换监听器
     * @param onMonthSwitchListener
     */
    public void setOnMonthSwitchListener(OnMonthSwitchListener onMonthSwitchListener) {
        this.onMonthSwitchListener = onMonthSwitchListener;
    }
}
