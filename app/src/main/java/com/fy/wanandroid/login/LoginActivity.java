package com.fy.wanandroid.login;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.fy.baselibrary.base.mvp.BaseMVPActivity;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.main.MainActivity;
import com.fy.wanandroid.test.H5Interactive;
import com.fy.wanandroid.testdemo.StatusDemoActivity;
import com.fy.wanandroid.utils.SelectUtils;

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
    @NeedPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    @OnClick({R.id.btnLogin, R.id.tvRegister})
    @Override
    public void onClick(View view) {
        String mUserName = editName.getText().toString().trim();//"fangshuai"
        switch (view.getId()) {
            case R.id.btnLogin:
                String mPassWord = editPass.getText().toString().trim();//"fangs123"

                mPresenter.login(mUserName, mPassWord);
                break;
            case R.id.tvRegister:
//                JumpUtils.jump(mContext, RegisterActivity.class, null);
                JumpUtils.jump(this, StatusDemoActivity.class, null);
//                JumpUtils.jump(this, TestStatusFragmentActivity.class, null);
//                JumpUtils.jump(this, RevealEffectActivity.class, null);
//                JumpUtils.jump(this, TestActivity.class, null);
//                JumpUtils.jump(this, TestListActivity.class, null);
//                JumpUtils.jump(this, H5Interactive.class, null);
                break;
        }
    }

    @Override
    public void loginSuccess(LoginBean login) {
        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
        mCache.put(Constant.userName, login);

        SpfAgent.init(Constant.baseSpf)
                .saveString(Constant.userName, login.getUsername())
                .commit(false);

        Bundle bundle = new Bundle();
        bundle.putString("大王", "大王叫我来巡山");
        JumpUtils.jump(LoginActivity.this, MainActivity.class, bundle);
    }

    @Override
    public void onBackPressed() {
        JumpUtils.backDesktop(this);
    }

}
