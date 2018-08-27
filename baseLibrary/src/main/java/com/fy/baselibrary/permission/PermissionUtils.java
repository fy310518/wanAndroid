package com.fy.baselibrary.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.retrofit.ServerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限相关工具类
 * Created by fangs on 2018/8/10 15:07.
 */
public class PermissionUtils {

    private PermissionUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取需要去申请权限的权限列表
     * @param permissions
     * @return
     */
    public static List<String> getRequestPermissionList(Context context, String[] permissions) {
        List<String> reequestPermissionCount = new ArrayList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                reequestPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

    /**
     * 检查是否需要提示用户对该权限的授权进行说明，返回boolean类型
     * @return
     */
    public static List<String> getShouldRationaleList(AppCompatActivity activity, String... permissions) {
        if (null != permissions) {
            List<String> strings = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    strings.add(permission);
                }
            }
            return strings;
        }
        return null;
    }

    /**
     * 检测权限有没有在清单文件中注册
     * @param activity           Activity对象
     * @param requestPermissions 请求的权限组
     */
    public static void checkPermissions(Activity activity, List<String> requestPermissions) {
        List<String> manifest = getManifestPermissions(activity);
        if (manifest != null && manifest.size() != 0) {
            for (String permission : requestPermissions) {
                if (!manifest.contains(permission)) {
                    throw new ServerException(permission, 100);
                }
            }
        } else {
            throw new ServerException("清单文件没有注册对应的权限", 100);
        }
    }

    /**
     * 返回应用程序在清单文件中注册的权限
     */
    public static List<String> getManifestPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return Arrays.asList(pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (Exception e) {
            return null;
        }
    }
}
