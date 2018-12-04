package com.fy.wanandroid.login;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.statuslayout.StatusLayout;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.main.MainActivity;
import com.fy.wanandroid.request.ApiService;
import com.fy.wanandroid.request.NetCallBack;
import com.fy.wanandroid.request.NetDialog;
import com.fy.wanandroid.testdemo.StatusDemoActivity;
import com.fy.wanandroid.utils.SelectUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity, StatusLayout.OnRetryListener, View.OnClickListener {
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
        L.e(getTaskId() + "----> LoginActivity");
        btnLogin.setBackground(SelectUtils.getBtnSelector(R.drawable.shape_btn, 0));
//        requestPermission();
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
                login();
                break;
            case R.id.tvRegister:
//                JumpUtils.jump(mContext, RegisterActivity.class, null);
                JumpUtils.jump(this, StatusDemoActivity.class, null);
//                JumpUtils.jump(this, TestStatusFragmentActivity.class, null);
//                JumpUtils.jump(this, RevealEffectActivity.class, null);
//                JumpUtils.jump(this, TestActivity.class, null);
                break;
        }
    }

    @Override
    public void onRetry() {
        login();
    }

    @NeedPermission(value = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, isRun = true)
    private void login() {
        IProgressDialog progressDialog = new NetDialog().init(this)
                .setDialogMsg(R.string.user_login);

        String mUserName = editName.getText().toString().trim();//"fangshuai"
        String mPassWord = editPass.getText().toString().trim();//"fangs123"

        Map<String, Object> param = new HashMap<>();
        param.put("username", mUserName);
        param.put("password", mPassWord);

        RequestUtils.create(ApiService.class)
                .login(param)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(new NetCallBack<LoginBean>(progressDialog) {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                        mCache.put(Constant.userName, login);

                        SpfUtils.saveBooleanToSpf(Constant.isLogin, true);
                        SpfUtils.saveStrToSpf(Constant.userName, login.getUsername());

                        Bundle bundle = new Bundle();
                        bundle.putString("大王", "大王叫我来巡山");
                        JumpUtils.jump(LoginActivity.this, MainActivity.class, bundle);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }

}
