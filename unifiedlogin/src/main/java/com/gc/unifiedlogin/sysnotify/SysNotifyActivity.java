package com.gc.unifiedlogin.sysnotify;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.gc.unifiedlogin.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * describe: 显示门户发送 的广播消息列表
 * Created by fangs on 2019/5/25 11:53.
 */
public class SysNotifyActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.rvNotifyList)
    RecyclerView rvNotifyList;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.system_notify_list_activity;
    }

    @StatusBar(statusColor = R.color.white, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRv();
    }

    private void initRv(){
//        adapterTwo = new AdapterTwo(this, new ArrayList<>());

        rvNotifyList.setLayoutManager(new LinearLayoutManager(this));
        rvNotifyList.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(this));

//        rvNotifyList.setAdapter(adapterTwo);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
    }

}
