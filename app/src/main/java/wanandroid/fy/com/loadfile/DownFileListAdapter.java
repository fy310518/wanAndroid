package wanandroid.fy.com.loadfile;

import android.content.Context;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.load.down.DownInfo;
import com.fy.baselibrary.retrofit.load.down.DownManager;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.FileUtils;
import com.fy.baselibrary.utils.TransfmtUtils;

import java.io.File;
import java.util.List;

import wanandroid.fy.com.R;

/**
 * 文件下载 列表 adapter
 * Created by fangs on 2018/6/5.
 */
public class DownFileListAdapter extends RvCommonAdapter<DownInfo> {

    public DownFileListAdapter(Context context, List<DownInfo> datas) {
        super(context, R.layout.item_down_file, datas);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        payloads 对象不会为null，但是它可能是空（empty），这时候需要完整绑定(所以我们在方法里只要判断isEmpty就好，不用重复判空)。
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            DownInfo downInfo = mDatas.get(position);
            holder.setText(R.id.tvProgress, TransfmtUtils.doubleToKeepTwoDecimalPlaces(downInfo.getPercent()) + "");
        }
    }

    @Override
    public void convert(ViewHolder holder, DownInfo downInfo, int position) {
        File file = FileUtils.createFile(downInfo.getUrl());
        holder.setText(R.id.tvTaskName, file.getName());
        holder.setText(R.id.tvProgress, TransfmtUtils.doubleToKeepTwoDecimalPlaces(downInfo.getPercent()) + "");

        //暂停 当前请求
        holder.setOnClickListener(R.id.tvPause, v -> DownManager.getInstentce().pause(downInfo.getUrl()));
        //取消 当前请求
        holder.setOnClickListener(R.id.tvCancel, v -> DownManager.getInstentce().cancle(downInfo.getUrl()));
    }
}
