package com.fy.img.picker.multiselect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.MultiCommonAdapter;
import com.fy.baselibrary.rv.adapter.MultiTypeSupport;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.PhotoUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.baselibrary.utils.notify.T;
import com.fy.img.picker.ImagePicker;
import com.fy.img.picker.bean.ImageFolder;
import com.fy.img.picker.bean.ImageItem;
import com.fy.library.imgpicker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择（单选、多选） RecyclerView 适配器
 * Created by fangs on 2017/8/7.
 */
public class ImgPickersAdapter extends MultiCommonAdapter<ImageItem> {

    private int mImageSize;
    protected List<ImageItem> selectedImages;   //所有已经选中的图片
    private OnImageItemClickListener listener;   //图片被点击的监听
    private OnCheckClickListener clickListener;  //checkBox点击监听
    private ImagePicker imagePicker;

    private ImgPickersAdapter(Context context, List<ImageItem> datas) {
        super(context, datas, new MultiTypeSupport<ImageItem>(){
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == 1) {
                    return R.layout.adapter_image;
                } else {
                    return R.layout.adapter_camera;
                }
            }

            @Override
            public int getItemViewType(int position, ImageItem imageItem) {
                return imageItem.isShowCamera();
            }
        });

        mImageSize = ScreenUtils.getImageItemWidth();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void convert(ViewHolder holder, ImageItem imgItem, int position) {
        if (position == 0 && imgItem.isShowCamera() == 0){//判断 是否显示 拍照按钮
            holder.setOnClickListener(R.id.camera, v -> {
                String takeImgFilePath = FileUtils.getPath("/DCIM/camera/", 1);
                File newFile = FileUtils.createFile(takeImgFilePath, "IMG_", ".png");
                //拍照
                PhotoUtils.takePicture((Activity) mContext, newFile);
            });
        } else {
            ImageView ivThumb = holder.getView(R.id.iv_thumb);
            ivThumb.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            ImgLoadUtils.loadImage(imgItem.path, ivThumb);
            ivThumb.setOnClickListener(v -> {
                if (listener != null) listener.onImageItemClick(imgItem, position);
            });

            CheckBox cbCheck = holder.getView(R.id.cb_check);
//            if (imagePicker.getSelectLimit() == 1){
//                cbCheck.setVisibility(View.GONE);
//            } else {
            if (imgItem.isSelect) {
                cbCheck.setChecked(true);
            } else {
                cbCheck.setChecked(false);
            }

            cbCheck.setOnClickListener(v -> {
                int selectLimit = imagePicker.getSelectLimit();
                if (cbCheck.isChecked() && selectedImages.size() >= selectLimit) {
                    T.showLong(ResUtils.getReplaceStr(R.string.select_limit, selectLimit));
                    cbCheck.setChecked(false);
                } else {
                    if (cbCheck.isChecked()) {
                        if (!selectedImages.contains(imgItem)) selectedImages.add(imgItem);
                        imgItem.setSelect(true);//设置状态 属性为 选中
                    } else {
                        selectedImages.remove(imgItem);
                        imgItem.setSelect(false);//设置状态 属性为 未选中
                    }
                }

                if (null != clickListener) clickListener.onClick(selectedImages.size());
            });
//            }
        }
    }


    public void refreshData(List<ImageItem> images) {
        if (null == images || images.size() == 0){
            setmDatas(new ArrayList<>());
        } else {
            setmDatas(images);
        }

        notifyDataSetChanged();
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public void setClickListener(OnCheckClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setImagePicker(ImagePicker imagePicker) {
        this.imagePicker = imagePicker;
    }

    public List<ImageItem> getSelectedImages() {
        return selectedImages;
    }

    public void setSelectedImages(List<ImageItem> selectedImages) {
        this.selectedImages = selectedImages;
    }

    /**
     * 定义item 图片点击事件回调接口
     */
    public interface OnImageItemClickListener {
        void onImageItemClick(ImageItem imageItem, int position);
    }

    /**
     * 定义 item checkBox点击事件回调接口
     */
    interface OnCheckClickListener{
        void onClick(int num);
    }


    static class Bulder{
        public ImagePicker parames;
        ImageFolder imageFolder;

        OnImageItemClickListener listener;
        OnCheckClickListener clickListener;

        public Bulder() {
            this.parames = new ImagePicker();
        }

        public Bulder setSelectLimit(int selectLimit) {
            this.parames.setSelectLimit(selectLimit);
            return this;
        }

        public Bulder setImageFolder(ImageFolder imageFolder) {
            this.imageFolder = imageFolder;
            return this;
        }

        public Bulder setOnImageItemClickListener(OnImageItemClickListener listener){
            this.listener = listener;
            return this;
        }

        public Bulder setClickListener(OnCheckClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public ImgPickersAdapter create(Context context, List<ImageItem> datas){
            ImgPickersAdapter adapter = new ImgPickersAdapter(context, datas);
            adapter.setImagePicker(parames);
            adapter.setSelectedImages(imageFolder.images);
            adapter.setOnImageItemClickListener(listener);
            adapter.setClickListener(clickListener);

            return adapter;
        }
    }
}
