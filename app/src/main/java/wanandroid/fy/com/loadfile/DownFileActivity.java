package wanandroid.fy.com.loadfile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.load.down.DownInfo;
import com.fy.baselibrary.retrofit.load.down.DownLoadListener;
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

        initRv();

        for (int i = 0; i < rvAdapter.getItemCount(); i++){
            DownManager.getInstentce()
                    .addDownTask(rvAdapter.getmDatas().get(i), new DownLoadCall(i));
        }

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
                .setmSpace(R.dimen.rv_divider_height).create(this));
        rvAdapter = new DownFileListAdapter(this, DataSource.getInstance().getData());
        rvAdapter.setChangeItemListener((position) -> rvAdapter.notifyItemChanged(position, ""));
        rvDownList.setAdapter(rvAdapter);
    }


    class DownLoadCall implements DownLoadListener {
        private int position;

        public DownLoadCall(int position) {
            this.position = position;
        }

        @Override
        public void onPuase() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onProgress(long finished, long total, double progress) {
            DownInfo downInfo = rvAdapter.getmDatas().get(position);
            downInfo.setStateInte(DownInfo.STATUS_DOWNLOADING);
            downInfo.setPercent(progress);
            downInfo.setCountLength(total);
            downInfo.getReadLength().set(finished);
            runOnUiThread(() -> rvAdapter.changeItemListener.onChange(position));
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }
}
