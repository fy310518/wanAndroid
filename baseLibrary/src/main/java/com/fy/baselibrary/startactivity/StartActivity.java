package com.fy.baselibrary.startactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;

/**
 * 不可见Activity 用于控制程序 退出(入口activity)
 * Created by fangs on 2017/4/26.
 */
public class StartActivity extends AppCompatActivity implements IBaseActivity {

    private static final String FLAG_EXIT = "FLAG_EXIT_APP";

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return 0;
    }

    @Override
    public void setStatusBar(Activity activity) {
//        MdStatusBar.setColorBar(activity, R.color.transparent, R.color.transparent);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        //缓存应用进程id
        SpfUtils.saveIntToSpf(Constant.appProcessId, AppUtils.getProcessId(activity));

//        if (null != savedInstanceState && savedInstanceState.getBoolean(FLAG_EXIT)) {
//            FileUtils.fileToInputContent("log", "日志.txt", "onCreate(): savedInstanceState" + AppUtils.getProcessId(this));
//            exitApp();
//            return;
//        }

        exitOrIn(getIntent());
    }

    @Override
    public void reTry() {}

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (null != outState) {
//            outState.putBoolean(FLAG_EXIT, true);
//            L.e("StartActivity", "onSaveInstanceState");
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        L.e("StartActivity", "onNewIntent- false");

        boolean b = (Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0;
        boolean c = intent.getBooleanExtra("exitApp", false);
        if (b && c) {
            exitApp();
        }
    }

    // 避免从桌面启动程序后，会重新实例化入口类的activity
    private void isStartActivityOnly() {
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    exitApp();
                    return;
                }
            }
        } else {
            startActivity(new Intent(this, StartUpActivity.class));
        }
    }

    /**
     * 根据intent 判断进入应用还是退出应用
     * @param intent
     */
    private void exitOrIn(Intent intent){
        boolean b = (Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0;
        boolean c = intent.getBooleanExtra("exitApp", false);

        if (b && c) {
            exitApp();
        } else {
            isStartActivityOnly();
        }
    }

    /**
     * 退出应用
     */
    private void exitApp() {
        SpfUtils.remove(Constant.appProcessId);//正常退出程序 清理缓存的进程id
        finish();
        System.exit(0);
    }
}
