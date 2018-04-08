package wanandroid.fy.com.about;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.AppUtils;

import butterknife.BindView;
import wanandroid.fy.com.R;

/**
 * 关于
 * Created by fangs on 2018/4/8.
 */
public class AboutActivity extends AppCompatActivity implements IBaseActivity{

    @BindView(R.id.tvAppInformation)
    TextView tvAppInformation;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_about;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        tvAppInformation.setText(AppUtils.getAppName() + "\n" + AppUtils.getVersionName());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reTry() {

    }
}
