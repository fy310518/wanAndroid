package com.gcstorage.parkinggather.ranking;

import android.content.Context;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.imgload.ImgLoadUtils;
import com.gcstorage.parkinggather.R;
import com.gcstorage.parkinggather.bean.DailyRankingEntity;

import java.util.List;

/**
 * DESCRIPTION：排行榜 列表 适配器
 * Created by fangs on 2019/7/8 17:19.
 */
public class RankingListAdapter extends RvCommonAdapter<DailyRankingEntity> {

    boolean type;

    public RankingListAdapter(Context context, List<DailyRankingEntity> datas, boolean type) {
        super(context, R.layout.ranking_list_item, datas);
        this.type = type;
    }

    @Override
    public void convert(ViewHolder holder, DailyRankingEntity dailyRanking, int position) {
        holder.setText(R.id.tv_rank, position + 1 + "");
        holder.setText(R.id.tv_count, dailyRanking.getCarnum() + "");

        if (type){//个人排行榜
            holder.setText(R.id.tv_name, dailyRanking.getMax());
            ImgLoadUtils.loadRadiusImg(dailyRanking.getRE_headpic(), R.drawable.default_pic_icon, holder.getView(R.id.head_pic));
        } else {//分局排行榜
            holder.setText(R.id.tv_name, dailyRanking.getDepartment());
        }

        ImageView iv_up = holder.getView(R.id.iv_up);
        iv_up.setImageResource(R.mipmap.rank_up);

    }

}
