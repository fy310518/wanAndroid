package hodgepodge.fy.com.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.startactivity.StartActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.ConstantUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.SpfUtils;
import com.fy.baselibrary.utils.T;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import hodgepodge.fy.com.R;
import hodgepodge.fy.com.login.LoginActivity;
import hodgepodge.fy.com.main.fragment.FragmentFive;
import hodgepodge.fy.com.main.fragment.FragmentFour;
import hodgepodge.fy.com.main.fragment.FragmentOne;
import hodgepodge.fy.com.main.fragment.FragmentThree;
import hodgepodge.fy.com.main.fragment.FragmentTwo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 体育联盟 主界面
 * Created by fangs on 2017/12/12.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity {

    private AppCompatActivity mContext;

    private static final String[] FRAGMENT_TAG = {"FragmentOne", "FragmentTwo", "FragmentThree", "FragmentFour", "FragmentFive"};
    private FragmentManager fragmentManageer;
    private Fragment mCurrentFrgment;//当前显示的fragment
    private int currentIndex = 0;    //当前显示的fragment的下标

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    private FragmentFour fragmentFour;
    private FragmentFive fragmentFive;

    @BindView(R.id.radioGlayout)
    RadioGroup radioGlayout;
    @BindView(R.id.rBtnOne)
    RadioButton rBtnOne;
    @BindView(R.id.rBtnFour)
    RadioButton rBtnFour;

    @BindView(R.id.loadImg)
    ImageView loadImg;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_main;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;

        //显示欢迎页，并设置点击事件（但是不设置点击回调）
        loadImg.setVisibility(View.VISIBLE);

        ConstantUtils.token = SpfUtils.getSpfSaveStr("token");
        ConstantUtils.studentID = SpfUtils.getSpfSaveInt("studentId");
        initRadioGroup();
        initSLManager();
        hideLoadView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //从登录界面 进入 首页导航按钮选中
        boolean flag = SpfUtils.getSpfSaveBoolean("loginShowHome");
        if (flag) {
            rBtnOne.setChecked(true);
            SpfUtils.saveBooleanToSpf("loginShowHome", false);
        }
    }

    /**
     * 解决 activity 启动模式为 singleTask时，intent传值 接收不到问题
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        //获取 Intent而不是用 方法传递的Intent，是因为在 Application 中 设置了activity 生命周期回调时候 用到Intent 缓存了配置信息
        Intent intent1 = getIntent();
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            intent1 = intent1.putExtras(bundle);
            super.onNewIntent(intent1);
            setIntent(intent1);//intent传值 接收不到问题，关键在这句
        } else {
            super.onNewIntent(intent);
        }
    }

    /**
     * activity fragment 横竖屏切换监听回调方法
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
        } else {
            //竖屏
        }
    }

    //初始化底部导航按钮
    private void initRadioGroup() {
        radioGlayout.setOnCheckedChangeListener((group, checkedId) -> {
            int position = 0;
            switch (checkedId) {
                case R.id.rBtnOne:
                    position = 0;
                    break;
                case R.id.rBtnTwo:
                    position = 1;
                    break;
                case R.id.rBtnThree:
                    position = 2;
                    break;
                case R.id.rBtnFour:
                    position = 3;
                    break;
                case R.id.rBtnFive:
                    position = 4;
                    break;
            }
            beginTransaction(position);
        });

        rBtnOne.setChecked(true);
    }

    private void beginTransaction(int position) {
        if (null == fragmentManageer) {
            fragmentManageer = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManageer.beginTransaction();
        setFragmentTransition(fragmentTransaction, position);

        Fragment showfragment = null;
        switch (position) {
            case 0:
                if (null == fragmentOne) fragmentOne = new FragmentOne();
                showfragment = fragmentOne;
                break;
            case 1:
                if (null == fragmentTwo) fragmentTwo = new FragmentTwo();
                showfragment = fragmentTwo;
                break;
            case 2:
                if (null == fragmentThree) fragmentThree = new FragmentThree();
                showfragment = fragmentThree;
                break;
            case 3:
                if (null == fragmentFour) fragmentFour = new FragmentFour();
                showfragment = fragmentFour;
                break;
            case 4:
                if (null == fragmentFive) fragmentFive = new FragmentFive();
                showfragment = fragmentFive;
                break;

        }

        //判断当前的Fragment是否为空，不为空则隐藏
        if (null != mCurrentFrgment) {
            fragmentTransaction.hide(mCurrentFrgment);
        }

        if (null == showfragment) return;
        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!showfragment.isAdded()) {
            fragmentTransaction.add(R.id.main_fragemnet_container, showfragment, FRAGMENT_TAG[position]);
        } else {
            fragmentTransaction.show(showfragment);
        }

        //保存当前显示的那个Fragment
        mCurrentFrgment = showfragment;
        currentIndex = position;
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 设置fragment 转场动画
     *
     * @param fragmentTransaction
     * @param position            将要显示的fragment的下标
     */
    private void setFragmentTransition(FragmentTransaction fragmentTransaction, int position) {
        //设置自定义过场动画
        if (currentIndex > position) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.anim_slide_right_in,
                    R.anim.anim_slide_right_out);
        } else {
            fragmentTransaction.setCustomAnimations(
                    R.anim.anim_slide_left_in,
                    R.anim.anim_slide_left_out);
        }
    }

    //延迟2秒 进入登录界面
    private void initSLManager() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    //验证token 为空 则进入登录界面
                    if (TextUtils.isEmpty(ConstantUtils.token)) {
                        JumpUtils.jump(mContext, LoginActivity.class, null);
                    }
                });
    }

    //延迟200 毫秒 隐藏 加载图片控件
    private void hideLoadView() {
        Observable.timer(2500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (null != loadImg) {
                        loadImg.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.loadImg})
    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {}

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); 	不要调用父类的方法
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
