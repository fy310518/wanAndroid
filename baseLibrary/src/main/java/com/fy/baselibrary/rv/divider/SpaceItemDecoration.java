package com.fy.baselibrary.rv.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecycleView item 间隔 (9宫格 样式分割线)
 * Created by fangs on 2017/12/29.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int minmSpace;

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
        minmSpace = (int) (mSpace * 0.5);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        设置item各个方向的间距
        outRect.top = (int) (mSpace * 0.25);
        outRect.bottom = (int) (mSpace * 0.25);

//        如果是RecyclerView的第一个子项
        int position = parent.getChildAdapterPosition(view);
        if (position % 3 == 0 ) {
            outRect.left = mSpace;
            outRect.right = 0;
        } else if (position % 3 == 1){
            outRect.left = minmSpace;
            outRect.right = 0;
        }else if (position % 3 == 2){
            outRect.left = minmSpace;
            outRect.right = mSpace;
        }
    }

}
