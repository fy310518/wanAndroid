package com.fy.wanandroid.main.fragment;

import android.content.Context;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.RvCommonAdapter;
import com.fy.baselibrary.utils.ResourceUtils;

import java.util.List;

import com.fy.wanandroid.R;
import com.fy.wanandroid.entity.TreeBean;

/**
 * 知识体系 fragment
 * Created by fangs on 2018/4/16.
 */
public class AdapterTwo extends RvCommonAdapter<TreeBean> {

    public AdapterTwo(Context context, List<TreeBean> datas) {
        super(context, R.layout.item_fm_two, datas);
    }

    @Override
    public void convert(ViewHolder holder, TreeBean treeBean, int position) {
        holder.setText(R.id.tvTree, treeBean.getName());

        List<TreeBean.ChildrenBean> childrenList = treeBean.getChildren();
        if (null != childrenList) {
            StringBuilder strBuilder = new StringBuilder();
            for (TreeBean.ChildrenBean children : childrenList) {
                String str = ResourceUtils.getReplaceStr(R.string.space, children.getName());
                strBuilder.append(str);
            }

            holder.setText(R.id.tvChildTitle, strBuilder.toString());
        }
    }
}
