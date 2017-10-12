package com.labs.svaithin.life_planner;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Notify extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute,timeset;
    TextView clock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
               //Test
                Log.d("alertdialog!!!!","Clicked OK");
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
}
