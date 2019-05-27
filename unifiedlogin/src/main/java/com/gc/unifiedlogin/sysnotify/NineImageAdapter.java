package com.gc.unifiedlogin.sysnotify;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fy.baselibrary.utils.DensityUtils;
import com.fy.baselibrary.utils.ScreenUtils;
import com.fy.baselibrary.widget.NineGridView;
import com.gc.unifiedlogin.R;

import java.util.List;


/**
 * DESCRIPTION：应用  系统通知 消息列表 图片列表 适配器
 * Created by fangs on 2019/5/14 11:38.
 */
public class NineImageAdapter implements NineGridView.NineGridAdapter<String> {

    private List<String> mImageBeans;

    private Context mContext;
    private int itemSize;


    public NineImageAdapter(Context context, List<String> imageBeans) {
        this.mContext = context;
        this.mImageBeans = imageBeans;
        itemSize = (ScreenUtils.getScreenWidth() - 2 * DensityUtils.dp2px(4) - DensityUtils.dp2px(54)) / 3;
    }

    @Override
    public int getCount() {
        return mImageBeans == null ? 0 : mImageBeans.size();
    }

    @Override
    public String getItem(int position) {
        return mImageBeans == null ? null :
                position < mImageBeans.size() ? mImageBeans.get(position) : null;
    }

    @Override
    public View getView(int position, View itemView) {
        ImageView imageView;
        if (itemView == null) {
            imageView = new ImageView(mContext);
            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.BgColor));
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            imageView = (ImageView) itemView;
        }
        String url = mImageBeans.get(position);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(itemSize, itemSize)
                .error(R.mipmap.img_load_error);

        Glide.with(imageView.getContext())
                .load(watermark(url))
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        return imageView;
    }


    /**
     * 对指定的 图片地址 拼接 水印后缀参数
     * @param url
     * @return
     */
    public static String watermark(String url){
        return url + "?mode=1&alarm=" + "420115199410109818" + "&height=330&width=330";
    }
}
