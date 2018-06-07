package wanandroid.fy.com.loadfile;

import android.content.Context;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.load.down.DownInfo;
import com.fy.baselibrary.retrofit.load.down.DownManager;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.TransfmtUtils;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;

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
            holder.setText(R.id.tvDownLoadStatus, getButtonText(downInfo.getStateInte()));
            holder.setText(R.id.tvProgress, TransfmtUtils.doubleToKeepTwoDecimalPlaces(downInfo.getPercent()) + "");
        }
    }

    @Override
    public void convert(ViewHolder holder, DownInfo downInfo, int position) {
        ImageView imgIcon = holder.getView(R.id.imgIcon);
        ImgLoadUtils.loadImage(mContext, downInfo.getImageUrl(), imgIcon);
        holder.setText(R.id.tvTaskName, downInfo.getName());

        holder.setText(R.id.tvDownLoadStatus, getButtonText(downInfo.getStateInte()));
        holder.setText(R.id.tvProgress, TransfmtUtils.doubleToKeepTwoDecimalPlaces(downInfo.getPercent()) + "");

        //当前下载状态
        holder.setOnClickListener(R.id.tvDownLoadStatus, v -> startTask(downInfo));
    }

    public void startTask(DownInfo downInfo){
        switch (downInfo.getStateInte()) {
            case DownInfo.STATUS_NOT_DOWNLOAD:
                DownManager.getInstentce().runDownTask();
                break;
            case DownInfo.STATUS_DOWNLOADING:
                DownManager.getInstentce().pause(downInfo.getUrl());
                break;
            case DownInfo.STATUS_PAUSED:
                DownManager.getInstentce().stratDown(downInfo);
                break;
            case DownInfo.STATUS_CANCEL:

                break;
            case DownInfo.STATUS_DOWNLOAD_ERROR:
                DownManager.getInstentce().stratDown(downInfo);
                break;
            case DownInfo.STATUS_COMPLETE:

                break;
        }
    }

    public String getButtonText(int status) {
        switch (status) {
            case DownInfo.STATUS_NOT_DOWNLOAD:
                return "下载队列中...";
            case DownInfo.STATUS_DOWNLOADING:
                return "pause download";
            case DownInfo.STATUS_PAUSED:
                return "resumed download";
            case DownInfo.STATUS_CANCEL:
                return "start download";
            case DownInfo.STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case DownInfo.STATUS_COMPLETE:
                return "Install";
            default:
                return "Download";
        }
    }
}
