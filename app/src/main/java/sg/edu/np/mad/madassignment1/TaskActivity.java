package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class TaskActivity extends DrawerBaseActivity{

    private String TAG = "Task Activity";

    //define activity binding
    ActivityTaskBinding activityTaskBinding;
    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Task> secondTaskList = new ArrayList<>();

    public String GLOBAL_PREF = "MyPrefs";

    private Spinner spinnerFilter;

    private int transparentBrown = Color.argb(30, 162, 149, 116);
    private int normalBrown = Color.argb(100, 162, 149, 116);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Navigate from navigation bar to activity
        //inflate according to activity binding to show
        activityTaskBinding = ActivityTaskBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityTaskBinding.getRoot());
        //set title
        allocateActivityTitle("Task");

        //define elements
        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        //define recyclerView
        RecyclerView recyclerView = findViewById(R.id.UncompletedTaskRecycleView);
        // button to clear all tasks
        ImageView clearAllTaskButton = findViewById(R.id.clearAllTaskButton);
        // floating button to go to addnewtask
        FloatingActionButton addNewTask = findViewById(R.id.addNewTaskButton);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        // define spinner dropdown for filter options
        spinnerFilter = findViewById(R.id.spinnerFilterDropDown);

        String[] alertTimes = getResources().getStringArray(R.array.filter_options);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alertTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        //fill taskList with db data
        taskList = dbHandler.getTaskData(user.getUserID());

        // toast to indicate to user there are no tasks
        if (taskList.size() == 0){
            Toast.makeText(this, "No Tasks", Toast.LENGTH_LONG).show();
        }

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        TaskRecyclerViewAdaptor mAdaptor = new TaskRecyclerViewAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        //For Swiping Menu
        TaskRecyclerTouchListener touchListener = new TaskRecyclerTouchListener(this, recyclerView);
        touchListener.setClickable(new TaskRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                //Toast.makeText(getApplicationContext(),taskList.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.edit_task, R.id.delete_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new TaskRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {

                        Task task = taskList.get(position);

                        switch (viewID){
                            case R.id.edit_task:
                                Bundle extras = new Bundle();
                                Intent myIntent = new Intent(TaskActivity.this, TaskEditActivity.class);
                                extras.putInt("task id", task.getId());
                                myIntent.putExtras(extras);
                                startActivity(myIntent);

                            case R.id.delete_task:
                                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                                builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dbHandler.deleteTask(task);

                                        //to "refresh" the change
                                        Intent myIntent = new Intent(TaskActivity.this, TaskActivity.class);
                                        startActivity(myIntent);

                                        //toast to indicate tasks successfully cleared
                                        Toast.makeText(TaskActivity.this, "Task Cleared", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Delete task");
                                alert.show();

                                //taskList.remove(position);
                                //mAdaptor.setTaskList(taskList);

                                mAdaptor.notifyDataSetChanged();
                                break;
                        }

                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);


        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String filterOption = adapterView.getItemAtPosition(i).toString();

                if (secondTaskList.size() != 0){
                    for (int j=0; j < secondTaskList.size(); j++){
                        taskList.add(secondTaskList.get(j));
                    }
                    secondTaskList.clear();
                    mAdaptor.notifyDataSetChanged();
                }
                else{
                    if (filterOption.matches("Default")){
                        Collections.sort(taskList, Task.TaskIdAscComparator);
                        mAdaptor.notifyDataSetChanged();
                        Log.v(TAG, "Selected " + filterOption);
                    }
                    else if (filterOption.matches("Task Id Desc")){
                        Collections.sort(taskList, Task.TaskIdDescComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Name Asc")){
                        Collections.sort(taskList, Task.TaskNameAscComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Name Desc")){
                        Collections.sort(taskList, Task.TaskNameDescComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Date Asc")){
                        Collections.sort(taskList, Task.TaskDateAscComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Date Desc")){
                        Collections.sort(taskList, Task.TaskDateDescComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button uncompletedTaskButton = findViewById(R.id.uncompletedTaskButton);
        uncompletedTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secondTaskList.size() != 0){
                    for (int j=0; j < secondTaskList.size(); j++){
                        taskList.add(secondTaskList.get(j));
                    }
                    secondTaskList.clear();
                    mAdaptor.notifyDataSetChanged();
                }
                for (int j=0; j < taskList.size(); j++){
                    if (taskList.get(j).getStatus() == 1){
                        secondTaskList.add(taskList.get(j));
                    }
                }
                for (int j=0; j<secondTaskList.size(); j++){
                    taskList.remove(secondTaskList.get(j));
                }
                mAdaptor.notifyDataSetChanged();
            }
        });

        Button completedTaskButton = findViewById(R.id.completedTaskButton);
        completedTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secondTaskList.size() != 0){
                    for (int j=0; j < secondTaskList.size(); j++){
                        taskList.add(secondTaskList.get(j));
                    }
                    secondTaskList.clear();
                    mAdaptor.notifyDataSetChanged();
                }
                for (int j=0; j < taskList.size(); j++){
                    if (taskList.get(j).getStatus() == 0){
                        secondTaskList.add(taskList.get(j));
                    }
                }
                for (int j=0; j<secondTaskList.size(); j++){
                    taskList.remove(secondTaskList.get(j));
                }
                mAdaptor.notifyDataSetChanged();
            }
        });

        //clear all tasks listener
        clearAllTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setMessage("Warning! This action is irreversible. Are you sure you want to clear all tasks?").setCancelable(true);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete all task entries
                        dbHandler.deleteAllTask(user.getUserID());
                        //replace taskList with empty dbHandler
                        taskList = dbHandler.getTaskData(user.getUserID());
                        Log.v("GOD",String.valueOf(taskList.size()));
                        //force clear adaptor
                        mAdaptor.clear();

                        //toast to indicate tasks successfully cleared
                        Toast.makeText(TaskActivity.this, "Tasks Cleared", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setTitle("Clear all tasks");
                alert.show();
            }
        });

        //add new task listener
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to AddNewTaskActivity
                Intent TaskActivityToAddNewTaskActivity = new Intent(TaskActivity.this, TaskAddNewActivity.class);

                //put extra
                TaskActivityToAddNewTaskActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        TaskActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(TaskActivityToAddNewTaskActivity,1);

            }
        });

    }
}