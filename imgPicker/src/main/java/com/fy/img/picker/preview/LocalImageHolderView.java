package com.fy.img.picker.preview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.fy.baselibrary.utils.Validator;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.img.picker.bean.ImageItem;
import com.fy.library.imgpicker.R;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * 本地图片Holder
 * Created by fangs on 2017/7/6.
 */
public class LocalImageHolderView implements Holder<ImageItem> {

    private PicturePreviewActivity activity;
    private View view;
    private PhotoView imageView;

    private LinearLayout llProgress;
    private AppCompatImageView imgLoad;
    private TextView tvLoadProgress;

    public LocalImageHolderView(PicturePreviewActivity activity) {
        this.activity = activity;
    }

    @Override
    public View createView(Context context) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.viewpager_preview, null);
        }


        llProgress = view.findViewById(R.id.llProgress);
        imgLoad = view.findViewById(R.id.imgLoad);
        tvLoadProgress = view.findViewById(R.id.tvLoadProgress);
        imageView = view.findViewById(R.id.subImageView);
        imageView.setOnClickListener(v -> activity.toggleStateChange());

        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, ImageItem imgData) {

        if (Validator.isNetAddress(imgData.path)){
            //todo 网络图片 待实现 fagns
//            ProgressInterceptor.addListener(imgData.path, new ProgressListener() {
//                @Override
//                public void onProgress(int progress) {
//                    L.e("progress", progress + "------" + Thread.currentThread().getName());
//                    activity.runOnUiThread(() -> tvLoadProgress.setText(ResUtils.getReplaceStr(R.string.loadProgress, progress)));
//                }
//            });
//
//            ImgLoadUtils.loadImage(activity, imgData.path, new GlideDrawableImageViewTarget(imageView) {
//                @Override
//                public void onLoadStarted(Drawable placeholder) {
//                    super.onLoadStarted(placeholder);
//                    llProgress.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                    super.onResourceReady(resource, animation);
//                    llProgress.setVisibility(View.GONE);
//                    ProgressInterceptor.removeListener(imgData.path);
//                }
//            });
        } else {//加载本地图片
            ImgLoadUtils.loadImage(imgData.path, imageView);
        }
    }
}
