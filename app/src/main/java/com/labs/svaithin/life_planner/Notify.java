package com.labs.svaithin.life_planner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.icu.text.DateFormat;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import static android.R.attr.delay;
import static android.R.attr.format;
import static android.R.attr.id;
import static android.os.Build.VERSION_CODES.N;
import static com.labs.svaithin.life_planner.R.id.lvItems;
import static com.labs.svaithin.life_planner.R.id.lvListNotify;

public class Notify extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute,timeset;
    private ArrayList<String> items,time,days;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TaskDbHelper mHelper;
    private String TAG = "Mainactivity";
    HashMap<Integer, Integer> map;
    TextView clock;
    Integer habitID;
    long notifyid;
    ArrayList<DataModel> dataModels;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHelper = new TaskDbHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Intent intent = getIntent();
        habitID = intent.getIntExtra("ID",0);
        UpdateUI();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void onFabClick(){

        final EditText taskEditText = new EditText(this);
        clock = new TextView(this);

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String dateString = sdf.format(date);
        SimpleDateFormat hour = new SimpleDateFormat("HH");
        mHour = Integer.parseInt(hour.format(date));
        SimpleDateFormat format = new SimpleDateFormat("mm");
        mMinute = Integer.parseInt(format.format(date));
        Log.d("Siddddd",""+mHour+":"+mMinute);
        clock.setText(dateString);
        clock.setTypeface(Typeface.DEFAULT_BOLD);
        clock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        clock.setGravity(Gravity.CENTER |Gravity.TOP);

        //Setting Listener for Clock.
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling Time picker
                timepicker();

            }
        });

        //start of testing
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setView(clock);
        final String[] Colors = new String[]{
                "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"
        };
        final ArrayList<Integer> selectedItems = new ArrayList<Integer>();
        final boolean[] preCheckedItems = new boolean[]{
                true,true,true,true,true,true,true
        };
        adb.setMultiChoiceItems(Colors, preCheckedItems, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked){

                if(isChecked)
                {

                    selectedItems.add(which);
                }
                else if(selectedItems.contains(which))
                {

                    selectedItems.remove(which);
                }
            }
        });

        //Define the AlertDialog positive/ok/yes button
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){

                String checkedtext =(Arrays.toString(preCheckedItems)) ;

               //Test
                Log.d("alertdialog!!!!","Clicked OK"+checkedtext+mHour+mMinute+"which"+which);
                //SQLiteDatabase update_db = mHelper.getWritableDatabase();

                //this need to go
                //which =1;


                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.HOUR, String.valueOf(mHour));
                values.put(TaskContract.TaskEntry.MINUTE, String.valueOf(mMinute));
                values.put(TaskContract.TaskEntry.DAYOFWEEK, checkedtext);
                values.put(TaskContract.TaskEntry.HABITID, habitID);
                notifyid = db.insertWithOnConflict(TaskContract.TaskEntry.NOTIFINAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();
                Log.d("dbinsertid", ""+notifyid);


                SetAlarm setalm = new SetAlarm(Notify.this);
                setalm.setNextAlarm(notifyid);
                UpdateUI();

                Log.d("Sidddddd","set alarm");
            }
        });

        //Define the Neutral/Cancel button in AlertDialog
        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //When user click the neutral/cancel button from alert dialog
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();

        //!!!!!!!!!!!!!!!End of test

            /*AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(clock)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();*/

    }
    

    public void timepicker(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Snackbar.make(view, "time picked", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Log.d("Timepicker","hour" +hourOfDay + "min" +minute);
                        mHour = hourOfDay;
                        mMinute = minute;
                        int test = hourOfDay%12;
                        if(test < 1){
                            if(minute>10) {
                                clock.setText(hourOfDay + ":" + minute + " AM");
                            }else{
                                clock.setText(hourOfDay + ":0" + minute + " AM");
                            }
                        }
                        else {
                            if(minute>10) {
                                clock.setText(hourOfDay%12 + ":" + minute + " PM");
                            }else{
                                clock.setText(hourOfDay%12 + ":0" + minute + " PM");
                            }
                        }

                        //clock.setText(hourOfDay+":"+minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    //Show UI List
    void UpdateUI(){
        lvItems = (ListView) findViewById(lvListNotify);
        items = new ArrayList<String>();
        time = new ArrayList<String>();
        days = new ArrayList<String>();
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> alarmdays = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        map = new HashMap<Integer, Integer>();
        int row = 0;
        List<String> myList;
        String displayDays = "";

        dataModels= new ArrayList<>();
        Cursor cursor = db.query(TaskContract.TaskEntry.NOTIFINAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.DAYOFWEEK,
                        TaskContract.TaskEntry.HOUR,TaskContract.TaskEntry.MINUTE},
                ""+TaskContract.TaskEntry.HABITID+" = ?",new String[]{habitID.toString()}, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.HOUR);
            String h = cursor.getString(idx);
            int idm = cursor.getColumnIndex(TaskContract.TaskEntry.MINUTE);
            String m = cursor.getString(idm);
            //mySimpleNewAdapter.add(cursor.getString(idx));
            int test = Integer.parseInt(h)%12;
            String clock1;
            if(test < 1){
                if(Integer.parseInt(m)>9) {
                    clock1 = h + ":" + m + " AM";
                }else{
                    clock1 =h + ":0" + m + " AM";
                }
            }
            else {
                if(Integer.parseInt(m)>9) {
                    clock1 = Integer.parseInt(h)%12 + ":" + m + " PM";
                }else{
                    clock1 =Integer.parseInt(h)%12+ ":0" + m + " PM";
                }
            }
            taskList.add(clock1);
            int idt = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            map.put(row, cursor.getInt(idt));
            int idday = cursor.getColumnIndex(TaskContract.TaskEntry.DAYOFWEEK);
            String Days = cursor.getString(idday);
            displayDays = "";
            String T = "true";
            if(Days != null) {
                String s = Days.substring(1, Days.length() - 1);
                myList = new ArrayList<String>(Arrays.asList(s.split(",")));
                Log.d("Siddddd", "Days" + myList);


                if(T.equals(myList.get(0))){
                    displayDays = displayDays + "Sun    ";

                }
                Log.d("Sun", "Days" + myList);
                if(T.equals(myList.get(1).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Mon    ";
                    Log.d("Mon", "Days" + myList);
                }
                if(T.equals(myList.get(2).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Tue    ";
                    Log.d("Tue", "Days" + myList);
                }
                if(T.equals(myList.get(3).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Wed    ";
                    Log.d("Wed", "Days" + myList);
                }
                if(T.equals(myList.get(4).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Thus    ";
                    Log.d("Thus", "Days" + myList);
                }
                if(T.equals(myList.get(5).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Fri    ";
                    Log.d("Fri", "Days" + myList);
                }
                if(T.equals(myList.get(6).replaceAll("\\s+",""))){
                    displayDays = displayDays + "Sat    ";
                    Log.d("Sat", "Days" + myList);
                }

            }

            dataModels.add(new DataModel(clock1,displayDays));
            row++;
            //Log.d(TAG, "row" + getString(idx));

        }

        //Code for testing
        adapter= new CustomAdapter(dataModels,getApplicationContext());

        itemsAdapter = new ArrayAdapter<String>(this,
                R.layout.notify_list_adaptor,R.id.time, items);
        if(displayDays != null){

        }
        lvItems.setAdapter(adapter);
        //lvItems.setAdapter(itemsAdapter);
        itemsAdapter.addAll(taskList);
        //items.add("First Item");
        //items.add("Second Item");
    }
}
