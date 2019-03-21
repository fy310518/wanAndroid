package com.fy.wanandroid.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;

import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.base.mvp.BaseMVPActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.main.MainActivity;
import com.fy.wanandroid.request.NetDialog;
import com.fy.wanandroid.test.TestListActivity;
import com.fy.wanandroid.utils.SelectUtils;
import com.gcstorage.circle.CircleActivity;
import com.gcstorage.circle.bean.LyLoginBean;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends BaseMVPActivity<LogingPresenter> implements IBaseActivity, View.OnClickListener, LoginContract.LoginView {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.editName)
    TextInputEditText editName;


    @BindView(R.id.iLayoutPass)
    TextInputLayout iLayoutPass;
    @BindView(R.id.editPass)
    TextInputEditText editPass;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected LogingPresenter createPresenter() {
        return new LogingPresenter();
    }

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_login;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        btnLogin.setBackground(SelectUtils.getBtnSelector(R.drawable.shape_btn, 0));
        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editPass.getText().toString().trim();

                if (!TextUtils.isEmpty(text) && text.length() > 12) {
                    iLayoutPass.setError("密码不符合规则!!!");
                } else {
                    if (null != iLayoutPass.getError()) {
                        iLayoutPass.setError(null);
                    }
                }
            }
        });
    }

    @ClickFilter
    @OnClick({R.id.btnLogin, R.id.tvRegister})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String mUserName = editName.getText().toString().trim();//"fangshuai"
                String mPassWord = editPass.getText().toString().trim();//"fangs123"

//                mPresenter.login(mUserName, mPassWord);
                lylogin();
                break;
            case R.id.tvRegister:
//                JumpUtils.jump(mContext, RegisterActivity.class, null);
//                JumpUtils.jump(this, StatusDemoActivity.class, null);
//                JumpUtils.jump(this, TestStatusFragmentActivity.class, null);
//                JumpUtils.jump(this, RevealEffectActivity.class, null);
//                JumpUtils.jump(this, TestActivity.class, null);
                JumpUtils.jump(this, TestListActivity.class, null);
                break;
        }
    }

    @Override
    public void loginSuccess(LoginBean login) {
        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
        mCache.put(Constant.userName, login);

        new SpfAgent(Constant.baseSpf)
                .saveBoolean(Constant.isLogin, true)
                .saveString(Constant.userName, login.getUsername())
                .commit(false);

        Bundle bundle = new Bundle();
        bundle.putString("大王", "大王叫我来巡山");
        JumpUtils.jump(LoginActivity.this, MainActivity.class, bundle);
    }

    @NeedPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE})
    @SuppressLint("CheckResult")
    public void lylogin() {
        IProgressDialog progressDialog = new NetDialog().init(this)
                .setDialogMsg(R.string.user_login);

        ArrayMap<String, Object> param = new ArrayMap<>();
        param.put("action", "login");
        param.put("alarm", "18871175373");
        param.put("passwd", "123456");
        param.put("userOwnUUID", "");

        RequestUtils.create(ApiService.class)
                .lyLogin(param)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<List<LyLoginBean>>(progressDialog) {
                    @Override
                    protected void onSuccess(List<LyLoginBean> data) {

                        if (null != data && data.size() > 0){
                            LyLoginBean loginBean = data.get(0);

                            new SpfAgent(Constant.baseSpf)
                                    .saveString(Constant.userName, loginBean.getAlarm())
                                    .saveString(Constant.token, loginBean.getToken())
                                    .commit(false);

                            Bundle bundle = new Bundle();
                            bundle.putString("大王", "大王叫我来巡山");
                            JumpUtils.jump(LoginActivity.this, CircleActivity.class, bundle);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        JumpUtils.backDesktop(this);
    }
}
