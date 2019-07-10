package com.gcstorage.map.shake.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeff.map.R;

import java.util.List;

import com.gcstorage.map.shake.api.ShakeCameraModel;

/**
 * 摇一摇结果 适配器 on 2019/4/13 0013.
 */

public class ApplyShakeResultAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShakeCameraModel> mData;
    private int shakeType;

    public ApplyShakeResultAdapter(Context context, List<ShakeCameraModel> data, int shakeType) {
        mContext = context;
        mData = data;
        this.shakeType = shakeType;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        View view = convertView;
        final ShakeCameraModel bean = mData.get(position);

        if (view == null) {
            view = View.inflate(mContext, R.layout.item_list_shake_camera, null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.info = (TextView) view.findViewById(R.id.tv_info);
            holder.distance = (TextView) view.findViewById(R.id.tv_distance);
            holder.fl = (TextView) view.findViewById(R.id.tv_fl);
            holder.phone = (TextView) view.findViewById(R.id.tv_phone);

            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText((position + 1) + "." + bean.NAME);
        holder.info.setText(bean.ADDRESS);

        if (TextUtils.isEmpty(bean.PHONE)) {
            holder.phone.setVisibility(View.GONE);
        } else {
            holder.phone.setVisibility(View.VISIBLE);
            holder.phone.setText(bean.PHONE);
        }

        if (shakeType == 1) {
            holder.fl.setVisibility(View.VISIBLE);
            try {
                int fl = Integer.parseInt(bean.FL);
                switch (fl) {
                    case 1:
                        if (bean.camera_rtsp != null && bean.camera_rtsp.length() > 1) {
                            holder.fl.setText("一类点 rtsp");
                        } else {
                            holder.fl.setText("一类点");
                        }
                        holder.fl.setBackgroundResource(R.drawable.shape_shake_fl1);
                        break;
                    case 2:
                        if (bean.camera_rtsp != null && bean.camera_rtsp.length() > 1) {
                            holder.fl.setText("二类点 rtsp");
                        } else {
                            holder.fl.setText("二类点");
                        }
                        holder.fl.setBackgroundResource(R.drawable.shape_shake_fl2);
                        break;
                    case 3:
                        if (bean.camera_rtsp != null && bean.camera_rtsp.length() > 1) {
                            holder.fl.setText("三类点 rtsp");
                        } else {
                            holder.fl.setText("三类点");
                        }
                        holder.fl.setBackgroundResource(R.drawable.shape_shake_fl3);
                        break;
                }
            } catch (Exception e) {
                holder.fl.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.fl.setVisibility(View.INVISIBLE);
        }

        holder.distance.setText(String.format("%d米", bean.distance));
        return view;
    }


    private static class ViewHolder {
        public TextView title; // 标题
        public TextView info; // 信息
        public TextView distance; // 距离
        public TextView fl; // 摄像头类型  1一类点 2二类点 3三类点
        public TextView phone; // 电话
    }
}
