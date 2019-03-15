package com.gcstorage.circle.comment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.gcstorage.circle.R;

import java.util.List;

/**
 * 照片选择适配器
 * Created by Administrator on 2015/12/25.
 */
public class AddTrackAdapter extends BaseAdapter {

    private Context mContext; // 上下文
    private List<String> mDataList; // 数据源
    private  int img_length;

    public boolean mIsShowDelIcon; // 是否显示加号图标 true 显示  false 隐藏

    public AddTrackAdapter(Context context, List<String> datas, int img_length) {
        this.mContext = context; // 上下文
        this.mDataList = datas; // 数据源
        this.img_length=img_length;
    }

    @Override
    public int getCount() {
        if (mDataList.size() < img_length) {
            return mDataList.size() + 1; // 只有当选择的图片小于设定的最大值的时候，才显示“+”号图标
        } else {
            return mDataList.size(); // 否则，不允许继续添加
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder; // 优化，避免重复获取UI对象
        if (convertView == null) {
            // 当 convertView 为空时，创建 convertView对象
            convertView = LayoutInflater.from(mContext).inflate(R.layout.circle_comment_track_griditem, null);
            // 创建 ViewHolder对象
            holder = new ViewHolder();
            // 图片
            holder.picImg = (ImageView) convertView.findViewById(R.id.add_track_griditem);
            // 删除图标
            holder.delImg = (ImageView) convertView.findViewById(R.id.del);
            // 把 ViewHolder对象 保存在 convertView 中
            convertView.setTag(holder);
        } else {
            // 从 convertView 中取出 ViewHolder对象
            holder = (ViewHolder) convertView.getTag();
        }

        Log.d("hehe", "position =" + position + "  mDataList.size()=" + mDataList.size());
        if (position == mDataList.size()) {
            // 当索引等于数据源的长度时，就设置为“+”号图标
            ImgLoadUtils.loadImage(R.drawable.comrade_add_pic, R.mipmap.img_load_error, holder.picImg);
            // 隐藏删除图标
            holder.delImg.setVisibility(View.INVISIBLE);
        } else { // 选中的图片
            // 显示图片
            ImgLoadUtils.loadImage(mDataList.get(position), R.mipmap.img_load_error, holder.picImg);

            // mIsShowDelIcon true显示删除图标   false隐藏删除图标
            holder.delImg.setVisibility(mIsShowDelIcon ? View.VISIBLE : View.INVISIBLE);
        }
        // 为删除图标设置点击监听事件
        holder.delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShowDelIcon = true;
                // 删除数据源中对应的数据
                mDataList.remove(position);
//                EventBus.getDefault().post(new Events.DelImg(position));//todo
                // 刷新界面
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView picImg; // 图片
        ImageView delImg; // 删除图标
    }
}
