package com.fy.baselibrary.retrofit.dialog;

import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;

/**
 * 加载 dialog
 * Created by fangs on 2017/3/10.
 */
public class DialogLoad extends CommonDialog {

    private String msg = "";

    public DialogLoad() {}

    public static DialogLoad init() {
        return new DialogLoad();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.state_dialog_loading;
    }

    @Override
    public void convertView(ViewHolder holder, CommonDialog dialog) {
        // 加载动画
        Animation loadAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_loading);
        // 使用ImageView显示动画
        ImageView imgLoadAnim = holder.getView(R.id.imgLoadAnim);
        imgLoadAnim.setAnimation(loadAnim);

        if (!TextUtils.isEmpty(msg)){
            holder.setText(R.id.txtLoadHint, msg);
        }
    }

    public DialogLoad setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
