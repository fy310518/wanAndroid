package com.gcstorage.map.shake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gcstorage.framework.utils.DipPxUtil;
import com.gcstorage.map.HeartBeatMapService;
import com.gcstorage.map.utils.ScreenUtil;
import com.gcstorage.parkinggather.Constant;
import com.jeff.map.R;
import com.leador.api.maps.MapUtils;
import com.leador.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

import com.gcstorage.map.shake.adapter.ApplyShakeResultAdapter;
import com.gcstorage.map.shake.api.ShakeCameraModel;
import com.gcstorage.map.shake.scrolllayout.ScrollLayout;


public class ApplyShakeResultGisActivity extends FragmentActivity {
    public static String NAME = "video_name";
    public static String ID = "video_id";
    public static String IP = "video_ip";
    public static String RTSP = "video_rtsp";
    public static String ADDRESS = "video_address";

    ScrollLayout mScrollLayout;
    TextView tv_title;
    ListView mListView;
    LinearLayout ll_foot;

    private ArrayList<ShakeCameraModel> mData;
    private View mHeadView;
    private Context mContext = ApplyShakeResultGisActivity.this;


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

    private ShakeMapGisFragment shakeMapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_shake_result);
        mScrollLayout = findViewById(R.id.scroll_down_layout);
        tv_title = findViewById(R.id.tv_title);
        mListView = findViewById(R.id.list_view);
        ll_foot = findViewById(R.id.ll_foot);

        mData = (ArrayList<ShakeCameraModel>) getIntent().getSerializableExtra("shake_data");
        int shakeType = getIntent().getIntExtra("shake_type", 2);
        if (mData != null) {
            tv_title.setText("视频查看");
            mData = sortData(mData);
            mScrollLayout.setVisibility(View.VISIBLE);
            mListView.setAdapter(new ApplyShakeResultAdapter(this, mData, shakeType));
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
//                    shakeMapFragment.moveCheckedMarker(position);
                    //跳转到视频查看页
                    ShakeCameraModel data = mData.get(position);
                    Intent intent = new Intent();
                    intent.putExtra(NAME, data.NAME);
                    intent.putExtra(ADDRESS, data.ADDRESS);
                    intent.putExtra(IP, data.IP);
                    intent.putExtra(RTSP, data.camera_rtsp);  //警务通的真是RTSP地址
//                    intent.putExtra(RTSP, "http://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8");
//                    intent.putExtra(RTSP, "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov"); //用来互联网测试,写死
                    intent.putExtra(ID, data._id);
                    intent.setClass(mContext, CamerasVideoActivity.class);
                    startActivity(intent);

                }
            });
            initMapFragment(mData);
        } else {
            mScrollLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void clickMarker() {
        if (!mScrollLayout.getCurrentStatus().equals(ScrollLayout.Status.OPENED)) {
            mScrollLayout.setToOpen();
        }
    }

    /**
     * 初始化摇一摇页面地图
     */
    private void initMapFragment(ArrayList<ShakeCameraModel> data) {
        shakeMapFragment = ShakeMapGisFragment.newInstance(data);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_map, shakeMapFragment)
                .commit();
    }

    /**
     * 根据距离升序
     *
     * @param data
     * @return
     */
    private ArrayList<ShakeCameraModel> sortData(ArrayList<ShakeCameraModel> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ShakeCameraModel bean = data.get(i);
                bean.distance = (int) MapUtils.calculateLineDistance(
                        new LatLng(Constant.sLatitude, Constant.sLongitude),
                        new LatLng(bean.gps_w, bean.gps_h));
            }

            Collections.sort(data, new ShakeCameraModel.DistanceComparator());
        }
        return data;
    }
}
