package com.labs.svaithin.life_planner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

/**
 * Created by Siddharth on 17/11/17.
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        Notification.Builder builder = new Notification.Builder(context);


        String title = intent.getStringExtra("Title");
        String content = intent.getStringExtra("Content");
        int notificationid = intent.getIntExtra("notifyid",0);
        Log.d("Siddddd","Inside alarm"+title+" "+ content);

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Resources res = context.getResources();
        Notification.Builder builder1 = new Notification.Builder(context);


        builder.setContentIntent(contentIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(content);


        if (Build.VERSION.SDK_INT < 16) {
            nm.notify(notificationid, builder.getNotification());
        } else {
            nm.notify(notificationid, builder.build());
        }

    }
}