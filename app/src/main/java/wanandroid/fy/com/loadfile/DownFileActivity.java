package wanandroid.fy.com.loadfile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.load.down.DownInfo;
import com.fy.baselibrary.retrofit.load.down.DownLoadListener;
import com.fy.baselibrary.retrofit.load.down.DownManager;
import com.fy.baselibrary.statusbar.MdStatusBar;

import butterknife.BindView;
import wanandroid.fy.com.R;

/**
 * 下载文件 演示 activity
 * Created by fangs on 2018/6/1.
 */
public class DownFileActivity extends AppCompatActivity implements IBaseActivity {

    @BindView(R.id.tvProgress1)
    TextView tvProgress1;

    @BindView(R.id.tvProgress2)
    TextView tvProgress2;

    @BindView(R.id.tvProgress3)
    TextView tvProgress3;

    @BindView(R.id.tvProgress4)
    TextView tvProgress4;

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

//        downLoad("http://pic48.nipic.com/file/20140912/7487939_224235377000_2.jpg");
        downLoad("http://imtt.dd.qq.com/16891/1861D39534D33194426C894BA0D816CF.apk?fsname=com.ss.android.ugc.aweme_1.8.3_183.aweme_1pk&csr=1bbd", tvProgress1);
        downLoad("https://pic.ibaotu.com/00/60/62/19S888piCNXP.mp4", tvProgress2);

        DownManager.getInstentce().runDownTask();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void reTry() {

    }

    private void downLoad(String url, TextView tv) {
        DownManager.getInstentce()
                .addDownTask(url, new DownLoadListener() {

                    @Override
                    public void onPuase() {
                        tv.setText(tv.getText() + "  暂停下载");
                    }

                    @Override
                    public void onCancel() {
                        tv.setText("取消下载");
                    }

                    @Override
                    public void onProgress(String percent) {
                        runOnUiThread(() -> tv.setText(percent + "%"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
