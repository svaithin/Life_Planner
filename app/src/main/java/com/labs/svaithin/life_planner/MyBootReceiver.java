package com.labs.svaithin.life_planner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Siddharth on 28/11/17.
 */

public class MyBootReceiver extends BroadcastReceiver {

    private TaskDbHelper mHelper;
    private String hour,minute;

    List<String> myList;
    SQLiteDatabase db;

    public void onReceive(Context context, Intent intent) {
        Log.d("SIDDDDDDD", "onReceive");
        Cursor cursor = db.query(TaskContract.TaskEntry.NOTIFINAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.DAYOFWEEK,
                        TaskContract.TaskEntry.HOUR, TaskContract.TaskEntry.MINUTE},null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            int id = cursor.getInt(idx);
            int idy = cursor.getColumnIndex(TaskContract.TaskEntry.HOUR);
            String h = cursor.getString(idy);
            Log.d("SIDDDDDDD", "onReceive"+h);
            SetAlarm setalm = new SetAlarm(context);
            setalm.setNextAlarm(id);
        }

    }
}
