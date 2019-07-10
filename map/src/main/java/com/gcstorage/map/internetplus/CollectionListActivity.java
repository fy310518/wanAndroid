package com.gcstorage.map.internetplus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcstorage.framework.net.listener.ActionCallbackListener;
import com.jeff.map.R;

import java.util.List;

public class CollectionListActivity extends Activity {

    private String userID;
    private AdapterCollection recycleAdapter;
    private String watchHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);
        userID = getIntent().getStringExtra("curr_alarm");
        watchHistory = getIntent().getStringExtra("watch_history");
        initView();
        //判断是否历史数据
        if (watchHistory != null && watchHistory.equals("history") && userID != null && !userID.isEmpty()) {

        } else {
            initData();
        }

    }


    private void initView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rc_list);
        recycleAdapter = new AdapterCollection();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {

        InternetMapVideoApi instan = InternetMapVideoApi.getInstan();
        instan.serchCollectionList(this, userID, new ActionCallbackListener<List<InternetPlusBean>>() {
            @Override
            public void onSuccess(List<InternetPlusBean> data) throws Exception {
                if (data != null && data.size() != 0) {
                    recycleAdapter.setData(data);
                }
            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });
    }
}
