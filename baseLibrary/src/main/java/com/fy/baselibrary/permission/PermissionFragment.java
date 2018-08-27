package com.fy.baselibrary.permission;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.JumpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 统一 权限管理
 * 参考 https://github.com/KCrason/PermissionGranted
 * Created by fangs on 2018/8/10 09:57.
 */
public class PermissionFragment extends AppCompatActivity implements IBaseActivity {

    public final static String KEY_PERMISSIONS_ARRAY = "key_permission_array";

    public final static String KEY_FIRST_MESSAGE = "key_first_message";

    public final static String KEY_ALWAYS_MESSAGE = "key_always_message";

    /** 权限请求 状态码 */
    private final static int PERMISSION_REQUEST_CODE = 0x01;

    /** 权限请求成功 状态码 */
    public final static int CALL_BACK_RESULT_CODE_SUCCESS = 0x02;
    /** 权限请求失败 状态码*/
    public final static int CALL_BACK_RESULE_CODE_FAILURE = 0x03;

    /** 第一次拒绝该权限的提示信息。 */
    private String mFirstRefuseMessage;
    /** 永久拒绝权限提醒的提示信息 */
    private String mAlwaysRefuseMessage;

    private String[] mPermissions;

    private boolean isToSettingPermission;

    private static OnPermission call;

    @Override
    public void reTry() {}

    @Override
    public boolean isShowHeadView() {
        return false;
    }

    @Override
    public int setView() {
        return 0;
    }

    @Override
    public void setStatusBar(Activity activity) {
        MdStatusBar.statusAlpha = 0;
        MdStatusBar.navAlpha = 0;
        MdStatusBar.setColorBar(activity, R.color.transparent, R.color.transparent);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mFirstRefuseMessage = getString(R.string.defaule_always_message);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mPermissions = bundle.getStringArray(KEY_PERMISSIONS_ARRAY);
            mFirstRefuseMessage = bundle.getString(KEY_FIRST_MESSAGE);
            mAlwaysRefuseMessage = bundle.getString(KEY_ALWAYS_MESSAGE);
        }

        if (TextUtils.isEmpty(mFirstRefuseMessage)) {
            mFirstRefuseMessage = getString(R.string.defaule_first_message);
        }
        if (TextUtils.isEmpty(mAlwaysRefuseMessage)) {
            mAlwaysRefuseMessage = getString(R.string.defaule_always_message);
        }

        checkPermission(mPermissions);
    }

    @Override
    public void onResume() {
        super.onResume();
        //如果是从权限设置界面过来，重新检查权限
        if (isToSettingPermission) {
            isToSettingPermission = false;
            checkPermission(mPermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && null != grantResults && grantResults.length > 0) {
            List<Integer> failurePermissionCount = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    failurePermissionCount.add(grantResults[i]);
                }
            }

            if (failurePermissionCount.size() == 0) {//权限请求失败数为0，则全部成功
                permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS, true);
            } else {
                //失败
                List<String> rationaleList = PermissionUtils.getShouldRationaleList(this, mPermissions);
                if (null != rationaleList && rationaleList.size() > 0) {
                    if (rationaleList.size() < mPermissions.length){
                        showPermissionDialog(false, false);
                    } else {
                        showPermissionDialog(false, true);//全部拒绝
                    }
                } else {
                    showPermissionDialog(true, true);
                }
            }
        }
    }

    /** 请求多个权限 */
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermission(String... permissions) {
        if (null != permissions) {
            List<String> requestPermissionCount = PermissionUtils.getRequestPermissionList(this, permissions);
            if (null != requestPermissionCount && requestPermissionCount.size() > 0) {
                requestPermissions(requestPermissionCount.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            } else {
                permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS, true);
            }
        } else {
            permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS, true);
        }
    }


    /**
     * 调用系统弹窗请求权限
     * @param isRefuse
     */
    public void onSurePermission(boolean isRefuse) {
        if (isRefuse) {
            isToSettingPermission = true;
            JumpUtils.jumpSettting(this, Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        } else {
            checkPermission(mPermissions);
        }
    }

    /**
     * 弹窗自定义弹窗 给予用户提示
     * @param isAlwaysRefuse
     * @param isAllSuccess      权限请求是否 全部成功
     */
    public void showPermissionDialog(final boolean isAlwaysRefuse, boolean isAllSuccess) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_permission)
                .setDialogConvertListener(new DialogConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, CommonDialog dialog) {
                        holder.setText(R.id.tvPermissionTitle, R.string.dialog_title);
                        holder.setText(R.id.tvPermissionDescribe, isAlwaysRefuse ? mAlwaysRefuseMessage : mFirstRefuseMessage);

                        holder.setText(R.id.tvpermissionConfirm, isAlwaysRefuse ? R.string.set : R.string.ok);
                        holder.setOnClickListener(R.id.tvpermissionConfirm, v -> {
                                    onSurePermission(isAlwaysRefuse);
                                    dialog.dismiss(false);
                                });

                        holder.setText(R.id.tvPermissionCancel, R.string.cancel);
                        holder.setOnClickListener(R.id.tvPermissionCancel, v -> {
                            permissionEnd(CALL_BACK_RESULE_CODE_FAILURE, isAllSuccess);
                            dialog.dismiss(false);
                        });
                    }
                })
                .setWidthPercent(CommonDialog.WidthPercent)
                .show(getSupportFragmentManager());
    }

    /**
     * 权限请求结束
     * @param resultCode
     * @param isStatus      是否全部成功或者是否全部失败（根据第一个参数判断：如参数1 表示“成功”状态码，则参数2表示 是否全部成功）
     */
    public void permissionEnd(int resultCode, boolean isStatus) {
        if (null != call){
            if (resultCode == CALL_BACK_RESULT_CODE_SUCCESS && isStatus) {
                call.hasPermission(Arrays.asList(mPermissions), isStatus);
            } else if (resultCode == CALL_BACK_RESULE_CODE_FAILURE && isStatus){
                call.noPermission(Arrays.asList(mPermissions));
            } else {
                call.hasPermission(PermissionUtils.getRequestPermissionList(this, mPermissions), isStatus);
            }
        }

        JumpUtils.exitActivity(this);
    }


    /**
     * 准备请求权限
     * @param object
     * @param permissions
     * @return
     */
    public static void newInstant(Object object, String[] permissions, OnPermission callListener) {
        call = callListener;

        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_PERMISSIONS_ARRAY, permissions);

        Intent intent = new Intent();
        intent.putExtras(bundle);

        if (object instanceof AppCompatActivity) {
            Activity act = ((Activity)object);
            intent.setClass(act, PermissionFragment.class);

            act.startActivity(intent);
        } else if (object instanceof Fragment) {
            Activity act = ((Fragment)object).getActivity();
            intent.setClass(act, PermissionFragment.class);

            act.startActivity(intent);
        } else if (object instanceof Service){
            intent.setClass((Service) object, PermissionFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((Service) object).startActivity(intent);
        }
    }


    /**
     * 权限管理 回调接口
     */
    public interface OnPermission {
        /**
         * 有权限被授予时回调（部分或全部授予）
         *
         * @param denyList    请求失败的权限组
         * @param isAll       是否全部授予了
         */
        void hasPermission(List<String> denyList, boolean isAll);

        /**
         * 权限被全部拒绝时回调
         *
         * @param denyList 请求失败的权限组
         */
        void noPermission(List<String> denyList);
    }
}
