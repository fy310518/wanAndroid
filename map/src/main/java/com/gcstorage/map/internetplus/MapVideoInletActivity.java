package com.gcstorage.map.internetplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.gcstorage.framework.utils.DipPxUtil;
import com.gcstorage.framework.utils.Logger;
import com.gcstorage.map.HeartBeatMapService;
import com.gcstorage.map.shake.scrolllayout.ScrollLayout;
import com.gcstorage.map.utils.ScreenUtil;
import com.gcstorage.parkinggather.Constant;
import com.jeff.map.R;

import java.util.List;

//ApplyShakeResultGisActivity 参考
public class MapVideoInletActivity extends FragmentActivity implements View.OnClickListener {
    ScrollLayout mScrollLayout;
    ListView mListView;
    LinearLayout ll_foot;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            Log.v("cProgress", "-------   " + currentProgress);
            if (ll_foot.getVisibility() == View.VISIBLE) {
                ll_foot.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                Log.v("cProgress", " ****  EXIT  *****");
                ll_foot.setVisibility(View.VISIBLE);
            } else if (currentStatus.equals(ScrollLayout.Status.CLOSED)) {
                Log.v("cProgress", " ****  CLOSED  *****");
            } else if (currentStatus.equals(ScrollLayout.Status.OPENED)) {
                Log.v("cProgress", " ****  OPENED  *****");
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };
    private InternetMapFragment shakeMapFragment;
    private String currAlarm;
    private String currName;
    private TextView tv_serach;
    private ImageView iv_clear;
    private EditText autotext;
    private InternetMapVideoApi api;
    private InternetVideoAdapter internetVideoAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_inletvideo);
        currAlarm = getIntent().getStringExtra("curr_alarm");
        currName = getIntent().getStringExtra("curr_name");
        api = new InternetMapVideoApi();
        initView();
        initData();

    }


    private void initView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mScrollLayout = findViewById(R.id.scroll_down_layout);
        mListView = findViewById(R.id.list_view);
        ll_foot = findViewById(R.id.ll_foot);

        tv_serach = findViewById(R.id.tv_serach);
        iv_clear = findViewById(R.id.iv_clear);
        autotext = findViewById(R.id.autotext);
        tv_serach.setOnClickListener(this);
    }

    private void initData() {
        double sLatitude = Constant.sLatitude;
        double sLongitude = Constant.sLongitude;
        api.getVideoListByLL(this, String.valueOf(sLongitude), String.valueOf(sLatitude), currName, currAlarm, new ActionCallbackListener<List<InternetPlusBean>>() {
            @Override
            public void onSuccess(List<InternetPlusBean> data) throws Exception {
                if (data != null && data.size() != 0) {
                    adapterDisplay(data);
                } else {
                    Toast.makeText(MapVideoInletActivity.this.getApplicationContext(),
                            "暂无数据", Toast.LENGTH_SHORT).show();
                    mScrollLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Toast.makeText(MapVideoInletActivity.this.getApplicationContext(),
                        "暂无数据onFailure", Toast.LENGTH_SHORT).show();
                mScrollLayout.setVisibility(View.GONE);
                initMapFragment(null);
            }
        });
    }

    private View mHeadView;

    private void adapterDisplay(final List<InternetPlusBean> mData) {
        mScrollLayout.setVisibility(View.VISIBLE);
        internetVideoAdapter = new InternetVideoAdapter();
        internetVideoAdapter.setData(this, mData);
        mListView.setAdapter(internetVideoAdapter);
        mHeadView = View.inflate(this, R.layout.headview_shake, null);
        mListView.addHeaderView(mHeadView);
        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(DipPxUtil.dip2px(this, 88));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToOpen();
        mScrollLayout.getBackground().setAlpha(0);
        ll_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position = i - mListView.getHeaderViewsCount();
                if (position < 0 || position > mData.size() - 1) {
                    return;
                }

                if (!mScrollLayout.getCurrentStatus().equals(ScrollLayout.Status.OPENED)) {
                    mScrollLayout.setToOpen();
                }
                shakeMapFragment.moveCheckedMarker(position);
                //跳转到视频查看页
                InternetPlusBean data = mData.get(position);
                if (data == null) {
                    Toast.makeText(MapVideoInletActivity.this, "未获取到设备信息", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (currAlarm == null || currName == null) {
                        Toast.makeText(MapVideoInletActivity.this, "未获取到用户信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("internet_data", data);
                    intent.putExtra("curr_alarm", currAlarm);
                    intent.putExtra("curr_name", currName);
                    intent.setClass(MapVideoInletActivity.this, InternetVideActivity.class);
                    startActivity(intent);
                }
            }
        });
        initMapFragment(mData);
    }

    public void clickMarker() {
        if (!mScrollLayout.getCurrentStatus().equals(ScrollLayout.Status.OPENED)) {
            mScrollLayout.setToOpen();
        }
    }

    private void initMapFragment(List<InternetPlusBean> mData) {
        shakeMapFragment = InternetMapFragment.newInstance(mData);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_map, shakeMapFragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_serach) {
            String s = autotext.getText().toString();
            if (s.isEmpty()) {
                Toast.makeText(MapVideoInletActivity.this.getApplicationContext(),
                        "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            } else {
                searchVideo(s);
            }
        }
    }

    private void searchVideo(String keyword) {
        api.getVideoListBySeeach(this, "", "", keyword, "", "", "", "", currAlarm, new ActionCallbackListener<List<InternetPlusBean>>() {
            @Override
            public void onSuccess(List<InternetPlusBean> data) throws Exception {
                Logger.d("dong", "搜索结果+ " + data.size());
                if (data != null && data.size() != 0) {
                    //搜索结果刷新适配器
                    internetVideoAdapter.setData(MapVideoInletActivity.this, data);
                    internetVideoAdapter.notifyDataSetChanged();
                    //todo 搜索结果刷新地图页面数据
                    shakeMapFragment.setSearchData(data);
                } else {
                    Toast.makeText(MapVideoInletActivity.this.getApplicationContext(),
                            "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                Toast.makeText(MapVideoInletActivity.this.getApplicationContext(),
                        "没有搜索到相关数据onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
