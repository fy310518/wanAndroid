package com.gcstorage.map.internetplus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeff.map.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterCollection extends RecyclerView.Adapter<AdapterCollection.MyViewHolder> {

    private List<InternetPlusBean> plusBeans = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_collection, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.iv_bg.setImageResource();
        if (plusBeans != null && plusBeans.size() != 0) {
            final InternetPlusBean bean = plusBeans.get(position);
            holder.tv_camera.setText("摄像头：" + bean.getAddress());
            holder.tv_camera_address.setText("摄像头地址：" + bean.getAddress());
            holder.tv_camera_time.setText("收藏时间：" + bean.getCreateTime());
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cid = bean.getCid();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return plusBeans.size();
    }

    public void setData(List<InternetPlusBean> data) {
        plusBeans.addAll(data);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_item;
        ImageView iv_bg;
        TextView tv_camera;
        TextView tv_camera_address;
        TextView tv_camera_time;


        public MyViewHolder(View view) {
            super(view);
            rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
            tv_camera = (TextView) view.findViewById(R.id.tv_camera);
            tv_camera_address = (TextView) view.findViewById(R.id.tv_camera_address);
            tv_camera_time = (TextView) view.findViewById(R.id.tv_camera_time);
        }
    }

}

