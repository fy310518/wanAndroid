package com.fy.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import com.fy.baselibrary.aop.resultfilter.ActResultManager;
import com.fy.baselibrary.aop.resultfilter.ResultCallBack;

import java.io.File;

/**
 * 界面跳转工具类
 * Created by fangs on 2017/5/9.
 */
public class JumpUtils {

    private JumpUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 跳转到指定 activity
     * @param actClass
     * @param bundle
     */
    public static void jump(Activity act, Class actClass, Bundle bundle) {
        Intent intent = new Intent(act, actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        act.startActivity(intent);
//        第一个参数 下一界面进入效果；第二个参数 当前界面退出效果
//        act.overridePendingTransition(R.anim.anim_slide_left_in, R.anim.anim_slide_left_out);
    }

    /**
     * 跳转到指定 Action 的activity
     * @param act
     * @param action
     * @param bundle
     */
    public static void jump(Activity act, String action, Bundle bundle) {
        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        intent.setAction(action);
        act.startActivity(intent);
    }

    /**
     * 跳转到指定 Action 的activity 带回调结果的跳转
     * @param action    要跳转到的Activity
     * @param bundle
     * @param requestCode 请求码
     * @param callBack 回调结果，回调接口
     */
    public static void jump(Activity act, String action, Bundle bundle, int requestCode, ResultCallBack callBack) {
        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setAction(action);

        ActResultManager.getInstance()
                .startActivityForResult(act, intent, requestCode, callBack);
    }

    /**
     * 跳转到指定 activity  带回调结果的跳转
     * @param actClass    要跳转到的Activity
     * @param bundle
     * @param requestCode 请求码
     * @param callBack    回调结果，回调接口
     */
    public static void jump(Activity act, Class actClass, Bundle bundle, int requestCode, ResultCallBack callBack) {
        Intent intent = new Intent(act, actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        ActResultManager.getInstance()
                .startActivityForResult(act, intent, requestCode, callBack);
    }


// 传统方式 fragment 通过startActivityForResult() 启动新的activity 有接收不到返回结果的问题，解决方案如下：
//  一.只嵌套了一层Fragment（比如activity中使用了viewPager，viewPager中添加了几个Fragment） 在这种情况下要注意几个点：
//  1.在Fragment中使用startActivityForResult的时候，不要使用getActivity().startActivityForResult,而是应该直接使startActivityForResult()。
//  2.如果activity中重写了onActivityResult，那么activity中的onActivityResult一定要加上：
//      super.onActivityResult(requestCode, resultCode, data)。
//  如果违反了上面两种情况，那么onActivityResult只能够传递到activity中的，无法传递到Fragment中的。
//  没有违反上面两种情况的前提下，可以直接在Fragment中使用startActivityForResult和onActivityResult，和在activity中使用的一样。
//  二：使用aop 在Activity的onActivityResult() 执行之后，通过回调接口 获取返回结果
//      使用方式同 activity 跳转到指定 activity  带回调结果的跳转

    /**
     * 从fragment 跳转到指定的 activity; 带回调结果的跳转
     * @param fragment
     * @param actClass
     * @param bundle
     * @param requestCode
     */
    public static void jump(Fragment fragment, Class actClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        fragment.startActivityForResult(intent, requestCode, bundle);
    }

    /**
     * 使用反射 从fragment 跳转到指定 包路径的 activity; 带回调结果的跳转
     * @param fragment
     * @param bundle
     * @param className
     * @param requestCode
     */
    public static void jump(Fragment fragment, Bundle bundle, String className, int requestCode){
        try {
            Class cla = Class.forName(className);
            Intent intent = new Intent(fragment.getContext(), cla);
            if (null != bundle) {
                intent.putExtras(bundle);
            }

            fragment.startActivityForResult(intent,requestCode, bundle);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从fragment 跳转到指定的 activity
     * @param fragment
     * @param bundle
     * @param actClass
     */
    public static void jump(Fragment fragment, Class actClass, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        fragment.startActivity(intent);
    }


    /**
     * 使用反射 跳转到指定 包路径的 activity
     * @param act
     * @param bundle
     * @param className
     */
    public static void jumpReflex(Activity act, Bundle bundle, String className){
        try {
            Class cla = Class.forName(className);
            jump(act, cla, bundle);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用反射(reflex) 跳转到指定 包路径的 activity; 带回调结果的跳转
     * @param act
     * @param bundle
     * @param className
     * @param callBack 回调结果，回调接口
     */
    public static void jumpReflex(Activity act, String className, Bundle bundle, int requestCode, ResultCallBack callBack){
        try {
            Class cla = Class.forName(className);
            jump(act, cla, bundle, requestCode, callBack);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出当前activity 并带数据回到上一个Activity
     * @param act
     * @param bundle 可空
     */
    public static void jumpResult(Activity act, Bundle bundle){
        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        act.setResult(Activity.RESULT_OK, intent);
        act.finish();
    }

    /**
     * 退出当前activity
     */
    public static void exitActivity(Activity act) {
        act.finish();
    }

    /**
     * 返回桌面
     * @param act
     */
    public static void backDesktop(Activity act){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        act.startActivity(intent);
    }

    /**
     * 退出整个应用
     * @param act
     */
    public static void exitApp(Activity act, Class actClass){
        Intent intent = new Intent(act, actClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意
        intent.putExtra("exitApp", true);//添加标记
        act.startActivity(intent);
    }


    /**
     * 跳转到浏览器 打开指定 URL链接
     * @param act
     * @param url
     */
    public static void jump(Activity act, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        act.startActivity(intent);
    }

    /**
     * 跳转到 对应 action的设置界面
     * @param act     如：Settings.ACTION_APPLICATION_DETAILS_SETTINGS(权限设置)
     * @param action
     */
    public static void jumpSettting(Activity act, String action){
        Intent localIntent = new Intent();
        localIntent.setAction(action);
        localIntent.setData(Uri.fromParts("package", act.getPackageName(), null));
        act.startActivity(localIntent);
    }


    /**
     * 调用系统安装器安装apk(适配 Android 7.0 在应用间共享文件)
     *
     * @param context 上下文
     * @param file apk文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, AppUtils.getFileProviderName(), file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载软件
     *
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        if (AppUtils.isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }

}
