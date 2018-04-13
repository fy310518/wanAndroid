package wanandroid.fy.com.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.widget.EasyPullLayout;
import com.fy.baselibrary.widget.TransformerView;

import butterknife.BindView;
import wanandroid.fy.com.R;

/**
 * 搜索
 * Created by fangs on 2018/4/13.
 */
public class SearchActivity extends AppCompatActivity implements IBaseActivity{

    @BindView(R.id.epl)
    EasyPullLayout epl;
    @BindView(R.id.topView)
    TransformerView topView;
    @BindView(R.id.rvBookmark)
    RecyclerView rvBookmark;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_search;
    }

    @Override
    public void setStatusBar(Activity activity) {

    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {}

    @Override
    public void reTry() {

    }
}
