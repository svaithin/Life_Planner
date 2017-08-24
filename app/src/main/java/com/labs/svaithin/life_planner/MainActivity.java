package com.labs.svaithin.life_planner;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import java.util.ArrayList;

import static com.labs.svaithin.life_planner.R.id.lvItems;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TaskDbHelper mHelper;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        //Custom Code start here

        //DB handler
        mHelper = new TaskDbHelper(this);


        UpdateUI();

    }

    //Show UI List
    void UpdateUI(){
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(TaskContract.TaskEntry.PLAN,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.PLANNAME,
                        TaskContract.TaskEntry.PCOMPLETED}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.PLANNAME);
            //mySimpleNewAdapter.add(cursor.getString(idx));
            taskList.add(cursor.getString(idx));

            //Log.d(TAG, "row" + doneMap);

        }

        //Code for testing
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        itemsAdapter.addAll(taskList);
        //items.add("First Item");
        //items.add("Second Item");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // This is the code for floating add button
    public void onFabClick(){

        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add a Plan")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.PLANNAME, task);
                        values.put(TaskContract.TaskEntry.PCOMPLETED, 0);
                        values.put(TaskContract.TaskEntry.PARCHIVED, 0);
                        db.insertWithOnConflict(TaskContract.TaskEntry.PLAN,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        UpdateUI();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }



}
