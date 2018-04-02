package wanandroid.fy.com.main.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.base.BaseFragment;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.retrofit.NetCallBack;
import com.fy.baselibrary.retrofit.RequestUtils;
import com.fy.baselibrary.retrofit.RxHelper;
import com.fy.baselibrary.utils.L;

import butterknife.BindView;
import butterknife.OnClick;
import wanandroid.fy.com.R;
import wanandroid.fy.com.api.ApiService;
import wanandroid.fy.com.entity.HomeBean;

/**
 * 首页
 * Created by fangs on 2017/12/12.
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
        getHomeList(0);
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

    private void getHomeList(int pageNum){
        RequestUtils.create(ApiService.class)
                .getHomeList(pageNum)
                .compose(RxHelper.handleResult())
                .doOnSubscribe(RequestUtils::addDispos)
                .subscribe(new NetCallBack<HomeBean>() {
                    @Override
                    protected void onSuccess(HomeBean login) {

                    }

                    @Override
                    protected void updataLayout(int flag) {
                        L.e("net updataLayout", flag + "-----");
                    }
                });
    }
}
