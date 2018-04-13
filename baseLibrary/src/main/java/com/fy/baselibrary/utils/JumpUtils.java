package com.fy.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fy.baselibrary.R;

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
     * 跳转到指定 Action 的activity
     * @param act
     * @param action
     * @param bundle
     */
    public static void jump(AppCompatActivity act, String action, Bundle bundle) {
        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        intent.setAction(action);
        act.startActivity(intent);
    }

    /**
     * 跳转到指定 Action 的activity 带回调结果的跳转
     *
     * @param action    要跳转到的Activity
     * @param bundle
     * @param requestCode 请求码
     */
    public static void jump(AppCompatActivity act, String action, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setAction(action);
        act.startActivityForResult(intent, requestCode);
        //第一个参数 下一界面进入效果；第二个参数 当前界面退出效果
//        act.overridePendingTransition(R.anim.anim_slide_left_in, R.anim.anim_slide_left_out);
    }

    /**
     * 跳转到指定 activity
     *
     * @param actClass
     * @param bundle
     */
    public static void jump(AppCompatActivity act, Class actClass, Bundle bundle) {
        Intent intent = new Intent(act, actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        act.startActivity(intent);
    }

    /**
     * 跳转到指定 activity <br/> 带回调结果的跳转
     *
     * @param actClass    要跳转到的Activity
     * @param bundle
     * @param requestCode 请求码
     */
    public static void jump(AppCompatActivity act, Class actClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(act, actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 使用反射 跳转到指定 包路径的 activity
     * @param act
     * @param bundle
     * @param className
     */
    public static void jumpReflex(AppCompatActivity act, Bundle bundle, String className){
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
     */
    public static void jumpReflex(AppCompatActivity act, String className, Bundle bundle, int requestCode){
        try {
            Class cla = Class.forName(className);
            jump(act, cla, bundle, requestCode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出当前activity 并带数据回到上一个Activity
     * @param act
     * @param bundle
     */
    public static void jumpResult(AppCompatActivity act, Bundle bundle){
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
    public static void exitActivity(AppCompatActivity act) {
        act.finish();
    }

    /**
     * 退出整个应用
     * @param act
     */
    public static void exitApp(AppCompatActivity act, Class actClass){
        Intent intent = new Intent(act, actClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //注意
        act.startActivity(intent);
        act.finish();
    }

//  一.只嵌套了一层Fragment（比如activity中使用了viewPager，viewPager中添加了几个Fragment） 在这种情况下要注意几个点：
//  1.在Fragment中使用startActivityForResult的时候，不要使用getActivity().startActivityForResult,而是应该直接使startActivityForResult()。
//  2.如果activity中重写了onActivityResult，那么activity中的onActivityResult一定要加上：
//      super.onActivityResult(requestCode, resultCode, data)。
//  如果违反了上面两种情况，那么onActivityResult只能够传递到activity中的，无法传递到Fragment中的。
//  没有违反上面两种情况的前提下，可以直接在Fragment中使用startActivityForResult和onActivityResult，和在activity中使用的一样。
    /**
     * 从fragment 跳转到指定的 activity; 带回调结果的跳转
     * @param act
     * @param bundle
     * @param actClass
     */
    public static void jump(Fragment act, Class actClass, Bundle bundle) {
        Intent intent = new Intent(act.getContext(), actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        act.startActivity(intent);
    }

    /**
     * 从fragment 跳转到指定的 activity; 带回调结果的跳转
     * @param act
     * @param actClass
     * @param bundle
     * @param requestCode
     */
    public static void jump(Fragment act, Class actClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(act.getContext(), actClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }

        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 使用反射 从fragment 跳转到指定 包路径的 activity; 带回调结果的跳转
     * @param act
     * @param bundle
     * @param className
     * @param requestCode
     */
    public static void jump(Fragment act, Bundle bundle, String className, int requestCode){
        try {
            Class cla = Class.forName(className);
            Intent intent = new Intent(act.getContext(), cla);
            if (null != bundle) {
                intent.putExtras(bundle);
            }

            act.startActivityForResult(intent, requestCode);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 安装apk
     * @param file
     */
    public static void install(AppCompatActivity act, File file) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        act.finish();
        act.startActivity(intent);
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

    /**
     * 跳转到浏览器 打开指定 URL链接
     * @param act
     * @param url
     */
    public static void jump(AppCompatActivity act, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        act.startActivity(intent);
    }



}
