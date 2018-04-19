package wanandroid.fy.com.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.utils.TintUtils;

import java.util.ArrayList;
import java.util.List;

import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.ArticleBean;

/**
 * 首页列表实体类
 * Created by fangs on 2018/4/16.
 */
public class AdapterOne extends RvCommonAdapter<ArticleBean.DatasBean> {

    private boolean isDelete;

    public AdapterOne(Context context, List<ArticleBean.DatasBean> datas) {
        super(context, R.layout.item_fm_one, datas);
    }

    //    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
//        payloads 对象不会为null，但是它可能是空（empty），这时候需要完整绑定(所以我们在方法里只要判断isEmpty就好，不用重复判空)。
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            ArticleBean.DatasBean article = mDatas.get(position);

            AppCompatImageView imgCollect = holder.getView(R.id.imgCollect);
            if (article.isCollect()) {//已收藏
                imgCollect.setImageDrawable(TintUtils.getTintDrawable(R.drawable.svg_collect, R.color.txtHighlight));
            } else {
                imgCollect.setImageResource(R.drawable.svg_collect);
            }
        }
    }

    @Override
    public void convert(ViewHolder holder, ArticleBean.DatasBean article, int position) {
        holder.setText(R.id.tvUserName, article.getAuthor());
        holder.setText(R.id.tvPublishTime, article.getNiceDate());
        holder.setText(R.id.tvPublishTitle, article.getTitle());
        holder.setText(R.id.tvPublishType, article.getChapterName());

        AppCompatImageView imgCollect = holder.getView(R.id.imgCollect);
        if (article.isCollect()) {//已收藏
            imgCollect.setImageDrawable(TintUtils.getTintDrawable(R.drawable.svg_collect, R.color.txtHighlight));
        } else {
            imgCollect.setImageResource(R.drawable.svg_collect);
        }

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

                        notifyItemChanged(position, "");
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

                        notifyItemChanged(position, "");
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
                        List<ArticleBean.DatasBean> list = new ArrayList<>();
                        list.addAll(getmDatas());
                        list.remove(position);

                        DiffUtil.DiffResult diffResult = DiffUtil
                                .calculateDiff(new DiffCallBack(getmDatas(), list), true);

                        diffResult.dispatchUpdatesTo(AdapterOne.this);
                        //别忘了将新数据给Adapter
                        setmDatas(list);
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
