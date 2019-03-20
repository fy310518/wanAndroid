package com.gcstorage.circle.publish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.aop.resultfilter.ResultCallBack;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.fy.baselibrary.utils.notify.T;
import com.fy.baselibrary.widget.NoScrollGridview;
import com.fy.img.picker.ImagePicker;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.bean.ImageItem;
import com.fy.img.picker.multiselect.ImgPickerActivity;
import com.fy.img.picker.preview.PicturePreviewActivity;
import com.gcstorage.circle.R;
import com.gcstorage.circle.R2;
import com.gcstorage.circle.bean.LyLocationBean;
import com.gcstorage.circle.bean.UploadBean;
import com.gcstorage.circle.comment.AddTrackAdapter;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;
import com.gcstorage.circle.request.NetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * DESCRIPTION：发布帖子 activity
 * Created by fangs on 2019/3/20 16:32.
 */
public class CirclePublishActivity extends AppCompatActivity implements IBaseActivity, ResultCallBack {

    String mode;
    String address = "";
    public static final int PICTRUE_MAX_COUNT = 9; // 最大可选择图片数量，这里设置 9 张

    @BindView(R2.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R2.id.edit_comment_add_content)
    EditText editCommentAddContent;
    @BindView(R2.id.grid_comment_add_img)
    NoScrollGridview gridCommentAddImg;
    private AddTrackAdapter mPicAdapter; // 显示图片GridView的适配器
    private List<ImageItem> mDataList; //图片本地地址集合  数据源

    @BindView(R2.id.vsAddress)
    ViewStub vsAddress;
    Switch pushSwitch;

    @BindView(R2.id.vsIntegral)
    ViewStub vsIntegral;
    TextView txt_dynamic_add_reward;
    EditText et_money;

    @BindView(R2.id.vslook)
    ViewStub vslook;
    Switch dynamiPosttype;

    @Override
    public boolean isShowHeadView() {
        return true;
    }

    @Override
    public int setView() {
        return R.layout.circle_comment_activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_menu_publish, menu);
        View menuLayout = menu.findItem(R.id.menuSchedule).getActionView();
        menuLayout.setOnClickListener(v -> upload());//发布点击事件
        return true;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        initView();
        initGridView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int titleId = bundle.getInt("title");
        toolbarTitle.setText(titleId);

