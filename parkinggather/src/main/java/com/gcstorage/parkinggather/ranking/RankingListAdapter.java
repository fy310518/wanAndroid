package com.gcstorage.parkinggather.ranking;

import android.content.Context;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.gcstorage.parkinggather.R;

import java.util.List;

/**
 * DESCRIPTION：排行榜 列表 适配器
 * Created by fangs on 2019/7/8 17:19.
 */
public class RankingListAdapter extends RvCommonAdapter<String> {

    public RankingListAdapter(Context context, List<String> datas) {
        super(context, R.layout.ranking_list_item, datas);
    }

    @Override
    public void convert(ViewHolder holder, String s, int position) {

    }

}
