package com.gcstorage.parkinggather.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.view.View;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.gcstorage.parkinggather.Constant;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.LoginEntity;
import com.gcstorage.parkinggather.bean.PersonEntity;
import com.gcstorage.parkinggather.main.MainActivity;
import com.gcstorage.parkinggather.request.ApiService;
import com.gcstorage.parkinggather.request.NetCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * DESCRIPTION：登录 activity
 * Created by fangs on 2019/7/1 16:33.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    @BindView(R.id.editId)
    TextInputEditText editId;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.login_activity;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

    }


    @OnClick({R.id.btnLogin})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login();
                break;
        }
    }

    //todo 简单的 登录操作
    private void login(){
        String idCard = editId.getText().toString().trim().equals("") ?
                "420621199202074539" : editId.getText().toString().trim();

        RequestUtils.create(ApiService.class)
                .getPersonInfo(idCard)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<PersonEntity>() {
                    @Override
                    protected void onSuccess(PersonEntity personEntity) {

                        if (null == personEntity) return;

                        new SpfAgent(Constant.baseSpf)
                                .saveBoolean(Constant.isLogin, true)
                                .saveString(Constant.userId, personEntity.getId())
                                .saveString(Constant.userIdCard, personEntity.getAccount())
                                .saveString(Constant.userAccount, personEntity.getAccount())
                                .saveString(Constant.userName, personEntity.getName())
                                .saveString(Constant.userImg, personEntity.getHead_pic())
                                .saveString(Constant.userAlarm, personEntity.getPolicenum())
                                .saveString(Constant.userDepart, personEntity.getDepart())
                                .saveString(Constant.userDepartId, personEntity.getOrgid())
                                .commit(false);

                        JumpUtils.jump(LoginActivity.this, MainActivity.class, null);
                    }
                });

//        ArrayMap<String, String> params = new ArrayMap<>();
//        params.put("action", "userlogind");
//        params.put("alarm", "999999");
//        params.put("passwd", "123456");
//        params.put("userOwnUUID","");
//        RequestUtils.create(ApiService.class)
//                .login(params)
//                .compose(RxHelper.handleResult())
//                .compose(RxHelper.bindToLifecycle(this))
//                .subscribe(new NetCallBack<List<LoginEntity>>() {
//                    @Override
//                    protected void onSuccess(List<LoginEntity> userList) {
//
//                        if (null == userList || userList.isEmpty()) return;
//
//                        LoginEntity user = userList.get(0);
//                        new SpfAgent(Constant.baseSpf)
//                                .saveBoolean(Constant.isLogin, true)
//                                .saveString(Constant.userIdCard, user.getIDCard())
//                                .saveString(Constant.userAccount, user.getUsername())
//                                .saveString(Constant.userName, user.getName())
//                                .saveString(Constant.userImg, user.getHeadpic())
//                                .saveString(Constant.userAlarm, user.getAlarm())
//                                .saveString(Constant.token, user.getToken())
//                                .commit(false);
//
//                        JumpUtils.jump(LoginActivity.this, MainActivity.class, null);
//                    }
//                });
    }
}
