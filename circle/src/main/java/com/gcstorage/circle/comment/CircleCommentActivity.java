package com.gcstorage.circle.comment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

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
import com.fy.img.picker.ImagePicker;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.bean.ImageItem;
import com.fy.img.picker.multiselect.ImgPickerActivity;
import com.fy.img.picker.preview.PicturePreviewActivity;
import com.gcstorage.circle.R;
import com.gcstorage.circle.R2;
import com.gcstorage.circle.bean.CommentListBean;
import com.gcstorage.circle.bean.LyCircleListBean;
import com.gcstorage.circle.bean.UploadBean;
import com.gcstorage.circle.request.ApiService;
import com.gcstorage.circle.request.NetCallBack;
import com.gcstorage.circle.request.NetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * DESCRIPTION： 评论界面
 * Created by fangs on 2019/3/15 11:59.
 */
public class CircleCommentActivity extends AppCompatActivity implements IBaseActivity, ResultCallBack, View.OnClickListener{

    public static final int PICTRUE_MAX_COUNT = 3; // 最大可选择图片数量，这里设置 3 张

    private String post_id;

    @BindView(R2.id.edit_comment_add_content)
    EditText editCommentAddContent;
    @BindView(R2.id.grid_comment_add_img)
    GridView gridCommentAddImg;
    private AddTrackAdapter mPicAdapter; // 显示图片GridView的适配器
    private List<ImageItem> mDataList; //图片本地地址集合  数据源


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
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        post_id = bundle.getString("postId", "");
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
                    goToPhoto();
                } else {
                    //查看选中的图片
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PickerConfig.KEY_IMG_FOLDER, new ImageFolder(mDataList));
                    bundle.putSerializable(PickerConfig.KEY_ALREADY_SELECT, new ImageFolder(mDataList));
                    JumpUtils.jump(CircleCommentActivity.this, PicturePreviewActivity.class, bundle);
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
    public void onActResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.Picture_Selection){
            if (null != data) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                ImageFolder imageFolder = (ImageFolder) bundle.getSerializable(ImagePicker.imgFolderkey);
                assert imageFolder != null;

                mDataList.clear();
                mDataList.addAll(imageFolder.images);
                mPicAdapter.notifyDataSetChanged();
            }else {
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
            if (mDataList.size() > 0) {
                List<String> files = new ArrayList<>();
                for (ImageItem imageItem : mDataList){
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


    //帖子 评论 请求（含图片）
    @SuppressLint("CheckResult")
    public void uploadFiles(List<String> files) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("uploadFile", "fileName");
        params.put("filePathList", files);
        params.put("UploadOnSubscribe", new UploadOnSubscribe());

        RequestUtils.create(ApiService.class)
                .uploadFile(params)
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .flatMap(new Function<List<UploadBean>, Observable<ArrayList<CommentListBean>>>() {
                    @SuppressLint("NewApi")
                    @Override
                    public Observable<ArrayList<CommentListBean>> apply(List<UploadBean> uploadBeans) throws Exception {
                        return RequestUtils.create(ApiService.class)
                                .commentNew(getParames(uploadBeans))
                                .compose(RxHelper.handleResult());
                    }
                }).subscribe(getObserver());
    }

    //帖子 评论 请求（不含图片）
    private void commentNew(){
        RequestUtils.create(ApiService.class)
                .commentNew(getParames(new ArrayList<>()))
                .compose(RxHelper.handleResult())
                .compose(RxHelper.bindToLifecycle(this))
                .subscribe(getObserver());
    }

    //定义 网络请求 观察者
    private NetCallBack<ArrayList<CommentListBean>> getObserver(){
        return new NetCallBack<ArrayList<CommentListBean>>(new NetDialog().init(this).setDialogMsg(R.string.publishLoading)) {
            @Override
            protected void onSuccess(ArrayList<CommentListBean> commentBeans) {
                T.showLong(R.string.publishSuccess);

                Bundle bundle = new Bundle();
                bundle.putSerializable("CommentListBeans", commentBeans);
                JumpUtils.jumpResult(CircleCommentActivity.this, bundle);
            }
        };
    }

    //定义 帖子评论 请求参数
    private ArrayMap<String, Object> getParames(List<UploadBean> uploadBeans){
        ArrayMap<String, Object> commentNewparams = new ArrayMap<>();
        commentNewparams.put("alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        commentNewparams.put("token", SpfAgent.getString(Constant.baseSpf, Constant.token));
        commentNewparams.put("action", "setcomment");
        commentNewparams.put("postid", post_id);
        //发布人的唯一ID
        commentNewparams.put("publish_alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        //回复的评论的ID（评论不传，回复必传）
        commentNewparams.put("reply_id", SpfAgent.getString(Constant.baseSpf, Constant.userName));
        //回复的评论的用户唯一ID（评论不传，回复必传）
        commentNewparams.put("reply_alarm", SpfAgent.getString(Constant.baseSpf, Constant.userName));

        StringBuilder sb = new StringBuilder();
        for (UploadBean bean : uploadBeans) {
            sb.append(bean.getUrl()).append(",");
        }
        commentNewparams.put("picture", sb.toString().substring(0, sb.length()));
        commentNewparams.put("is_visible", "0");//是否为私密评论，0：不是，1是
        commentNewparams.put("content", editCommentAddContent.getText().toString().trim());

        return commentNewparams;
    }
}
