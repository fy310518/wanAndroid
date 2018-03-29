package hodgepodge.fy.com.main.fragment;

import android.view.View;

import com.fy.baselibrary.base.BaseFragment;

import hodgepodge.fy.com.R;

/**
 * 我的
 * Created by fangs on 2017/12/12.
 */
public class FragmentFive extends BaseFragment {


    @Override
    protected int setContentLayout() {
        return R.layout.fragment_main_five;
    }

    @Override
    protected void baseInit() {

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }


}
