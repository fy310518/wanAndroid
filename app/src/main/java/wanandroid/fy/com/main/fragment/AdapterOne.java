package wanandroid.fy.com.main.fragment;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.T;
import com.fy.baselibrary.utils.TintUtils;

import java.util.List;

import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
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

        AppCompatImageView imgCollect = holder.getView(R.id.imgCollect);
        if (article.isCollect()) {//已收藏
            imgCollect.setImageDrawable(TintUtils.getTintDrawable(R.drawable.svg_collect, R.color.txtHighlight));
        } else {
            imgCollect.setImageResource(R.drawable.svg_collect);
        }

        imgCollect.setOnClickListener(view -> {
                    if (article.isCollect()) {//已收藏 则 点击取消
                        uncollectArticle(article, position);
                    } else {
                        collectArticle(article, position);
                    }
                }
        );
    }

//    收藏
    private void collectArticle(ArticleBean.DatasBean article, int position){
        RequestUtils.create(ApiService.class)
                .collectArticle(article.getId(), "")
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object collect) {
                        T.showLong("收藏成功");
                        article.setCollect(true);

//                        removeData(position);
//                        addData(position, article);
                        notifyItemChanged(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }

//    取消收藏
    private void uncollectArticle(ArticleBean.DatasBean article, int position){
        RequestUtils.create(ApiService.class)
                .uncollectArticle(article.getId(), "")
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<Object>() {
                    @Override
                    protected void onSuccess(Object collect) {
                        T.showLong("取消收藏成功");
                        article.setCollect(false);

//                        removeData(position);
//                        addData(position, article);
                        notifyItemChanged(position);
                    }

                    @Override
                    protected void updataLayout(int flag) {

                    }
                });
    }
}
