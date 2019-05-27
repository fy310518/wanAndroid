package com.gc.unifiedlogin.sysnotify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.utils.GsonUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.widget.refresh.EasyPullLayout;
import com.fy.baselibrary.widget.refresh.OnRefreshListener;
import com.gc.unifiedlogin.R;
import com.google.gson.JsonObject;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

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
    AppNotifyAdapter notifyAdapter;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.system_notify_list_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initRv();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sysNotifyReceiver);
    }

    private void initRv(){

        List<MsgData> newsList = LitePal
                .order("SEND_TIME asc")
                .limit(10)
                .find(MsgData.class);

        notifyAdapter = new AppNotifyAdapter(this, newsList);

        rvNotifyList.setLayoutManager(new LinearLayoutManager(this));
        rvNotifyList.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false)
                .create(this));

        rvNotifyList.setAdapter(notifyAdapter);

        epl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<MsgData> newsList = LitePal
                        .order("SEND_TIME asc")
                        .limit(5).offset(5)
                        .find(MsgData.class);

                notifyAdapter.addData(newsList);
                notifyAdapter.notifyItemRangeChanged(0, newsList.size());
                rvNotifyList.scrollToPosition(0);

                epl.stop();
            }
        });
    }




    /**
     * 注册消息相关的广播接收者
     */
    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MESSAGE_ARRIVED");
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.setPriority(100);
        registerReceiver(sysNotifyReceiver, filter);
    }


    BroadcastReceiver sysNotifyReceiver = new BroadcastReceiver() {

        public static final String BROADCAsssST_ACTION_CHAT_MESSAGE = "android.intent.action.MESSAGE_ARRIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message", "");
            JsonObject sysNotifyJson = GsonUtils.jsonStrToJsonObj(message);
            String msgDataStr = sysNotifyJson.get("MSG").getAsJsonObject().get("DATA").getAsJsonObject().toString();

            MsgData msgData = GsonUtils.fromJson(msgDataStr, MsgData.class);
            msgData.save();

            List<MsgData> newsList = LitePal
                    .order("SEND_TIME desc")
                    .limit(1)
                    .find(MsgData.class);

            L.e("大王", "--------------------------------------------------");
            notifyAdapter.addData(newsList);
            notifyAdapter.notifyItemInserted(notifyAdapter.getItemCount());
            rvNotifyList.scrollToPosition(notifyAdapter.getItemCount());
        }
    };
}
