package com.fy.baselibrary.rv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.fy.baselibrary.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 通用的Adapter
 * Created by fangs on 2017/7/31.
 */
public abstract class RvCommonAdapter<Item> extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;


    protected Context mContext;
    protected int mLayoutId;
    protected List<Item> mDatas;
    protected LayoutInflater mInflater;

    protected OnListener.OnitemClickListener itemClickListner;//列表条目点击事件
    protected OnListener.OnRemoveItemListener removeItemListener;
    public OnListener.OnChangeItemListener changeItemListener;

    protected SparseBooleanArray mSelectedPositions;//保存多选 数据

    private RecyclerView mRv;
    private int mSelectedPos = -1;//实现单选  保存当前选中的position

    public RvCommonAdapter(Context context, int layoutId, List<Item> datas) {
        init(context, layoutId, datas);
    }

    public RvCommonAdapter(Context context, int layoutId, List<Item> datas, RecyclerView rv) {
        init(context, layoutId, datas);
        this.mRv = rv;
    }

    private void init(Context context, int layoutId, List<Item> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        this.mDatas = datas;

        this.mSelectedPositions = new SparseBooleanArray();
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
     *
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, Item item, int position);

    /**
     * 绑定点击事件
     *
     * @param viewHolder
     */
    protected void bindOnClick(ViewHolder viewHolder) {
//        避免 在onBindViewHolder里面频繁创建事件回调，应该在 onCreateViewHolder()中每次为新建的 View 设置一次即可
        if (null != itemClickListner) {
//            需要在 convert() 最后使用 holder.itemView.setTag(Item)
            viewHolder.itemView.setOnClickListener(v -> itemClickListner.onItemClick(v));
        }
    }

    public void setmDatas(List<Item> list) {
        mDatas.clear();
        mDatas.addAll(list);
    }

    /**
     * 添加data，从指定location中加入
     *
     * @param location
     * @param item
     */
    public void addData(int location, Item item) {
        this.mDatas.add(location, item);
    }

    /**
     * 追加一个集合
     *
     * @param data
     */
    public void addData(List<Item> data) {
        this.mDatas.addAll(data);
    }

    /**
     * 追加一个集合
     *
     * @param location
     * @param data
     */
    public void addData(int location, List<Item> data) {
        this.mDatas.addAll(location, data);
    }

    /**
     * 删除指定 Location 位置的data
     *
     * @param location
     */
    public void removeData(int location) {
        if (location < getItemCount()) this.mDatas.remove(location);
    }

    /**
     * 清理 多选状态
     */
    public void cleanChecked() {
        mSelectedPositions.clear();
    }

    /**
     * 设置给定位置条目的选择状态
     *
     * @param array
     * @param position
     * @param isChecked
     */
    protected void setItemChecked(SparseBooleanArray array, int position, boolean isChecked) {
        array.put(position, isChecked);
    }

    /**
     * 根据位置判断条目是否选中
     *
     * @param position
     * @return
     */
    protected boolean isItemChecked(SparseBooleanArray array, int position) {
        return array.get(position);
    }

    /**
     * 设置全选 or 反选
     *
     * @param isAllSelect
     */
    public void setIsAllSelect(boolean isAllSelect) {
        for (int i = 0; i < getItemCount(); i++) {
            setItemChecked(mSelectedPositions, i, isAllSelect);
        }

        notifyDataSetChanged();
    }


    public List<Item> getmDatas() {
        return this.mDatas;
    }

    public SparseBooleanArray getmSelectedPositions() {
        return mSelectedPositions;
    }


    /**
     * 设置 item 点击事件 监听
     *
     * @param itemClickListner
     */
    public void setItemClickListner(OnListener.OnitemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    /**
     * 设置 item 删除事件 监听
     *
     * @param removeItemListener
     */
    public void setRemoveItemListener(OnListener.OnRemoveItemListener removeItemListener) {
        this.removeItemListener = removeItemListener;
    }

    /**
     * 设置 item 更新事件 监听
     *
     * @param changeItemListener
     */
    public void setChangeItemListener(OnListener.OnChangeItemListener changeItemListener) {
        this.changeItemListener = changeItemListener;
    }


//    单选 样板代码
//    holder.ivSelect.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            //实现单选方法三： RecyclerView另一种定向刷新方法：不会有白光一闪动画 也不会重复onBindVIewHolder
//            CouponVH couponVH = (CouponVH) mRv.findViewHolderForLayoutPosition(mSelectedPos);
//            if (couponVH != null) {//还在屏幕里
//                couponVH.ivSelect.setSelected(false);//此处注意判空
//            }else {//add by 2016 11 22 for 一些极端情况，holder被缓存在Recycler的cacheView里，
//                //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
//                notifyItemChanged(mSelectedPos);
//            }
//            mDatas.get(mSelectedPos).setSelected(false);//不管在不在屏幕里 都需要改变数据
//            //设置新Item的勾选状态
//            mSelectedPos = position;
//            mDatas.get(mSelectedPos).setSelected(true);
//            holder.ivSelect.setSelected(true);
//        }
//    });


    //过滤器上的锁可以同步复制原始数据。
    private final Object mLock = new Object();
    //对象数组的备份，当调用 Filter 的时候初始化和使用。此时，对象数组只包含已经过滤的数据。
    private ArrayList<Item> mOriginalValues;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override//执行过滤操作
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();//过滤的结果
                //原始数据备份为空时，上锁，同步复制原始数据
                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList<>(mDatas);
                    }
                }

                //过滤条件为空
                if (constraint == null || constraint.length() == 0) {
                    ArrayList<Item> list;
                    synchronized (mLock) {//同步复制一个原始备份数据
                        list = new ArrayList<>(mOriginalValues);
                    }
                    results.values = list;
                    results.count = list.size();//此时返回的results就是原始的数据，不进行过滤
                } else {
                    ArrayList<Item> values;
                    synchronized (mLock) {//同步复制一个原始备份数据
                        values = new ArrayList<>(mOriginalValues);
                    }

                    int count = values.size();
                    ArrayList<Item> newValues = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        Item value = values.get(i);//从List<Item>中拿到 对象
                        if (filterRule(value, constraint)) {
                            newValues.add(value);//将这个item加入到数组对象中
                        }
                    }

                    results.values = newValues;//此时的results就是过滤后的List<Item>数组
                    results.count = newValues.size();
                }
                return results;
            }

            @Override//把过滤后的值返回出来
            protected void publishResults(CharSequence constraint, FilterResults results) {
                setmDatas((List<Item>) results.values);//此时，Adapter数据源就是过滤后的Results
                notifyDataSetChanged();//这个相当于从mDatas中删除了一些数据，只是数据的变化，故使用notifyDataSetChanged()
            }
        };
    }

    /**
     * 如需 根据关键字筛选功能，子类 adapter 重写此方法，定义过滤规则
     * @param value         bean
     * @param constraint    过滤条件
     * @return              满足过滤条件返回 true
     */
    public boolean filterRule(Item value, CharSequence constraint){
        return false;
    }
}
