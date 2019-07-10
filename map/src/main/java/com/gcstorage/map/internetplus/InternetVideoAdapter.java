package com.gcstorage.map.internetplus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jeff.map.R;

import java.util.List;

public class InternetVideoAdapter extends BaseAdapter {
    private Context mContext;
    private List<InternetPlusBean> mData;

    public InternetVideoAdapter() {
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
        final InternetPlusBean bean = mData.get(position);

        if (view == null) {
            view = View.inflate(mContext, R.layout.item_list_shake_camera, null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.info = (TextView) view.findViewById(R.id.tv_info);
            holder.distance = (TextView) view.findViewById(R.id.tv_distance);
            holder.fl = (TextView) view.findViewById(R.id.tv_fl);
            holder.phone = (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText((position + 1) + ". " + bean.getDeviceName());
        holder.info.setVisibility(View.GONE);  //设备信息描述,
        holder.phone.setVisibility(View.GONE);  //tv_phone
        String deviceStatus = bean.getDeviceStatus();
        if (deviceStatus != null) {
            holder.fl.setVisibility(View.VISIBLE);
            if (deviceStatus.equals(1)) {
                holder.fl.setText("离线");
            } else {
                holder.fl.setText("在线");
            }
        } else {
            holder.fl.setVisibility(View.GONE);
        }
        return view;
    }

    //搜索之后,刷新数据
    public void setData(Context context, List<InternetPlusBean> data) {
        mContext = context;
        mData = data;
//        notifyDataSetChanged();
    }


    private static class ViewHolder {
        public TextView title; // 标题
        public TextView info; // 信息
        public TextView distance; // 距离
        public TextView fl; // 摄像头类型  1一类点 2二类点 3三类点
        public TextView phone; // 电话
    }

}
