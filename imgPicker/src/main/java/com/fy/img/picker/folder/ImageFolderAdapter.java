package com.fy.img.picker.folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fy.baselibrary.base.CommonAdapter;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.fy.bean.ImageFolder;
import com.fy.img.picker.R;

import java.util.List;

/**
 * 图片文件夹的适配器
 * Created by fangs on 2017/6/30.
 */
public class ImageFolderAdapter extends CommonAdapter<ImageFolder> {

    private int lastSelected = 0;
    /** 是否显示 拍照按钮 */
    private boolean isShowPicture = false;

    public ImageFolderAdapter(Context context, List<ImageFolder> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        View itemView = getViewCache().get(position);

        if (null == itemView) {
            final ImageFolder item = getData().get(position);
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_imgfolder, null);

            ImageView iv_cover = itemView.findViewById(R.id.iv_cover);
            ImageView iv_folder_check = itemView.findViewById(R.id.iv_folder_check);
            TextView tv_folder_name = itemView.findViewById(R.id.tv_folder_name);
            TextView tv_image_count = itemView.findViewById(R.id.tv_image_count);
			
			int sum = isShowPicture && position == 0 ? item.images.size() - 1 : item.images.size();
            tv_image_count.setText(ResUtils.getReplaceStr(R.string.folder_image_count, sum));

            tv_folder_name.setText(item.name);
            ImgLoadUtils.loadImage(item.cover.path,R.mipmap.default_image, iv_cover);

            if (lastSelected == position) {
                iv_folder_check.setVisibility(View.VISIBLE);
            } else {
                iv_folder_check.setVisibility(View.INVISIBLE);
            }
            
            itemView.setTag(item);
            getViewCache().put(position, itemView);
        }

        return itemView;
    }

    public void setSelectIndex(int i, boolean isShowPicture) {
        lastSelected = i;
        this.isShowPicture = isShowPicture;
        clearCache();
    }
}
