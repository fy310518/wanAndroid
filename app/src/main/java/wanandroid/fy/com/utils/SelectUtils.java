package wanandroid.fy.com.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.TintUtils;

import wanandroid.fy.com.R;

/**
 * 代码设置 选择器
 * Created by fangs on 2018/4/2.
 */
public class SelectUtils {

    private SelectUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取 指定 ID的 drawable，生成的 选择器
     * @param draId
     * @return
     */
    public static Drawable getSelector(@DrawableRes int draId){
        int[] colors = new int[]{ResourceUtils.getColor(R.color.button_pressed),
                ResourceUtils.getColor(R.color.button_pressed),
                ResourceUtils.getColor(R.color.button_pressed),
                ResourceUtils.getColor(R.color.button_normal)};

        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_selected};
        states[2] = new int[]{android.R.attr.state_checked};
        states[3] = new int[]{};

        return TintUtils.tintSelector(TintUtils.getDrawable(draId), colors, states);
    }

    /**
     * 根据 指定 drawable的 ID ，生成 选择器
     * @param draId
     * @return
     */
    public static Drawable getTagSelector(@DrawableRes int draId){
        int[] colors = new int[]{
                ResourceUtils.getColor(R.color.button_pressed),
                ResourceUtils.getColor(R.color.button_normal)};

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};

        return TintUtils.tintSelector(TintUtils.getDrawable(draId), colors, states);
    }
}
