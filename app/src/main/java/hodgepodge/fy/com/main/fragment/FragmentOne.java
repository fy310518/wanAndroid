package hodgepodge.fy.com.main.fragment;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.utils.ResourceUtils;
import com.fy.baselibrary.utils.TintUtils;

import butterknife.BindView;
import butterknife.OnClick;
import hodgepodge.fy.com.R;

/**
 * 首页
 * Created by Administrator on 2017/12/12.
 */
public class FragmentOne extends BaseFragment {

    @BindView(R.id.tvImgPicker)
    TextView tvImgPicker;

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_one;
    }

    @Override
    protected void baseInit() {

        Drawable drawable = TintUtils.getDrawable(R.drawable.vector_mainfive_normal);
        int[][] states = new int[2][];
        int[] colors = new int[]{ResourceUtils.getColor(R.color.pink),
                ResourceUtils.getColor(R.color.colorPrimaryDark)};

        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        Drawable drawable2 = TintUtils.tintSelector(drawable, colors, states);
        TintUtils.setTxtIconLocal(tvImgPicker, drawable2, 1);
    }


    @OnClick({R.id.tvImgPicker})
    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.tvImgPicker:
                NiceDialog.init()
                        .setLayoutId(R.layout.demo_popup)
                        .setDialogConvertListener(new DialogConvertListener() {
                            @Override
                            protected void convertView(ViewHolder holder, CommonDialog dialog) {
                                holder.setText(R.id.pop_financial, "大王叫我来巡山");
                            }
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setAnim(R.style.AnimUp)
                        .setHide(true)
                        .setDimAmount(0.7f)
                        .show(getFragmentManager());
                break;
        }
    }
}
