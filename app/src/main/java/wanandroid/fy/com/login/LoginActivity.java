package wanandroid.fy.com.login;

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

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.permission.PermissionActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.T;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.LoginBean;
import wanandroid.fy.com.status.StatusDemoActivity;

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

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_login;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
//        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;

        Bundle bundle = new Bundle();
        bundle.putStringArray(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});

        JumpUtils.jump(this, PermissionActivity.class, bundle, PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);

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
                break;
        }
    }

    @Override
    public void reTry() {
        login();
    }

    private void login() {
        IProgressDialog progressDialog = new IProgressDialog().init(mContext)
                .setDialogMsg(R.string.user_login);

        String mUserName = editName.getText().toString().trim();
        String mPassWord = editPass.getText().toString().trim();

        Map<String, Object> param = new HashMap<>();
        param.put("username", "fangshuai");
        param.put("password", "fangs123");

        RequestUtils.create(ApiService.class)
                .login(param)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<LoginBean>(progressDialog) {
                    @Override
                    protected void onSuccess(LoginBean login) {
                        Bundle bundle = new Bundle();
                        bundle.putString("大王", "大王叫我来巡山");
                        JumpUtils.jump(mContext, StatusDemoActivity.class, bundle);
                        finish();
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
//                    T.showLong("权限申请成功！");
                    break;
                case PermissionActivity.CALL_BACK_RESULE_CODE_FAILURE:
//                    T.showLong("权限申请失败！");
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
