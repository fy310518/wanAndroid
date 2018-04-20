package wanandroid.fy.com.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import wanandroid.fy.com.entity.ArticleBean;

/**
 * 判断 新旧Item是否相等
 * Created by fangs on 2018/4/19.
 */
public class DiffCallBack extends DiffUtil.Callback {

    private List<ArticleBean.DatasBean> mOldDatas, mNewDatas;

    public DiffCallBack(List<ArticleBean.DatasBean> mOldDatas, List<ArticleBean.DatasBean> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    /**
     * 被 DiffUtil调用，用来判断 两个对象是否是相同的Item。
     * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getId() == (mNewDatas.get(newItemPosition).getId());
    }

    /**
     * 被 DiffUtil调用，用来检查 两个item是否含有相同的数据
     * DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
     * DiffUtil 用这个方法替代equals方法去检查是否相等。所以你可以根据你的UI去改变它的返回值
     * 例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
     *
     * ^_^ 这个方法仅仅在 areItemsTheSame()返回true时，才调用。^_^
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ArticleBean.DatasBean beanOld = mOldDatas.get(oldItemPosition);
        ArticleBean.DatasBean beanNew = mNewDatas.get(newItemPosition);

        if (beanOld.isCollect() != beanNew.isCollect()){
            return false; //如果有内容不同，就返回false
        }

        return true; //默认两个data内容是相同的
    }


    /**
     * 当{@link #areItemsTheSame(int, int)} 返回true，且{@link #areContentsTheSame(int, int)}
     * 返回false时，DiffUtils会回调此方法，去得到这个Item（有哪些）改变的payload。
     *
     * 例如，如果你用RecyclerView配合DiffUtils，你可以返回  这个Item改变的那些字段，
     * {@link android.support.v7.widget.RecyclerView.ItemAnimator ItemAnimator} 可以用那些信息去执行正确的动画
     *
     * 默认的实现是返回null; 否则返回 一个 代表着新老item的改变内容的 payload对象，
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // 定向刷新中的部分更新
        // 效率最高
        //只是没有了ItemChange的白光一闪动画
        ArticleBean.DatasBean oldBean = mOldDatas.get(oldItemPosition);
        ArticleBean.DatasBean newBean = mNewDatas.get(newItemPosition);

        //这里就不用比较核心字段了,一定相等
        Bundle payload = new Bundle();
        if (oldBean.isCollect() != newBean.isCollect()) {
            payload.putString("Collect", newBean.getDesc());
        }

        //如果没有变化 就传空
        if (payload.size() == 0) return null;

        return payload;//
    }
}
