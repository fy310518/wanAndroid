package hodgepodge.fy.com.main.fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.PopupDismissListner;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.popupwindow.CommonPopupWindow;
import com.fy.baselibrary.base.popupwindow.NicePopup;
import com.fy.baselibrary.utils.T;

import butterknife.OnClick;
import hodgepodge.fy.com.R;

/**
 * Created by Administrator on 2017/12/12.
 * 数据
 */
public class FragmentTwo extends BaseFragment {

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_two;
    }

    @Override
    protected void baseInit() {

    }

    @OnClick({R.id.tvPopup})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvPopup:
                showPopupWindow(view);
                break;
            case R.id.pop_computer:
                T.showLong("computer");
                mPopWindow.dismiss();
                break;
            case R.id.pop_financial:
                T.showLong("financial");
                mPopWindow.dismiss();
                break;
            case R.id.pop_manage:
                T.showLong("manage");
                mPopWindow.dismiss();
                break;
        }
    }

    private PopupWindow mPopWindow;
    private void showPopupWindow(View view) {

        mPopWindow = NicePopup.Builder.init()
                .setLayoutId(R.layout.demo_popup)
                .setConvertListener(new NicePopup.PopupConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder) {
                        TextView tv1 = holder.getView(R.id.pop_computer);
                        TextView tv2 = holder.getView(R.id.pop_financial);
                        TextView tv3 = holder.getView(R.id.pop_manage);

                        tv1.setText("大王叫我来巡山");
                        tv1.setOnClickListener(FragmentTwo.this);
                        tv2.setOnClickListener(FragmentTwo.this);
                        tv3.setOnClickListener(FragmentTwo.this);
                    }
                }).create()
                .setDismissListner(new PopupDismissListner() {
                    @Override
                    public void onDismiss() {
                        T.showLong("popupWindow 已关闭");
                    }
                }).setAnim(R.style.AnimUp)
                .setOutside(true)
                .onCreateView(getContext());

        mPopWindow.showAtLocation(getActivity().findViewById(android.R.id.content),
                Gravity.BOTTOM, 0, 0);

    }
}
