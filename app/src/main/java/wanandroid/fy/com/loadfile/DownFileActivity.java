package wanandroid.fy.com.loadfile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.load.down.DownManager;
import com.fy.baselibrary.rv.divider.ListItemDecoration;
import com.fy.baselibrary.statusbar.MdStatusBar;

import butterknife.BindView;
import wanandroid.fy.com.R;

/**
 * 下载文件 演示 activity
 * Created by fangs on 2018/6/1.
 */
public class DownFileActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.rvDownList)
    RecyclerView rvDownList;
    DownFileListAdapter rvAdapter;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.activity_file_down;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        DownManager.getInstentce().setLoadCall(downInfo -> runOnUiThread(() -> {
            for (int i = 0; i < rvAdapter.getItemCount(); i++) {
                if (downInfo.getUrl().equals(rvAdapter.getmDatas().get(i).getUrl())) {
                    rvAdapter.changeItemListener.onChange(i);
                    break;
                }
            }
        }));

        downLoad("http://pic48.nipic.com/file/20140912/7487939_224235377000_2.jpg");
        downLoad("http://imtt.dd.qq.com/16891/1861D39534D33194426C894BA0D816CF.apk?fsname=com.ss.android.ugc.aweme_1.8.3_183.aweme_1pk&csr=1bbd");
        downLoad("https://pic.ibaotu.com/00/60/62/19S888piCNXP.mp4");

        initRv();
        DownManager.getInstentce().runDownTask();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reTry() {

    }

    private void initRv(){
        rvDownList.setLayoutManager(new LinearLayoutManager(this));
        rvDownList.addItemDecoration(new ListItemDecoration.Builder()
                .setmSpace(R.dimen.rv_divider_height)
                .setDraw(false).create(this));
        rvAdapter = new DownFileListAdapter(this, DownManager.getInstentce().getDownTask());
        rvAdapter.setChangeItemListener((position) -> rvAdapter.notifyItemChanged(position, ""));
        rvDownList.setAdapter(rvAdapter);
    }

    private void downLoad(String url) {
        DownManager.getInstentce()
                .addDownTask(url, null);


    }
}
