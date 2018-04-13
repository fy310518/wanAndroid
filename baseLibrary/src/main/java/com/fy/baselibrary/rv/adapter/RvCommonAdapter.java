package com.fy.baselibrary.rv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fy.baselibrary.base.ViewHolder;

import java.util.List;

/**
 * RecyclerView 通用的Adapter
 * Created by fangs on 2017/7/31.
 */
public abstract class RvCommonAdapter<Item> extends RecyclerView.Adapter<ViewHolder> {

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;


    protected Context mContext;
    protected int mLayoutId;
    protected List<Item> mDatas;
    protected LayoutInflater mInflater;

    protected OnItemClickListner itemClickListner;//列表条目点击事件
    protected SparseBooleanArray mSelectedPositions;//保存多选 数据

    public RvCommonAdapter(Context context, int layoutId, List<Item> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        mSelectedPositions = new SparseBooleanArray();
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.createViewHolder(mContext, parent, mLayoutId);

        bindOnClick(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);

//        设置 tag 对应 onCreateViewHolder() 设置点击事件
        holder.itemView.setTag(getmDatas().get(position));
    }

    /**
     * 渲染数据到 View中
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, Item item, int position);

    /**
     * 绑定点击事件
     * @param viewHolder
     */
    protected void bindOnClick(ViewHolder viewHolder) {
//        避免 在onBindViewHolder里面频繁创建事件回调，应该在 onCreateViewHolder()中每次为新建的 View 设置一次即可
        if (null != itemClickListner) {
//            需要在 convert() 最后使用 holder.itemView.setTag(Item)
            viewHolder.itemView.setOnClickListener(v -> itemClickListner.onItemClick(v));
        }
    }


    /**
     * 添加data，从指定location中加入
     * @param location
     * @param item
     */
    public void addData(int location, Item item){
        this.mDatas.add(location, item);
    }

    /**
     * 追加一个集合
     * @param data
     */
    public void addData(List<Item> data){
        this.mDatas.addAll(data);
    }

    /**
     * 追加一个集合
     * @param location
     * @param data
     */
    public void addData(int location, List<Item> data){
        this.mDatas.addAll(location, data);
    }

    /**
     * 删除指定 Location 位置的data
     * @param location
     */
    public void removeData(int location){
        if (location < getItemCount()) this.mDatas.remove(location);
    }

    /**
     * 清理 多选状态
     */
    public void cleanChecked(){
        mSelectedPositions.clear();
    }

    /**
     * 设置给定位置条目的选择状态
     * @param array
     * @param position
     * @param isChecked
     */
    protected void setItemChecked(SparseBooleanArray array, int position, boolean isChecked){
        array.put(position, isChecked);
    }

    /**
     * 根据位置判断条目是否选中
     * @param position
     * @return
     */
    protected boolean isItemChecked(SparseBooleanArray array, int position) {
        return array.get(position);
    }

    /**
     * 设置全选 or 反选
     * @param isAllSelect
     */
    public void setIsAllSelect(boolean isAllSelect){
        for (int i = 0; i < getItemCount(); i++){
            setItemChecked(mSelectedPositions, i, isAllSelect);
        }

        notifyDataSetChanged();
    }

    public void setmDatas(List<Item> mDatas) {
        this.mDatas = mDatas;
    }

    public List<Item> getmDatas() {
        return this.mDatas;
    }

    public SparseBooleanArray getmSelectedPositions() {
        return mSelectedPositions;
    }

    /**
     * 设置 item 点击事件 监听
     * @param itemClickListner
     */
    public void setItemClickListner(OnItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
