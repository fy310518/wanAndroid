package wanandroid.fy.com.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.AnimUtils;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.TintUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import wanandroid.fy.com.R;
import wanandroid.fy.com.about.AboutActivity;
import wanandroid.fy.com.main.fragment.FragmentOne;
import wanandroid.fy.com.main.fragment.FragmentThree;
import wanandroid.fy.com.main.fragment.FragmentTwo;
import wanandroid.fy.com.utils.SelectUtils;

/**
 * 体育联盟 主界面
 * Created by fangs on 2017/12/12.
 */
public class MainActivity extends AppCompatActivity implements IBaseActivity {

    private AppCompatActivity mContext;

    private static final String[] FRAGMENT_TAG = {"FragmentOne", "FragmentTwo", "FragmentThree"};
    private FragmentManager fragmentManageer;
    private Fragment mCurrentFrgment;//当前显示的fragment
    private int currentIndex = 0;    //当前显示的fragment的下标

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;

    @BindView(R.id.radioGlayout)
    RadioGroup radioGlayout;
    @BindView(R.id.rBtnOne)
    RadioButton rBtnOne;
    @BindView(R.id.rBtnTwo)
    RadioButton rBtnTwo;
    @BindView(R.id.rBtnThree)
    RadioButton rBtnThree;

    @BindView(R.id.dlMain)
    DrawerLayout dlMain;
    @BindView(R.id.navView)
    NavigationView navView;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.main_activity;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mContext = this;

        initNav();
        initRadioGroup();
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
        //设置 选择器
        TintUtils.setTxtIconLocal(rBtnOne, SelectUtils.getSelector(R.drawable.svg_home_page), 1);
        TintUtils.setTxtIconLocal(rBtnTwo, SelectUtils.getSelector(R.drawable.svg_knowledge_system), 1);
        TintUtils.setTxtIconLocal(rBtnThree, SelectUtils.getSelector(R.drawable.svg_bookmark), 1);

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
        AnimUtils.setFragmentTransition(fragmentTransaction, currentIndex, position);

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

    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {}

    @Override
    public void onBackPressed() {
//        如果DrawerLayout是打开状态则关闭
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed(); 	不要调用父类的方法
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

//    初始化导航视图
    private void initNav(){
        navView.setNavigationItemSelectedListener(item -> {
            dlMain.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.myLike:

                    break;
                case R.id.atNightModel:
//                    NightModeUtils.switchNightMode(this);//todo
                    break;
                case R.id.about:
                    JumpUtils.jump(mContext, AboutActivity.class, null);
                    break;
            }
            return false;
        });
    }
}
