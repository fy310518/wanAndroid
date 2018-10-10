package com.fy.wanandroid.login;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fy.baselibrary.aop.annotation.ClickFilter;
import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.BaseActivityBean;
import com.fy.baselibrary.application.ConfigUtils;
import com.fy.baselibrary.application.IreTryActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
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
import com.fy.wanandroid.test.TestActivity;
import com.fy.wanandroid.utils.SelectUtils;

import org.reactivestreams.Subscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends AppCompatActivity implements IreTryActivity, View.OnClickListener {
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

        as();
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
//                JumpUtils.jump(this, StatusDemoActivity.class, null);
//                JumpUtils.jump(this, RevealEffectActivity.class, null);
                JumpUtils.jump(this, TestActivity.class, null);
                break;
        }
    }

    @Override
    public void reTry() {
        login();
    }

    @NeedPermission(value = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
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
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RequestUtils.addDispos(disposable);
                    }
                })
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

    private void as(){
        BaseActivityBean activityBean = (BaseActivityBean) getIntent()
                .getSerializableExtra("ActivityBean");

        Observable.interval(3, TimeUnit.SECONDS)
                .compose(RxHelper.bindToLifecycle(activityBean.getSubject()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {
                        Toast.makeText(LoginActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(final Long aLong) {
                        Toast.makeText(LoginActivity.this, "aLong:" + aLong, Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
