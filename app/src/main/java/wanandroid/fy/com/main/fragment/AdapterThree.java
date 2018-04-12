package wanandroid.fy.com.main.fragment;

import android.content.Context;
import android.widget.TextView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.rv.adapter.MultiCommonAdapter;
import com.fy.baselibrary.rv.adapter.MultiTypeSupport;
import com.fy.baselibrary.utils.ConstantUtils;

import java.util.List;

import wanandroid.fy.com.R;
import wanandroid.fy.com.entity.Bookmark;
import wanandroid.fy.com.utils.SelectUtils;

/**
 * 书签 adapter
 * Created by fangs on 2018/4/12.
 */
public class AdapterThree extends MultiCommonAdapter<Bookmark> {

    public AdapterThree(Context context, List<Bookmark> datas) {
        super(context, datas, new MultiTypeSupport<Bookmark>() {
            @Override
            public int getLayoutId(int itemType) {
                return itemType == ConstantUtils.StickyType ?
                        R.layout.item_fm_three_sticky : R.layout.item_fm_three;
            }

            @Override
            public int getItemViewType(int position, Bookmark bookmark) {
                return bookmark.getItemType();
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, Bookmark bookmark, int position) {
        if (bookmark.getItemType() == ConstantUtils.StickyType){
            holder.setText(R.id.tvTagSticky, bookmark.getName());
        } else {
            TextView te = holder.getView(R.id.tvTag);
            te.setText(bookmark.getName());
            te.setBackground(SelectUtils.getTagSelector(R.drawable.shape_tag));
        }
    }
}
