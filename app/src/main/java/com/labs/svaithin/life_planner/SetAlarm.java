package com.labs.svaithin.life_planner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Calendar;


/**
 * Created by Siddharth on 21/11/17.
 */

public class SetAlarm {

    private TaskDbHelper mHelper;
    private String hour,minute;
    Context context1;
    List<String> myList;;
    SQLiteDatabase db;


    public SetAlarm( Context context) {

        mHelper = new TaskDbHelper(context);
        db = mHelper.getReadableDatabase();
        context1 = context;


    }

    public void setNextAlarm(long notifyID){

        Cursor cursor = db.query(TaskContract.TaskEntry.NOTIFINAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.DAYOFWEEK,
                        TaskContract.TaskEntry.HOUR, TaskContract.TaskEntry.MINUTE},
                "" + TaskContract.TaskEntry._ID + " = ?", new String[]{Long.toString(notifyID)}, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.HOUR);
            hour = cursor.getString(idx);
            int idm = cursor.getColumnIndex(TaskContract.TaskEntry.MINUTE);
            minute = cursor.getString(idm);
            int idday = cursor.getColumnIndex(TaskContract.TaskEntry.DAYOFWEEK);
            String Days = cursor.getString(idday);
            String T = "true";
            if (Days != null) {
                String s = Days.substring(1, Days.length() - 1);
                myList = new ArrayList<String>(Arrays.asList(s.split(",")));
                Log.d("Siddddd", "Days" + myList);
            }

        }

        //Get what day today
        int todate =Calendar.getInstance().get(Calendar.DATE);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        Log.d("Today",""+today+"Hour: "+h+":"+m + "from db"+hour+":"+minute);
        Intent notificationIntent = new Intent(context1, NotificationPublisher.class);
        notificationIntent.putExtra("Title","LifePlanner1");
        notificationIntent.putExtra("Content","LifePlanner1");
        notificationIntent.putExtra("notifyid",Long.toString(notifyID));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context1.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // If there is an alarm today set that alarm
        if((myList.get(today-1).replaceAll("\\s+", "").equals("true")) ){
            if((Integer.parseInt(hour) > h)|| ((Integer.parseInt(hour) == h) && (Integer.parseInt(minute) > m))){

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                calendar.set(Calendar.SECOND,0);
                AlarmManager alarmManager = (AlarmManager)context1.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                Log.d("SetAlarm","setNextAlarm");
                return;
            }
        }


        for(int i =2; i < 8; i++){
            if((myList.get(today-i).replaceAll("\\s+", "").equals("true")) ){

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE,todate+(i-1));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
                calendar.set(Calendar.SECOND,0);
                AlarmManager alarmManager = (AlarmManager)context1.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                Log.d("SetAlarm","inside set loop");
                return;
            }
        }

        Log.d("SetAlarm","setNextAlarm failed");

/* This is test so commenting as of now
        Intent notificationIntent = new Intent(context1, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra("Title","LifePlanner1");
        notificationIntent.putExtra("Content","LifePlanner1");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context1.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + 1000;
        AlarmManager alarmManager = (AlarmManager)context1.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        Log.d("SetAlarm","setNextAlarm");
        */

    }

}
