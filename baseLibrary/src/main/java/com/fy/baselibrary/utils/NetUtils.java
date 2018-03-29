package com.fy.baselibrary.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 网络相关的工具类
 * <p/>
 * Created by fangs on 2017/3/1.
 */
public class NetUtils {

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前的网络状态 -1：没有网络 1：WIFI网络 2：wap网络 3：net网络
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;

        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 把File集合转化成MultipartBody.Part 集合（retrofit 多文件文件上传）
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> fileToMultipartBodyParts(List<String> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (String path : files) {
            File file = new File(path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件

            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 把File集合转化成MultipartBody.Part 集合（retrofit 多文件文件上传）
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> fileToMultipartBodyPart(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件
            String path = file.getPath();
            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 用于把File 集合对象转化成MultipartBody （retrofit 多文件文件上传）
     * @param files
     * @return
     */
    public static MultipartBody filesToMultipartBody(List<String> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (String path : files) {
            File file = new File(path);//访问手机端的文件资源，保证手机端sdcdrd中必须有这个文件

            String fileStr = path.substring(path.lastIndexOf(".") + 1);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/" + fileStr), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return multipartBody;
    }
}
