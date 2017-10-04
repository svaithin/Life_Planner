package com.labs.svaithin.life_planner.db;

import android.database.SQLException;
import android.provider.BaseColumns;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * Created by Siddharth on 22/08/17.
 */

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.PLAN + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.PLANNAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.PARCHIVED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.PCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0);";

        db.execSQL(createTable);

        createTable = "CREATE TABLE " + TaskContract.TaskEntry.PMILESTONE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.PMILESTONENAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.PMCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.PMPLANID + " INTEGER," + " FOREIGN KEY (" +
                TaskContract.TaskEntry.PMPLANID+")REFERENCES " +
                TaskContract.TaskEntry.PLAN + "("+ TaskContract.TaskEntry._ID +"));";

        try {

            db.execSQL(createTable);
            Log.d("test1","createtabe");
        } catch (SQLException e) {
            Log.d("craetetable", e.getMessage());
        }

        createTable = "CREATE TABLE " + TaskContract.TaskEntry.GOAL + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.GOALNAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.GARCHIVED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.GCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.PLANID + " INTEGER," + " FOREIGN KEY (" +
                TaskContract.TaskEntry.PLANID+")REFERENCES " +
                TaskContract.TaskEntry.PLAN + "("+ TaskContract.TaskEntry._ID +"));";

        db.execSQL(createTable);

        createTable = "CREATE TABLE " + TaskContract.TaskEntry.GMILESTONE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.GMILESTONENAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.GMCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.GMGOALID + " INTEGER," + " FOREIGN KEY (" +
                TaskContract.TaskEntry.GMGOALID+")REFERENCES " +
                TaskContract.TaskEntry.GOAL + "("+ TaskContract.TaskEntry._ID +"));";

        db.execSQL(createTable);

        createTable = "CREATE TABLE " + TaskContract.TaskEntry.HABIT + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.HABITNAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.HARCHIVED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.HCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.GOALID + " INTEGER," + " FOREIGN KEY (" +
                TaskContract.TaskEntry.GOALID+")REFERENCES " +
                TaskContract.TaskEntry.GOAL + "("+ TaskContract.TaskEntry._ID +"));";

        db.execSQL(createTable);

        createTable = "CREATE TABLE " + TaskContract.TaskEntry.NOTIFICATION + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.NOTIFINAME + " TEXT NOT NULL," +
                TaskContract.TaskEntry.NARCHIVED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.NCOMPLETED + " BOOLEAN NOT NULL DEFAULT 0," +
                TaskContract.TaskEntry.HABITID + " INTEGER," + " FOREIGN KEY (" +
                TaskContract.TaskEntry.HABITID+")REFERENCES " +
                TaskContract.TaskEntry.HABIT + "("+ TaskContract.TaskEntry._ID +"));";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.GOAL);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.PLAN);
        onCreate(db);
    }
}
