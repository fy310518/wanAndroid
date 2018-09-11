package com.fy.wanandroid.request;

import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.retrofit.dialog.IProgressDialog;
import com.fy.wanandroid.R;

/**
 * 根据请求框架实现 请求等待对话框
 * Created by fangs on 2018/9/11 09:25.
 */
public class NetDialog extends IProgressDialog {

    public IProgressDialog init(AppCompatActivity mContext) {
        this.mContext = mContext;
        return this;
    }

    @Override
    public IProgressDialog setDialogMsg(int msg) {
        if (null == dialog) {
            dialog = NiceDialog.init()
                    .setLayoutId(R.layout.state_dialog_loading)
                    .setDialogConvertListener(new DialogConvertListener() {
                        @Override
                        protected void convertView(ViewHolder holder, CommonDialog dialog) {
                            // 加载动画
                            Animation loadAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading);
                            // 使用ImageView显示动画
                            ImageView imgLoadAnim = holder.getView(R.id.imgLoadAnim);
                            imgLoadAnim.setAnimation(loadAnim);

                            holder.setText(R.id.txtLoadHint, msg);
                        }
                    })
                    .setWidthPercent(CommonDialog.WidthPercent);
        }

        return this;
    }
}
