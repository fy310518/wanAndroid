package com.fy.wanandroid.main.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.Html;
import android.widget.TextView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.utils.TintUtils;

import java.util.List;

import com.fy.wanandroid.R;
import com.fy.wanandroid.api.ApiService;
import com.fy.wanandroid.entity.ArticleBean;

/**
 * 首页列表实体类
 * Created by fangs on 2018/4/16.
 */
public class AdapterOne extends RvCommonAdapter<ArticleBean.DatasBean> {

    private boolean isDelete;

    public AdapterOne(Context context, List<ArticleBean.DatasBean> datas) {
        super(context, R.layout.item_fm_one, datas);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        payloads 对象不会为null，但是它可能是空（empty），这时候需要完整绑定(所以我们在方法里只要判断isEmpty就好，不用重复判空)。
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            ArticleBean.DatasBean article = mDatas.get(position);

            setCollectImg(holder, article.isCollect());
        }
    }

    //设置 收藏 图标 颜色
    private void setCollectImg(ViewHolder holder, boolean collect){
        AppCompatImageView imgCollect = holder.getView(R.id.imgCollect);
        if (collect) {//已收藏
            imgCollect.setImageDrawable(TintUtils.getTintDrawable(R.drawable.svg_collect, R.color.txtHighlight));
        } else {
            imgCollect.setImageResource(R.drawable.svg_collect);
        }
    }

    @Override
    public void convert(ViewHolder holder, ArticleBean.DatasBean article, int position) {
        holder.setText(R.id.tvUserName, article.getAuthor());
        holder.setText(R.id.tvPublishTime, article.getNiceDate());
        holder.setText(R.id.tvPublishType, article.getChapterName());

        TextView tvPublishTitle = holder.getView(R.id.tvPublishTitle);
        tvPublishTitle.setText(Html.fromHtml(article.getTitle()));

        AppCompatImageView imgCollect = holder.getView(R.id.imgCollect);
        setCollectImg(holder, article.isCollect());
        imgCollect.setOnClickListener(view -> {
                    if (article.isCollect()) {//已收藏 则 点击取消
                        if (isDelete) {
                            unMycollectArticle(article, position);
                        } else {
                            uncollectArticle(article, position);
                        }
                    } else {
                        collectArticle(article, position);
                    }
                }
        );
    }

    //    收藏
    private void collectArticle(ArticleBean.DatasBean article, int position) {
        RequestUtils.create(ApiService.class)
                .collectArticle(article.getId(), "")
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object collect) {
                        T.showLong("收藏成功");
                        article.setCollect(true);
                        if (null != changeItemListener) changeItemListener.onChange(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

    //    取消收藏
    private void uncollectArticle(ArticleBean.DatasBean article, int position) {
        RequestUtils.create(ApiService.class)
                .uncollectArticle(article.getId(), "")
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object collect) {
                        T.showLong("取消收藏成功");
                        article.setCollect(false);

                        if (null != changeItemListener) changeItemListener.onChange(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

    //    我的收藏页面, 取消收藏
    private void unMycollectArticle(ArticleBean.DatasBean article, int position) {
        RequestUtils.create(ApiService.class)
                .unMyCollectArticle(article.getId(), article.getOriginId())
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object collect) {
                        T.showLong("取消收藏成功");
                        removeData(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
