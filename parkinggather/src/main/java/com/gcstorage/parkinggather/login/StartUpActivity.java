package com.gcstorage.parkinggather.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.AppUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.gcstorage.map.HeartBeatMapService;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.main.MainActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 欢迎页
 * 注意：1、欢迎页，背景图片不能是 svg图片
 *      2、使用此欢迎页项目应用id 需要和项目包名一致
 * Created by fangs on 2017/12/12.
 */
public class StartUpActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    private int skip = 3;
    @BindView(R.id.tvSkip)
    TextView tvSkip;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.start_up_activity;
    }

    @StatusBar(statusColor = R.color.transparent, navColor = R.color.statusBar, applyNav = false, statusOrNavModel = 1)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        StatusBarContentColor.setStatusTextColor(this, true, true);

        tvSkip.setText(ResUtils.getReplaceStr(R.string.skip, skip));
        tvSkip.setBackground(ShapeBuilder.create()
                .radius(400)
                .solid(R.color.alphaBlack)
                .build());

        hideLoadView();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String otherAppId = bundle.getString("otherAppId", "com.gcstorage.newapp");
        String head_pic = bundle.getString("head_pic", "");    //用户头像
        String name = bundle.getString("name", "");          //用户姓名
        String depart = bundle.getString("depart", "");        //部门
        String deptID = bundle.getString("deptID", "");        //部门ID
        String policenum = bundle.getString("policenum", "");  //警号
        String idCard = bundle.getString("idCard", "");
        String userId = bundle.getString("sp_key_id", "");//用户id

        new SpfAgent(Constant.baseSpf)
                .saveString(Constant.otherAppID, otherAppId)
//                .saveBoolean(Constant.isLogin, true)
//                .saveString(Constant.userId, userId)
//                .saveString(Constant.userIdCard, idCard)
//                .saveString(Constant.userAccount, idCard)
//                .saveString(Constant.userName, name)
//                .saveString(Constant.userImg, head_pic)
//                .saveString(Constant.userAlarm, policenum)
//                .saveString(Constant.userDepart, depart)
//                .saveString(Constant.userDepartId, deptID)
                .commit(false);

        HeartBeatMapService.startMapService(this);
    }

    @ClickFilter()
    @OnClick({R.id.tvSkip})
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSkip) {
            intoMainOrLogin();
        }
    }

    @SuppressLint("CheckResult")
    private void hideLoadView() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(skip + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> tvSkip.setText(ResUtils.getReplaceStr(R.string.skip, skip - aLong)))
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(aLong -> {
                    if (aLong == 2L)intoMainOrLogin();
                });
    }

    /**
     * 根据条件 判断进入登录页还是主界面
     */
    @NeedPermission(value = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    private void intoMainOrLogin() {
        if (!SpfAgent.getBoolean(Constant.baseSpf, Constant.isLogin)) {
            JumpUtils.jump(this, AppUtils.getLocalPackageName() + ".login.LoginActivity", null);
        } else {
            JumpUtils.jump(this, MainActivity.class, null);
        }

        finish();
    }
}
