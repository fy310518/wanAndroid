package com.fy.img.picker.preview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.fy.baselibrary.aop.annotation.StatusBar;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.utils.JumpUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.img.picker.ImagePicker;
import com.fy.img.picker.PickerConfig;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.bean.ImageItem;
import com.fy.img.picker.R;

import java.util.List;

/**
 * 图片预览activity
 * Created by fangs on 2017/7/6.
 */
public class PicturePreviewActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    protected FrameLayout pickerBottom;         //底部布局
    protected ConstraintLayout rlHead;          //头部布局
    protected TextView tvTitle;                 //显示当前图片的位置  例如  5/31
    protected TextView tvBack;
    protected TextView tvMenu;
    protected ConvenientBanner viewPager;
    protected CheckBox original_checkbox;
    protected TextView send;

    protected ImageFolder imgFolder;            //跳转进 PicturePreviewActivity 的图片文件夹
    protected List<ImageItem> selectedImages;   //所有已经选中的图片
    protected int mCurrentPosition = 0;         //跳转进PicturePreviewActivity时的序号，第几个图片; 当前显示的图片下标
    protected int max = 12;                     //最大选择图片数目

    /**
     * 当前屏幕状态 全屏or显示菜单
     */
    private int currentState = PickerConfig.STATE_SHOW_MENU;

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return R.layout.layout_preview;
    }

    @StatusBar(statusStrColor = "statusBar", navStrColor = "statusBar")
    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        rlHead = findViewById(R.id.rlHead);
        rlHead.setBackgroundColor(getResources().getColor(R.color.imgPreviewHeadBg));

        viewPager = findViewById(R.id.bannerViewPager);
        tvTitle = findViewById(R.id.tvTitle);
        tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(this);
        tvMenu = findViewById(R.id.tvMenu);

        original_checkbox = findViewById(R.id.original_checkbox);
        pickerBottom = findViewById(R.id.pickerBottom);
        pickerBottom.setOnClickListener(this);
        send = findViewById(R.id.send);
        send.setOnClickListener(this);
        original_checkbox.setOnClickListener(this);

        getTransmitData();
        //初始化当前页面的状态
        tvTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, mCurrentPosition + 1, imgFolder.images.size()));
        tvTitle.setTextColor(ResUtils.getColor(R.color.white));
        original_checkbox.setChecked(imgFolder.images.get(mCurrentPosition).isSelect);
        tvMenu.setVisibility(View.INVISIBLE);

        //本地图片例子 new LocalImageHolderView(), imgFolder.images
        viewPager.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new LocalImageHolderView(itemView, PicturePreviewActivity.this);
            }

            @Override
            public int getLayoutId() {
                return R.layout.viewpager_preview;
            }
        }, imgFolder.images)
        .setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                original_checkbox.setChecked(imgFolder.images.get(position).isSelect);
                tvTitle.setText(ResUtils.getReplaceStr(R.string.preview_image_count, position + 1, imgFolder.images.size()));
            }
        })
                .setFirstItemPos(mCurrentPosition);
//                .setPageTransformer(new AccordionTransformer())
    }

    /**
     * 获取传递的数据
     */
    private void getTransmitData() {
        Bundle bundle = getIntent().getExtras();
        mCurrentPosition = bundle.getInt(PickerConfig.KEY_CURRENT_POSITION, 0);
        max = bundle.getInt(PickerConfig.KEY_MAX_COUNT, -1);

        if (max > 0 && max == 1) {
            original_checkbox.setVisibility(View.GONE);
        } else if(max == -1){
            pickerBottom.setVisibility(View.GONE);
        }

        imgFolder = (ImageFolder) bundle.getSerializable(PickerConfig.KEY_IMG_FOLDER);

        ImageFolder folder = (ImageFolder) bundle.getSerializable(PickerConfig.KEY_ALREADY_SELECT);
        if (null != folder) selectedImages = folder.images;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.send) {//完成
            Bundle bundle = new Bundle();
            bundle.putSerializable("pickerImgData", imgFolder.images.get(mCurrentPosition));
            bundle.putSerializable(ImagePicker.imgFolderkey, new ImageFolder(selectedImages));
            JumpUtils.jumpResult(PicturePreviewActivity.this, bundle);
        } else if (i == R.id.original_checkbox) {
            if (original_checkbox.isChecked() && selectedImages.size() >= max) {
                T.showLong(ResUtils.getReplaceStr(R.string.select_limit, max));
                original_checkbox.setChecked(false);
            } else {
                ImageItem imgItem = imgFolder.images.get(mCurrentPosition);
                if (original_checkbox.isChecked()) {
                    imgItem.setSelect(true);//设置状态 属性为 选中
                    selectedImages.add(imgItem);
                } else {
                    selectedImages.remove(imgItem);
                    imgItem.setSelect(false);//设置状态 属性为 未选中
                }
            }
        } else if (i == R.id.tvBack){
            JumpUtils.exitActivity(this);
        }
    }

    /**
     * 隐藏 或显示 标题栏，底部栏
     */
    public void toggleStateChange() {
        if (currentState == PickerConfig.STATE_SHOW_MENU) {
            currentState = PickerConfig.STATE_FULLSCREEN;
            rlHead.setVisibility(View.GONE);
            if(max != -1)pickerBottom.setVisibility(View.GONE);
        } else {
            currentState = PickerConfig.STATE_SHOW_MENU;
            rlHead.setVisibility(View.VISIBLE);
            if(max != -1)pickerBottom.setVisibility(View.VISIBLE);
        }
    }
}
