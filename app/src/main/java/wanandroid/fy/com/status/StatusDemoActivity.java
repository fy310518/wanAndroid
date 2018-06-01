package wanandroid.fy.com.status;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.application.BaseActivityBean;
import com.fy.baselibrary.application.BaseApp;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.LoadFileUtils;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.load.UpLoadCallBack;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.statuslayout.StatusLayoutManager;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.L;
import com.fy.baselibrary.utils.NightModeUtils;
import com.fy.baselibrary.utils.cache.ACache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import wanandroid.fy.com.R;

/**
 * 多状态布局 demo
 * Created by fangs on 2018/3/16.
 */
public class StatusDemoActivity extends AppCompatActivity implements IBaseActivity {

    StatusLayoutManager slManager;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tvKing)
    TextView tvKing;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.activity_status;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.setColorBar(activity, R.color.statusBar, R.color.statusBar);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        BaseActivityBean activityBean = (BaseActivityBean) activity.getIntent()
                .getSerializableExtra("ActivityBean");
        slManager = activityBean.getSlManager();

//        uploadFiles();
//        downLoad("http://pic48.nipic.com/file/20140912/7487939_224235377000_2.jpg");
        downLoad("http://imtt.dd.qq.com/16891/1861D39534D33194426C894BA0D816CF.apk?fsname=com.ss.android.ugc.aweme_1.8.3_183.aweme_1pk&csr=1bbd");
//        downLoad("https://pic.ibaotu.com/00/60/62/19S888piCNXP.mp4");
    }


    @OnClick({R.id.tvKing})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKing:
                NightModeUtils.switchNightMode(this);
//                slManager.showNetWorkError();
//                Observable.timer(3000, TimeUnit.MILLISECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//                                slManager.showError();
//                            }
//                        });
                break;
        }
    }

    @Override
    public void reTry() {
        slManager.showEmptyData();

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Exception {
                        slManager.showNetWorkError();
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Object, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Object o) throws Exception {
                        slManager.showError();
                        return Observable.timer(3000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        slManager.showContent();
                    }
                });
    }

    private void uploadImg() {
        List<String> fileList = new ArrayList<>();
        fileList.add(FileUtils.getSDCardPath() + "DCIM/Camera/20121006174327607.jpg");
        fileList.add(FileUtils.getSDCardPath() + "DCIM/Camera/tooopen_sy_133481514678.jpg");

        RequestUtils.create(LoadService.class)
                .uploadFile1(LoadFileUtils.fileToMultipartBodyParts(fileList))
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });

    }

    public void uploadFiles() {
        List<File> files = new ArrayList<>();
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Camera/20121006174327607.jpg"));
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Downloads.zip"));
        files.add(new File(FileUtils.getSDCardPath() + "DCIM/Camera/tooopen_sy_133481514678.jpg"));

        LoadFileUtils.uploadFiles(files, RequestUtils.create(LoadService.class))
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new UpLoadCallBack() {
                    @Override
                    protected void onProgress(String percent) {
                        L.e("进度监听", percent + "%");

                    }

                    @Override
                    protected void onSuccess(Object t) {

                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

    private void downLoad(String url) {
//        LoadFileUtils.downLoadFile(url, new UpLoadCallBack() {
//            @Override
//            protected void onProgress(Integer percent) {
//                L.e("文件下载", "file download: " + percent);
//            }
//
//            @Override
//            protected void onSuccess(Object t) {
//
//            }
//
//            @Override
//            protected void updataLayout(int flag) {
//
//            }
//        });
        ACache mCache = ACache.get(BaseApp.getAppCtx());
        int percent = mCache.getAsInt(url + "percent");
        if (percent > 0)tvKing.setText(percent + "%");

        LoadFileUtils.downFile(url, new UpLoadCallBack(url) {
                    @Override
                    protected void onProgress(String percent) {
                        runOnUiThread(() -> tvKing.setText(percent + "%"));
                    }

                    @Override
                    protected void onSuccess(Object t) {
                    }

                    @Override
                    protected void updataLayout(int flag) {
                    }
                });
    }
}
