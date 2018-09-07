package com.fy.baselibrary.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.fy.baselibrary.R;
import com.fy.baselibrary.aop.annotation.NeedPermission;

/**
 * 通知 工具类
 * Created by fangs on 2018/3/18.
 */
public class NotificationUtils {

    private NotificationUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 创建一个通知渠道(此方法一般在 发送通知消息前执行, 或者再 启动页 等待的时间执行)
     * @param ctx
     * @param channelId     渠道id
     * @param channelName   渠道名称
     * @param importance    渠道重要级别 (NotificationManager.IMPORTANCE_DEFAULT)
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context ctx, String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 管理通知渠道（用户关闭了某个通知渠道，可通过此方法进入系统 通知渠道管理界面，提示用户打开对应的通知渠道）
     * @param act
     * @param channelName
     */
    public static void manageNotificationChannel(Activity act, String channelName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(channelName);

            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Bundle bundle = new Bundle();
                bundle.putString(Settings.EXTRA_APP_PACKAGE, AppUtils.getLocalPackageName());
                bundle.putString(Settings.EXTRA_CHANNEL_ID, channelName);
                JumpUtils.jump(act, Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS, bundle);

                T.showLong("请手动将通知打开");
            }
        }
    }

    /**
     * 发送一条通知
     * @param act
     * @param actClass       点击通知 跳转的 目标activity（可空）
     * @param fyBuild        通知关键数据 包装类
     */
    @NeedPermission(value = {Manifest.permission.VIBRATE})
    public static void sendSubscribeMsg(Activity act, Class actClass, FyBuild fyBuild) {

        manageNotificationChannel(act, fyBuild.channelName);

        PendingIntent pendingIntent;
        if (null != actClass) {
            Intent intent = new Intent(act, actClass);
            pendingIntent = PendingIntent.getActivity(act, 0, intent, 0);
        } else {
            pendingIntent = getDefalutIntent(act, Notification.FLAG_AUTO_CANCEL);
        }

        NotificationManager manager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(act, fyBuild.channelName)
                .setContentTitle(fyBuild.msgTitle)
                .setContentText(fyBuild.msgContent)
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(fyBuild.icon)
                .setLargeIcon(BitmapFactory.decodeResource(act.getResources(), fyBuild.icon))
                .setColor(ResUtils.getColor(fyBuild.iconBgColor))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND|
                        Notification.DEFAULT_VIBRATE|
                        Notification.DEFAULT_LIGHTS|
                        Notification.FLAG_ONLY_ALERT_ONCE)//系统默认声音、震动、呼吸灯（只响应一次）
                .build();

        assert manager != null;
        manager.notify(fyBuild.channelId, notification);
    }

    public static PendingIntent getDefalutIntent(Activity act, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(act, 1, new Intent(), flags);
        return pendingIntent;
    }


    public static class FyBuild {
        /** 通知渠道名称 */
        public String channelName;
        /** 通知渠道 Id */
        public int channelId;

        /** 通知 图标（Android从5.0以上 通知 icon 只使用alpha图层来进行绘制，而不应该包括RGB图层） */
        public int icon;
        /** 通知图标 背景颜色 */
        public int iconBgColor;
        /** 通知标题 */
        public String msgTitle;
        /** 通知内容 */
        public String msgContent;

        public static FyBuild init() {
            return new FyBuild();
        }

        /**
         * 发送通知
         * @param act
         * @param actClass
         */
        public void sendNotify(Activity act, Class actClass){
            NotificationUtils.sendSubscribeMsg(act, actClass, this);
        }


        public FyBuild setChannel(int channelId, String channelName) {
            this.channelName = channelName;
            this.channelId = channelId;
            return this;
        }

        public FyBuild setIcon(@DrawableRes int icon, @ColorRes int iconBgColor) {
            this.icon = icon;
            this.iconBgColor = iconBgColor;
            return this;
        }


        public FyBuild setMsgTitle(String msgTitle) {
            this.msgTitle = msgTitle;
            return this;
        }

        public FyBuild setMsgContent(String msgContent) {
            this.msgContent = msgContent;
            return this;
        }
    }
}
