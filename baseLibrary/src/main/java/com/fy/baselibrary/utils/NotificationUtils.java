package com.fy.baselibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.fy.baselibrary.R;

/**
 * 通知 工具类
 * Created by fangs on 2018/3/18.
 */
public class NotificationUtils {

    public static final String ChannelStr1 = "chat";
    public static final String ChannelStr2 = "subscribe";

    public static final int ChannelId1 = 1;
    public static final int ChannelId2 = 2;


    private NotificationUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 创建一个通知渠道(此方法一般在 发送通知消息前执行)
     * @param ctx
     * @param channelId     渠道id
     * @param channelName   渠道名称
     * @param importance    渠道重要级别
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
     * @param channelId
     */
    public static void manageNotificationChannel(Activity act, String channelId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = manager.getNotificationChannel(channelId);

            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Bundle bundle = new Bundle();
                bundle.putString(Settings.EXTRA_APP_PACKAGE, AppUtils.getLocalPackageName());
                bundle.putString(Settings.EXTRA_CHANNEL_ID, channelId);
                JumpUtils.jump(act, Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS, bundle);

                T.showLong("请手动将通知打开");
            }
        }
    }

    /**
     * 模拟 发送一条通知
     * @param act
     * @param actClass          点击通知 跳转的 目标activity（可空）
     * @param channelStrId      消息渠道id
     * @param msgTitle          消息标题
     * @param msgContent        消息内容
     */
    public static void sendSubscribeMsg(Activity act, Class actClass, String channelStrId, String msgTitle, String msgContent) {
        PendingIntent pendingIntent;
        if (null != actClass) {
            Intent intent = new Intent(act, actClass);
            pendingIntent = PendingIntent.getActivity(act, 0, intent, 0);
        } else {
            pendingIntent = getDefalutIntent(act, Notification.FLAG_AUTO_CANCEL);
        }

        NotificationManager manager = (NotificationManager) act.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(act, channelStrId)
                .setContentTitle(msgTitle)
                .setContentText(msgContent)
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.image_circular)
                .setLargeIcon(BitmapFactory.decodeResource(act.getResources(), R.mipmap.image_circular))
                .setAutoCancel(true)
                .build();

        assert manager != null;
        manager.notify(getChannelId(channelStrId), notification);
    }

    private static int getChannelId(@NonNull String channelStrid){
        if (channelStrid.equals(ChannelStr1)){
            return ChannelId1;
        } else {
            return ChannelId2;
        }
    }

    public static PendingIntent getDefalutIntent(Activity act, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(act, 1, new Intent(), flags);
        return pendingIntent;
    }
}
