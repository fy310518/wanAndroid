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
public abstract class IProgressDialog {

    protected AppCompatActivity mContext;
    protected CommonDialog dialog;

    /**
     * 创建对话框 子类实现此方法
     * @param msg
     * @return
     */
    public abstract IProgressDialog setDialogMsg(int msg);

    public CommonDialog getDialog() {
        return dialog;
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (null != dialog && null != mContext)
            dialog.show(mContext.getSupportFragmentManager());
    }

    /**
     * 关闭对话框
     */
    public void close() {
        if (null != dialog && null != mContext) {
            dialog.dismiss(false);
        }
    }
}