        if (titleId == R.string.newDynamic) {
            mode = "dynamic";
            //地理位置
            vsAddress.inflate();
            pushSwitch = findViewById(R.id.pushSwitch);
            pushSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        pushSwitch.setText(address);
                    } else {
                        pushSwitch.setText(R.string.noAddressInfo);
                    }
                }
            });
            getAddress();

        } else if (titleId == R.string.bulletin) {
            mode = "xiechatongbao";
            //积分
            vsIntegral.inflate();
            txt_dynamic_add_reward = findViewById(R.id.txt_dynamic_add_reward);
            et_money = findViewById(R.id.et_money);

        } else if (titleId == R.string.CaseSharing) {
            mode = "caseshare";
        } else if (titleId == R.string.MicroClassroom) {
            mode = "microclassroom";
        }

        initLook();
    }

    //哪些人可以查看 开关
    private void initLook(){
        //哪些人可以查看
        vslook.inflate();
        dynamiPosttype = findViewById(R.id.tbtn_dynamic_posttype);
//        dynamiPosttype.isChecked();
        dynamiPosttype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dynamiPosttype.setText(R.string.open);
                } else {
                    dynamiPosttype.setText(R.string.privacy);
                }
            }
        });
    }


    private void initGridView() {
        mDataList = new ArrayList<>(); // 创建图片数据源对象
        mPicAdapter = new AddTrackAdapter(this, mDataList, PICTRUE_MAX_COUNT); // 创建适配器对象
        gridCommentAddImg.setAdapter(mPicAdapter); // 为 GridView 设置适配器
        // 显示图片的GridView 设置条目点击监听
        gridCommentAddImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当前索引位置是“+”号图标时，才执行if里的代码
                if (position == mDataList.size()) {
                    goToPhoto();
                } else {
                    //查看选中的图片
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, new ImageFolder(mDataList));
                    bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, new ImageFolder(mDataList));
                    JumpUtils.jump(CirclePublishActivity.this, PicturePreviewActivity.class, bundle);
                }
            }
        });

        // 显示图片的GridView 设置条目长按监听
        gridCommentAddImg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取震动服务对象
                Vibrator vibrator = (Vibrator) CirclePublishActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                // 只震动 150 ms，一次
                vibrator.vibrate(150);
                // 把所有的删除图标设置为显示状态
                mPicAdapter.mIsShowDelIcon = true;
                // 刷新界面
                mPicAdapter.notifyDataSetChanged();
                // 拦截事件
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPicAdapter.mIsShowDelIcon) { // 当删除图片为显示状态时
            // 把显示图片的删除图标改为隐藏状态
            mPicAdapter.mIsShowDelIcon = false;
            // 刷新显示图片GridView的界面
            mPicAdapter.notifyDataSetChanged();
            // 只要不 return super.onKeyDown(keyCode, event) 就不会退出这个Activity
            // return true 是拦截这个事件，这样的话其他监听者就接受不到这个事件了
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.Picture_Selection) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                ImageFolder imageFolder = (ImageFolder) bundle.getSerializable(ImagePicker.imgFolderkey);
                assert imageFolder != null;

                mDataList.clear();
                mDataList.addAll(imageFolder.images);
                mPicAdapter.notifyDataSetChanged();
            } else {
                T.showLong("没有数据");
            }
        }
    }

    //跳转到 图片选择界面
    @NeedPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void goToPhoto() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(PickerConfig.KEY_ISTAKE_picture, true);
        bundle.putInt(PickerConfig.KEY_MAX_COUNT, PICTRUE_MAX_COUNT);
        bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, new ImageFolder(mDataList));
        JumpUtils.jump(this, ImgPickerActivity.class, bundle, ImagePicker.Picture_Selection, this);
    }


    /**
     * 上传操作
     */
    @NeedPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void upload() {
        if (mDataList.size() > 0 || !TextUtils.isEmpty(editCommentAddContent.getText().toString())) {
            if (null != et_money && TextUtils.isEmpty(et_money.getText().toString())){
                T.showLong("必须填写悬赏积分才可以上传哦！");
                return;
            }

            if (mDataList.size() > 0) {
                List<String> files = new ArrayList<>();
                for (ImageItem imageItem : mDataList) {
                    files.add(imageItem.getPath());
                }
                uploadFiles(files);
            } else {
                commentNew();
            }
        } else {
            T.showLong("必须有文字或者图片才可以上传哦！");
        }
    }

    public void uploadFiles(List<String> files) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("uploadFile", "fileName");
        params.put("filePathList", files);
        params.put("UploadOnSubscribe", new UploadOnSubscribe());

        RequestUtils.create(ApiService.class)
                .uploadFile(params)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .flatMap(new Function<List<UploadBean>, Observable<Object>>() {
                    @SuppressLint("NewApi")
                    @Override
                    public Observable<Object> apply(List<UploadBean> uploadBeans) throws Exception {
                        return RequestUtils.create(ApiService.class)
                                .publishpost(getParames(uploadBeans))
                                .compose(RxHelper.handleResult());
                    }
                }).subscribe(getObserver());
    }

    //帖子 评论 请求（不含图片）
    private void commentNew() {
        RequestUtils.create(ApiService.class)
                .publishpost(getParames(new ArrayList<>()))
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(getObserver());
    }

    //定义 网络请求 观察者
    private NetCallBack<Object> getObserver() {
        return new NetCallBack<Object>(new NetDialog().init(this).setDialogMsg(R.string.publishLoading)) {
            @Override
            protected void onSuccess(Object commentBeans) {
                T.showLong(R.string.publishSuccess);
                JumpUtils.exitActivity(CirclePublishActivity.this);
            }
        };
    }

    //定义 帖子评论 请求参数
    private ArrayMap<String, Object> getParames(List<UploadBean> uploadBeans) {
        ArrayMap<String, Object> commentNewparams = new ArrayMap<>();
        commentNewparams.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        commentNewparams.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));
        commentNewparams.put("action", "publishpost");
        commentNewparams.put("mode", mode);

        StringBuilder sb = new StringBuilder();
        for (UploadBean bean : uploadBeans) {
            sb.append(bean.getUrl()).append(",");
        }
        commentNewparams.put("picture", sb.toString().substring(0, sb.length()));
        commentNewparams.put("content", editCommentAddContent.getText().toString().trim());

        if (null != et_money)commentNewparams.put("rewardtask", et_money.getText().toString().trim());//发布朋友圈 协查通报时奖励的任务金额
        if (null != pushSwitch)commentNewparams.put("position", pushSwitch.getText().toString().trim());//发布动态的位置
        if (null != dynamiPosttype)commentNewparams.put("ispravite", dynamiPosttype.isChecked() ? "0" : "1");//是否为私密评论，0：不是，1是

        return commentNewparams;
    }

    //获取地理位置
    @SuppressLint("MissingPermission")
    @NeedPermission(value = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    private void getAddress(){
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (!TextUtils.isEmpty(address))return;

                ArrayMap<String, String> options = new ArrayMap<>();
                options.put("output", "json");
                options.put("ak", "esNPFDwwsXWtsQfw4NMNmur1");
                options.put("location", location.getLatitude() + "," + location.getLongitude());

                RequestUtils.create(ApiService.class)
                        .getLocation(options)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxHelper.bindToLifecycle(CirclePublishActivity.this))
                        .subscribe(new NetCallBack<LyLocationBean>() {
                            @Override
                            protected void onSuccess(LyLocationBean locationBean) {
                                address = locationBean.getResult().getFormatted_address();
                                pushSwitch.setText(address);
                            }
                        });
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

}
