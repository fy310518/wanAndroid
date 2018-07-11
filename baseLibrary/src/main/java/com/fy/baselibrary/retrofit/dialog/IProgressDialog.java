package com.fy.baselibrary.retrofit.dialog;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fy.baselibrary.R;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;

/**
 * 自定义对话框的dialog
 * Created by fangs on 2017/11/7.
 */
public class IProgressDialog {

    AppCompatActivity mContext;
    CommonDialog dialog;

    public IProgressDialog init(AppCompatActivity mContext) {
        this.mContext = mContext;

        return this;
    }

    /** 创建对话框 */
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

    public CommonDialog getDialog() {
        return dialog;
    }

    /** 显示对话框 */
    public void show(){
        if (null != dialog && null != mContext)
        dialog.show(mContext.getSupportFragmentManager());
    }

    /** 关闭对话框 */
    public void close() {
        if (null != dialog && null != mContext) {
            dialog.dismiss(false);
        }
    }
}
