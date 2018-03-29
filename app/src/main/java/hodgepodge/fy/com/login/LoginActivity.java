package hodgepodge.fy.com.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.permission.PermissionActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.RxNetCache;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.ConstantUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import hodgepodge.fy.com.R;
import hodgepodge.fy.com.api.ApiService;
import hodgepodge.fy.com.entity.HomeBean;
import hodgepodge.fy.com.entity.LoginBean;
import hodgepodge.fy.com.entity.NewsBean;
import hodgepodge.fy.com.main.MainActivity;
import hodgepodge.fy.com.status.StatusDemoActivity;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 登录
 * Created by fangs on 2017/12/12.
 */
public class LoginActivity extends AppCompatActivity implements IBaseActivity {
    private AppCompatActivity mContext;


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
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;

        Bundle bundle = new Bundle();
        bundle.putStringArray(PermissionActivity.KEY_PERMISSIONS_ARRAY,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});

        JumpUtils.jump(this, PermissionActivity.class, bundle, PermissionActivity.CALL_BACK_PERMISSION_REQUEST_CODE);
    }


    @OnClick({R.id.tvLogin})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                Bundle bundle = new Bundle();
                bundle.putString("大王", "大王叫我来巡山");
                JumpUtils.jump(mContext, StatusDemoActivity.class, bundle);
//                login();
//                getNews();
                break;
        }
    }

    @Override
    public void reTry() {}

    private void login(){
        IProgressDialog progressDialog = new IProgressDialog().init(mContext)
                .setDialogMsg(R.string.user_login);

//        String mUserName = editUser.getText().toString().trim();
//        String mPassWord = editPass.getText().toString().trim();

        Map<String, Object> param = new HashMap<>();
        param.put("username", "13191614");
        param.put("password", "123456");

        RequestUtils.create(ApiService.class)
                .loginToApp(param)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(disposable -> RequestUtils.addDispos(disposable))
                .flatMap(new Function<LoginBean, ObservableSource<HomeBean>>() {
                    @Override
                    public ObservableSource<HomeBean> apply(LoginBean loginBean) throws Exception {

                        Bundle bundle = new Bundle();
                        bundle.putString("大王", "大王叫我来巡山");
                        JumpUtils.jump(mContext, MainActivity.class, bundle);

                        //获取最新的token
                        ConstantUtils.token = loginBean.getToken();

                        if (null != loginBean.getStudent()) {
                            //缓存学生信息 和 单独换粗学生id , 单独学生头像
                            ConstantUtils.studentID = loginBean.getStudent().getStudentid();
                            ConstantUtils.head_portrait = loginBean.getStudent().getTouxiangurl();
                            SpfUtils.saveIntToSpf("studentId", ConstantUtils.studentID);
                            SpfUtils.saveStrToSpf("touxiangurl", ConstantUtils.head_portrait);
                        }

                        Map<String, Object> homeParam = new HashMap<>();
                        homeParam.put("token", ConstantUtils.token);
                        homeParam.put("studentid", ConstantUtils.studentID);
                        return RequestUtils
                                .create(ApiService.class)
                                .getHome(homeParam)
                                .compose(RxHelper.handleResult())
                                .doOnSubscribe(disposable -> RequestUtils.addDispos(disposable));
                    }
                })
                .subscribe(new NetCallBack<HomeBean>(progressDialog) {
                    @Override
                    protected void onSuccess(HomeBean t) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }

    private void loginTwo(){
        IProgressDialog progressDialog = new IProgressDialog().init(mContext)
                .setDialogMsg(R.string.user_login);

        Map<String, Object> param = new HashMap<>();
        param.put("username", "");
        param.put("password", "");

        new RxNetCache.Builder().setApi("loginToApp").create()
                .request(RequestUtils
                        .create(ApiService.class)
                        .loginToApp(param)
                        .compose(RxHelper.handleResult()))
                .flatMap(new Function<LoginBean, ObservableSource<HomeBean>>() {
                    @Override
                    public ObservableSource<HomeBean> apply(LoginBean loginBean) throws Exception {
                        //获取最新的token
                        ConstantUtils.token = loginBean.getToken();

                        if (null != loginBean.getStudent()) {
                            //缓存学生信息 和 单独换粗学生id , 单独学生头像
                            ConstantUtils.studentID = loginBean.getStudent().getStudentid();
                            ConstantUtils.head_portrait = loginBean.getStudent().getTouxiangurl();
                            SpfUtils.saveIntToSpf("studentId", ConstantUtils.studentID);
                            SpfUtils.saveStrToSpf("touxiangurl", ConstantUtils.head_portrait);
                        }

                        Map<String, Object> homeParam = new HashMap<>();
                        homeParam.put("token", ConstantUtils.token);
//                        homeParam.put("studentid", ConstantUtils.studentID);
                        homeParam.put("studentid", "1");
                        return RequestUtils.create(ApiService.class)
                                .getHome(homeParam)
                                .compose(RxHelper.handleResult());
                    }
                })
                .subscribe(new NetCallBack<HomeBean>(progressDialog) {
                    @Override
                    protected void onSuccess(HomeBean t) {
                        Bundle bundle = new Bundle();
                        bundle.putString("大王", "大王叫我来巡山");
                        JumpUtils.jump(mContext, MainActivity.class, bundle);
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

    private void getNews(){
        IProgressDialog progressDialog = new IProgressDialog().init(mContext)
                .setDialogMsg(R.string.user_login);

        RequestUtils.create(ApiService.class)
                .getNews()
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<List<NewsBean>>(progressDialog) {
                    @Override
                    protected void onSuccess(List<NewsBean> t) {
                        L.e(t.toString());
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
