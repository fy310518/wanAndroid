package com.fy.wanandroid.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.fy.baselibrary.application.ConfigUtils;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.permission.PermissionActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.statusbar.StatusBarContentColor;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.utils.cache.ACache;
import com.fy.wanandroid.R;
import com.fy.wanandroid.api.ApiService;
import com.fy.wanandroid.entity.LoginBean;
import com.fy.wanandroid.main.MainActivity;
import com.fy.wanandroid.utils.SelectUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity {
    private AppCompatActivity mContext;

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

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
        StatusBarContentColor.setStatusTextColor(this, true, false);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;

        Bundle bundle = new Bundle();
        bundle.putStringArray(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});

        JumpUtils.jump(this, PermissionActivity.class, bundle, PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);

        btnLogin.setBackground(SelectUtils.getBtnSelector(R.drawable.shape_btn));

        editPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = editPass.getText().toString().trim();

                if (!TextUtils.isEmpty(text) && text.length() > 12){
                    iLayoutPass.setError("密码不符合规则!!!");
                } else {
                    if(null != iLayoutPass.getError()){
                        iLayoutPass.setError(null);
                    }
                }

            }
        });
    }


    @OnClick({R.id.btnLogin, R.id.tvRegister})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.tvRegister:
                JumpUtils.jump(mContext, RegisterActivity.class, null);
//                JumpUtils.jump(mContext, StatusDemoActivity.class, null);
                break;
        }
    }

    @Override
    public void reTry() {
        login();
    }

    private void weiyu(){
        RequestUtils.create(ApiService.class)
                .booksByTag("奇幻", 1)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }

    private void login() {
        IProgressDialog progressDialog = new IProgressDialog().init(mContext)
                .setDialogMsg(R.string.user_login);

        String mUserName = editName.getText().toString().trim();//"fangshuai"
        String mPassWord = editPass.getText().toString().trim();//"fangs123"

        Map<String, Object> param = new HashMap<>();
        param.put("username", mUserName);
        param.put("password", mPassWord);

        RequestUtils.create(ApiService.class)
                .login(param)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<LoginBean>(progressDialog) {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        ACache mCache = ACache.get(ConfigUtils.getAppCtx());
                        mCache.put(Constant.userName, login);

                        SpfUtils.saveBooleanToSpf(Constant.isLogin, true);
                        SpfUtils.saveStrToSpf(Constant.userName, login.getUsername());

                        Bundle bundle = new Bundle();
                        bundle.putString("大王", "大王叫我来巡山");
                        JumpUtils.jump(mContext, MainActivity.class, bundle);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE) {
            switch (resultCode) {
                case PermissionActivity.CALL_BACK_RESULT_CODE_SUCCESS:
                    T.showLong("权限申请成功！");
                    break;
                case PermissionActivity.CALL_BACK_RESULE_CODE_FAILURE:
                    T.showLong("权限申请失败！");
                    break;
            }
        }
    }

    //保存点击的时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //处理 退出界面
            if ((System.currentTimeMillis() - exitTime) > 2000) {

                T.showLong(com.fy.baselibrary.R.string.exit_app);
                exitTime = System.currentTimeMillis();
            } else {
                JumpUtils.exitApp(mContext, StartActivity.class);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
