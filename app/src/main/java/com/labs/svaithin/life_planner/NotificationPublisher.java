package com.labs.svaithin.life_planner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import static android.R.attr.id;

/**
 * Created by Siddharth on 17/11/17.
 */

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    private TaskDbHelper mHelper;
    SQLiteDatabase db;
    int id1;

    public void onReceive(Context context, Intent intent) {


        Notification.Builder builder = new Notification.Builder(context);


        String title = intent.getStringExtra("Title");
        String content = intent.getStringExtra("Content");
        String notificationid = intent.getStringExtra("notifyid");
        Log.d("next alarm","Inside alarm"+title+" "+ content+" " +notificationid);
        int notifyid = Integer.decode(notificationid);

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
            nm.notify(notifyid, builder.getNotification());
        } else {
            nm.notify(notifyid, builder.build());
        }

        //set next alarm

        mHelper = new TaskDbHelper(context);
        db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.NOTIFINAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.DAYOFWEEK,
                        TaskContract.TaskEntry.HOUR, TaskContract.TaskEntry.MINUTE},
                "" + TaskContract.TaskEntry._ID + " = ?", new String[]{notificationid}, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            id1 = cursor.getInt(idx);
        }
        Log.d("next alarm","setting next alarm notification"+id1);
        if (id1 > 0){
            SetAlarm setalm = new SetAlarm(context);
            setalm.setNextAlarm(notifyid);
            Log.d("next alarm","inside set loop");
        }

    }
}