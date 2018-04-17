package wanandroid.fy.com.main.fragment;

import android.content.Context;
import android.view.View;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.T;

import java.util.List;

import wanandroid.fy.com.R;
import wanandroid.fy.com.entity.ArticleBean;

/**
 * 首页列表实体类
 * Created by fangs on 2018/4/16.
 */
public class AdapterOne extends RvCommonAdapter<ArticleBean.DatasBean> {

    public AdapterOne(Context context, List<ArticleBean.DatasBean> datas) {
        super(context, R.layout.item_fm_one, datas);
    }

    @Override
    public void convert(ViewHolder holder, ArticleBean.DatasBean article, int position) {
        holder.setText(R.id.tvUserName, article.getAuthor());
        holder.setText(R.id.tvPublishTime, article.getNiceDate());
        holder.setText(R.id.tvPublishTitle, article.getTitle());
        holder.setText(R.id.tvPublishType, article.getChapterName());

        holder.setOnClickListener(R.id.imgCollect, view -> T.showLong("收藏此文章..."));
    }
}
