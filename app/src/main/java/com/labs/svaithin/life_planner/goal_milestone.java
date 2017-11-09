package com.labs.svaithin.life_planner;

/**
 * Created by Siddharth on 04/10/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.labs.svaithin.life_planner.db.TaskContract;
import com.labs.svaithin.life_planner.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Build.ID;

public class goal_milestone extends Fragment {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TaskDbHelper mHelper;
    private String TAG = "Milestone1activity";
    HashMap<Integer, Integer> map;
    View rootView;
    Integer goalID;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            UpdateUI();
            Log.d("receiver", "Got message: " + message);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.milestone_goal, container, false);

        //DB handler
        mHelper = new TaskDbHelper(getActivity());
        goalID = 0; //Need to change and get from extraHabit_tabbed myActivity = (Habit_tabbed) getActivity();
        Habit_tabbed myActivity = (Habit_tabbed) getActivity();
        goalID = myActivity.getGoalId();
        Log.d("Sidd", "Inside create view goalmilestone");


        //Update UI
        UpdateUI();

        //Setup Listner
        setupListViewListener();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name1"));



        return rootView;
    }

    //Show UI List
    public void UpdateUI(){
        lvItems = (ListView) rootView.findViewById(R.id.lvGoalMilestone);
        items = new ArrayList<String>();
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        map = new HashMap<Integer, Integer>();
        int row = 0;


        Cursor cursor = db.query(TaskContract.TaskEntry.GMILESTONE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.GMILESTONENAME,
                        TaskContract.TaskEntry.GMCOMPLETED,TaskContract.TaskEntry.GMGOALID},
                ""+TaskContract.TaskEntry.GMGOALID+" = ?",new String[]{goalID.toString()}, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.GMILESTONENAME);
            //mySimpleNewAdapter.add(cursor.getString(idx));
            taskList.add(cursor.getString(idx));
            Log.d("test1",cursor.getString(idx) );
            int idt = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            map.put(row, cursor.getInt(idt));
            row++;
            //Log.d(TAG, "row" + doneMap);

        }


        //Code for testing
        if(getActivity() != null) {
            itemsAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, items);
            lvItems.setAdapter(itemsAdapter);
            itemsAdapter.addAll(taskList);
        }
        db.close();
        //items.add("First Item");
        //items.add("Second Item");
    }


    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   final View item, final int pos, long id) {
                        final EditText taskEditText = new EditText(getActivity());

                        taskEditText.setText(items.get(pos));
                        taskEditText.setHint("Goal");
                        Log.d(TAG,"edit text:"+items.get(pos));
                        String completeButtonName = new String();

                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setView(taskEditText)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String task = String.valueOf(taskEditText.getText());
                                        if(!task.trim().isEmpty()) {

                                            SQLiteDatabase update_db = mHelper.getWritableDatabase();


                                            update_db.execSQL("update " + TaskContract.TaskEntry.GMILESTONE +
                                                    " set " + TaskContract.TaskEntry.GMILESTONENAME + " = '" +
                                                    task.toString() + "' where _id = " + map.get(pos));

                                            UpdateUI();
                                            update_db.close();
                                        }

                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Log.d("AlertDialog", "Negative");
                                        SQLiteDatabase remove_db = mHelper.getWritableDatabase();
                                        remove_db.execSQL("delete from " + TaskContract.TaskEntry.GMILESTONE +
                                                " where _id =" + map.get(pos));
                                        remove_db.close();
                                        UpdateUI();
                                    }
                                })
                                .show();

                        TextView dialogTitle = (TextView) dialog.findViewById(android.R.id.title);
                        Log.d(TAG,"dialogtitle"+dialogTitle);
                        Button save = (Button) dialog.findViewById(android.R.id.button1);
                        Button delete = (Button) dialog.findViewById(android.R.id.button2);
                        Button completed = (Button) dialog.findViewById(android.R.id.button3);


                        return true;
                    }

                });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        //Intent intent = new Intent(getActivity(), test_goal.class);
                        Intent intent = new Intent(getActivity(), Notify.class);
                        Log.d("SIDDDDD:", ""+map.get(pos) );
                        intent.putExtra("ID", map.get(pos));
                        startActivity(intent);

                    }

                });
    }

}
