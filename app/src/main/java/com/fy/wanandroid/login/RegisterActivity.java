package com.fy.wanandroid.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.ioc.ConfigUtils;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.IProgressDialog;
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
import com.fy.wanandroid.utils.SelectUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 * Created by fangs on 2018/3/30.
 */
public class RegisterActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    private AppCompatActivity mContext;

    @BindView(R.id.editRegisterName)
    TextInputEditText editRegisterName;

    @BindView(R.id.tilRegisterPass)
    TextInputLayout tilRegisterPass;
    @BindView(R.id.editRegisterPass)
    TextInputEditText editRegisterPass;

    @BindView(R.id.tilRegisterPass2)
    TextInputLayout tilRegisterPass2;
    @BindView(R.id.editRegisterPass2)
    TextInputEditText editRegisterPass2;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_register;
    }

    @StatusBar(statusColor = R.color.statusBar, navColor = R.color.statusBar)
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;
        btnRegister.setBackground(SelectUtils.getBtnSelector(R.drawable.shape_btn, 0));
    }

    @OnClick({R.id.btnRegister})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {//正则
        IProgressDialog progressDialog = new NetDialog().init(mContext)
                .setDialogMsg(R.string.register_loading);

        String mUserName = editRegisterName.getText().toString().trim();
        String mPassWord = editRegisterPass.getText().toString().trim();

        Map<String, Object> param = new HashMap<>();
        param.put("username", mUserName);
        param.put("password", mPassWord);
        param.put("repassword", mPassWord);

        RequestUtils.create(ApiService.class)
                .register(param)
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
                        JumpUtils.jump(mContext, MainActivity.class, bundle);
                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }
}
