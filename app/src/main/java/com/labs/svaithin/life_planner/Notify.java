package com.labs.svaithin.life_planner;

import android.app.TimePickerDialog;
import android.content.ContentValues;
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

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import static com.labs.svaithin.life_planner.R.id.lvItems;
import static com.labs.svaithin.life_planner.R.id.lvListNotify;

public class Notify extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute,timeset;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TaskDbHelper mHelper;
    private String TAG = "Mainactivity";
    HashMap<Integer, Integer> map;
    TextView clock;
    Integer habitID;
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
                which =1;


                SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.HOUR, String.valueOf(mHour));
                values.put(TaskContract.TaskEntry.MINUTE, String.valueOf(mMinute));
                values.put(TaskContract.TaskEntry.HABITID, habitID);
                db.insertWithOnConflict(TaskContract.TaskEntry.NOTIFINAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                db.close();


                UpdateUI();
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
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        map = new HashMap<Integer, Integer>();
        int row = 0;

        Cursor cursor = db.query(TaskContract.TaskEntry.NOTIFINAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.DAYOFWEEK,
                        TaskContract.TaskEntry.HOUR,TaskContract.TaskEntry.MINUTE},
                ""+TaskContract.TaskEntry.HABITID+" = ?",new String[]{habitID.toString()}, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.HOUR);
            //mySimpleNewAdapter.add(cursor.getString(idx));
            taskList.add(cursor.getString(idx));
            int idt = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            map.put(row, cursor.getInt(idt));
            row++;
            //Log.d(TAG, "row" + getString(idx));

        }

        //Code for testing
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        itemsAdapter.addAll(taskList);
        //items.add("First Item");
        //items.add("Second Item");
    }
}
