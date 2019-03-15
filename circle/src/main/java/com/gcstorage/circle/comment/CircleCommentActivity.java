package com.gcstorage.circle.comment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.retrofit.load.LoadCallBack;
import com.fy.baselibrary.retrofit.load.LoadService;
import com.fy.baselibrary.retrofit.load.up.UploadOnSubscribe;
import com.fy.baselibrary.utils.Constant;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.cache.SpfAgent;
import com.gcstorage.circle.R;
import com.gcstorage.circle.R2;
import com.gcstorage.circle.bean.LyCircleListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * DESCRIPTION： 评论界面
 * Created by fangs on 2019/3/15 11:59.
 */
public class CircleCommentActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener{

    public static final int PICTRUE_MAX_COUNT = 3; // 最大可选择图片数量，这里设置 3 张

    private String post_id;

    @BindView(R2.id.edit_comment_add_content)
    EditText editCommentAddContent;
    @BindView(R2.id.grid_comment_add_img)
    GridView gridCommentAddImg;
    private AddTrackAdapter mPicAdapter; // 显示图片GridView的适配器
    private List<String> mDataList; //图片本地地址集合  数据源


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
        menuLayout.setOnClickListener(this);
        return true;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {

        initData();
        initGridView();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.menuLayout) {//发布按钮
            upload();
        }
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


    private void initData(){
        LyCircleListBean article = (LyCircleListBean) getIntent().getExtras().get("LyCircleListBean");
//        if(TextUtils.isEmpty(commentListBean.getPublish_alarm())){
//            editCommentAddContent.setHint("写评论...");
//        }else {
//            editCommentAddContent.setHint("回复"+commentListBean.getPublish_name() + "...");
//        }
    }

    private void initGridView(){
        mDataList = new ArrayList<>(); // 创建图片数据源对象
        mPicAdapter = new AddTrackAdapter(this, mDataList, PICTRUE_MAX_COUNT); // 创建适配器对象
        gridCommentAddImg.setAdapter(mPicAdapter); // 为 GridView 设置适配器
        // 显示图片的GridView 设置条目点击监听
        gridCommentAddImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当前索引位置是“+”号图标时，才执行if里的代码
                if (position == mDataList.size()) {
//                    goToPhoto();
                } else {
                    // todo 查看选中的图片
//                    Intent intent = new Intent(context, ChatPicPagerActivity.class);
                    // 设置图片当前索引
//                    intent.putExtra("position", position);
//                    // 设置选中的图片地址集合
//                    intent.putStringArrayListExtra("pathlist", (ArrayList<String>) mDataList);
//                    // 启动 ChatPicPagerActivity
//                    startActivity(intent);
                }
            }
        });

        // 显示图片的GridView 设置条目长按监听
        gridCommentAddImg.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取震动服务对象
                Vibrator vibrator = (Vibrator) CircleCommentActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        ImagePickerHelper.resultMultiImagePicker(requestCode, resultCode, data, new ImagePickerHelper.MultiSelectedCallback() {
//            @Override
//            public void onImageSelected(ArrayList<ImageItem> selImageList, boolean isOrigin) {
//                if (selImageList != null) {//选择图片后上传
//                    List<String> resultDatas = new ArrayList<String>();
//                    Log.d("yy", "---------选择后图片-------------->>>>" + selImageList.size());
//                    for (ImageItem item : selImageList) {
//                        String path = item.path;
//                        resultDatas.add(path);
//
//                    }
//                    mDataList.addAll(resultDatas);
//                    mPicAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        if (resultCode != RESULT_OK)
//            return;
    }

    //跳转到 图片选择界面
    @NeedPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void goToPhoto() {
//        if (mDataList.size() == 0) {
//            ImagePickerHelper.showMultiImagePicker(CircleCommentActivity.this, null,PICTRUE_MAX_COUNT);
//        } else {
//            ImagePickerHelper.showMultiImagePicker(CircleCommentActivity.this, null, PICTRUE_MAX_COUNT - mDataList.size());
//        }
    }


    /**
     * 上传操作
     */
    @NeedPermission(value = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void upload() {
        List<String> files = new ArrayList<>();
        files.add(FileUtils.getSDCardPath() + "MagazineUnlock/1.jpg");
        files.add(FileUtils.getSDCardPath() + "MagazineUnlock/2.jpg");
        files.add(FileUtils.getSDCardPath() + "MagazineUnlock/3.jpg");
        uploadFiles(files);

//        if (mDataList.size() > 0 || !TextUtils.isEmpty(editCommentAddContent.getText().toString())) {
//            if (mDataList.size() > 0) {
//            } else {
//                uploadCommemnt();
//            }
//        } else {
//            T.showLong("必须有文字或者图片才可以上传哦！");
//        }
    }

    public void uploadFiles(List<String> files) {
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe();

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("UploadOnSubscribe", uploadOnSubscribe);
        params.put("filePathList", files);
        params.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        params.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));

        Observable.merge(Observable.create(uploadOnSubscribe), RequestUtils.create(LoadService.class).uploadFile(params))
                .compose(RxHelper.bindToLifecycle(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoadCallBack<Object>() {
                    @Override
                    protected void onProgress(String percent) {
                        editCommentAddContent.setText(percent + "%");
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
