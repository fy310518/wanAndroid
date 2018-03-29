package com.fy.baselibrary.retrofit.dialog;

import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.base.dialog.CommonDialog;

/**
 * 自定义对话框的dialog</p>
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
    public IProgressDialog setDialogMsg(int msg){

        if (null == dialog) {
            dialog = DialogLoad.init()
                    .setMsg(mContext.getString(msg));
        }

        return this;
    }

    /** 创建对话框 */
    public IProgressDialog setDialogMsg(String msg){

        if (null == dialog) {
            dialog = DialogLoad.init()
                    .setMsg("正在登录...");
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
