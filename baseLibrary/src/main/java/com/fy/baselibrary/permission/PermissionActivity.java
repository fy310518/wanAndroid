package com.fy.baselibrary.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.fy.baselibrary.R;
import com.fy.baselibrary.application.IBaseActivity;
import com.fy.baselibrary.base.ViewHolder;
import com.fy.baselibrary.base.dialog.CommonDialog;
import com.fy.baselibrary.base.dialog.DialogConvertListener;
import com.fy.baselibrary.base.dialog.NiceDialog;
import com.fy.baselibrary.statusbar.MdStatusBar;
import com.fy.baselibrary.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 需要优化 比如 询问框
 * https://github.com/KCrason/PermissionGranted
 * Created by github on 2017/8/17.
 */
public class PermissionActivity extends AppCompatActivity implements IBaseActivity, View.OnClickListener {

    /** 权限请求 状态码 */
    private final static int PERMISSION_REQUEST_CODE = 0x01;
    /** 权限请求成功 状态码 */
    public final static int CALL_BACK_RESULT_CODE_SUCCESS = 0x02;
    /** 权限请求失败 状态码*/
    public final static int CALL_BACK_RESULE_CODE_FAILURE = 0x03;
    /** 跳转至权限请求界面 requestcode */
    public final static int CALL_BACK_PERMISSION_REQUEST_CODE = 0x04;

    public final static String KEY_PERMISSIONS_ARRAY = "key_permission_array";

    public final static String KEY_FIRST_MESSAGE = "key_first_message";

    public final static String KEY_ALWAYS_MESSAGE = "key_always_message";

    /** 第一次拒绝该权限的提示信息。 */
    private String mFirstRefuseMessage;
    /** 永久拒绝权限提醒的提示信息 */
    private String mAlwaysRefuseMessage;

    private String[] mPermissions;

    private boolean isToSettingPermission;

    @Override
    public void onClick(View v) {}

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
        MdStatusBar.setTransparentBar(activity, R.color.transparent, R.color.transparent, true);
    }

    @Override
    public void initData(Activity activity, Bundle savedInstanceState) {
        mFirstRefuseMessage = getString(R.string.defaule_always_message);

        Intent intent = getIntent();
        if (null != intent) {
            mPermissions = getIntent().getExtras().getStringArray(KEY_PERMISSIONS_ARRAY);
            mFirstRefuseMessage = getIntent().getStringExtra(KEY_FIRST_MESSAGE);
            mAlwaysRefuseMessage = getIntent().getStringExtra(KEY_ALWAYS_MESSAGE);
        }

        if (TextUtils.isEmpty(mFirstRefuseMessage)) {
            mFirstRefuseMessage = ResourceUtils.getStr(R.string.defaule_first_message);
        }
        if (TextUtils.isEmpty(mAlwaysRefuseMessage)) {
            mAlwaysRefuseMessage = ResourceUtils.getStr(R.string.defaule_always_message);
        }

        checkPermission(mPermissions);
    }

    /** 请求多个权限 */
    public void checkPermission(String... permissions) {
        if (null != permissions) {
            List<String> requestPermissionCount = getRequestPermissionList(permissions);
            if (null != requestPermissionCount && requestPermissionCount.size() > 0) {
                ActivityCompat.requestPermissions(this, requestPermissionCount.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            } else {
                permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS);
            }
        } else {
            permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS);
        }
    }

    /**
     * 获取需要去申请权限的权限列表
     *
     * @param permissions
     * @return
     */
    public List<String> getRequestPermissionList(String... permissions) {
        List<String> reequestPermissionCount = new ArrayList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                reequestPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果是从权限设置界面过来，重新检查权限
        if (isToSettingPermission) {
            isToSettingPermission = false;
            checkPermission(mPermissions);
        }
    }

    /**
     * 权限请求结束
     * @param resultCode
     */
    public void permissionEnd(int resultCode) {
        setResult(resultCode);
        finish();
    }

    /**
     * 调用系统弹窗请求权限
     * @param isRefuse
     */
    public void onSurePermission(boolean isRefuse) {
        if (isRefuse) {
            isToSettingPermission = true;
            Intent localIntent = new Intent();
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(localIntent);
        } else {
            checkPermission(mPermissions);
        }
    }

    /**
     * 循环获取 被用户拒绝的权限列表，
     * @return
     */
    public List<String> getshouldRationaleList() {
        if (null != mPermissions) {
            List<String> strings = new ArrayList<>();
            for (String permission : mPermissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    strings.add(permission);
                }
            }
            return strings;
        }
        return null;
    }

    public void showPermissionDialog(final boolean isAlwaysRefuse) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_permission)
                .setDialogConvertListener(new DialogConvertListener() {
                    @Override
                    protected void convertView(ViewHolder holder, CommonDialog dialog) {
                        holder.setText(R.id.tvPermissionTitle, R.string.dialog_title);
                        holder.setText(R.id.tvPermissionDescribe, isAlwaysRefuse ? mAlwaysRefuseMessage : mFirstRefuseMessage);

                        holder.setText(R.id.tvpermissionConfirm, isAlwaysRefuse ? R.string.set : R.string.ok);
                        holder.setOnClickListener(R.id.tvpermissionConfirm, v -> onSurePermission(isAlwaysRefuse));

                        holder.setText(R.id.tvPermissionCancel, R.string.cancel);
                        holder.setOnClickListener(R.id.tvPermissionCancel, v -> permissionEnd(CALL_BACK_RESULE_CODE_FAILURE));
                    }
                })
                .setWidthPercent(CommonDialog.WidthPercent)
                .show(getSupportFragmentManager());
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

            if (failurePermissionCount.size() == 0) {
                //全部成功
                permissionEnd(CALL_BACK_RESULT_CODE_SUCCESS);
            } else {
                //失败
                List<String> rationaleList = getshouldRationaleList();
                if (null != rationaleList && rationaleList.size() > 0) {
                    showPermissionDialog(false);
                } else {
                    showPermissionDialog(true);
                }
            }
        }
    }
}

