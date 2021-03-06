package com.fy.wanandroid.request;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.retrofit.observer.IProgressDialog;
import com.fy.baselibrary.utils.drawable.ShapeBuilder;
import com.fy.wanandroid.R;

/**
 * 根据请求框架实现 请求等待对话框
 * Created by fangs on 2018/9/11 09:25.
 */
public class NetDialog extends IProgressDialog {

    /**
     * 初始化 网络请求等待对话框
     * @param mContext 环境（AppCompatActivity or v4.app.Fragment）
     * @return 等待对话框
     */
    public IProgressDialog init(Object mContext) {
        this.obj = mContext;
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
                            ShapeBuilder.create()
                                    .solid(R.color.white)
                                    .stroke(2, R.color.stroke)
                                    .radius(24)
                                    .setBackBg(holder.getView(R.id.loadDialogLayout));

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
