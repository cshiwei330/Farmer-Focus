package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class TaskActivity extends DrawerBaseActivity{

    private String TAG = "Task Activity";

    //define activity binding
    ActivityTaskBinding activityTaskBinding;
    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Task> secondTaskList = new ArrayList<>();

    private String filterOption;

    public String GLOBAL_PREF = "MyPrefs";

    private Spinner spinnerFilter;

    private boolean completedTaskButtonClicked = false;
    private boolean uncompletedTaskButtonClicked = false;

    AlarmManager alarmManager;

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
                    public void onSwipeOptionClicked(int viewID, int position) throws ParseException {

                        Task task = taskList.get(position);

                        switch (viewID){
                            case R.id.edit_task:
                                Bundle extras = new Bundle();
                                Intent myIntent = new Intent(TaskActivity.this, TaskEditActivity.class);
                                extras.putInt("Task id", task.getId());
                                myIntent.putExtras(extras);
                                startActivity(myIntent);

                                // update widget
                                updateWidgets(getApplicationContext(), dbHandler, user, task);

                                break;

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
                                        Toast.makeText(TaskActivity.this, "Deleted Task", Toast.LENGTH_LONG).show();

                                        // update widget
                                        try {
                                            updateWidgets(getApplicationContext(), dbHandler, user, task);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
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
                filterOption = adapterView.getItemAtPosition(i).toString();

                if (secondTaskList.size() != 0){
                    for (int j=0; j < secondTaskList.size(); j++){
                        taskList.add(secondTaskList.get(j));
                    }
                    secondTaskList.clear();
                    mAdaptor.notifyDataSetChanged();
                }
                else{
                    if (filterOption.matches("Default (Date Asc)")){
                        Collections.sort(taskList, Task.TaskDateAscComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Date Desc")){
                        Collections.sort(taskList, Task.TaskDateDescComparator);
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
                    else if (filterOption.matches("Task Creation Date Asc")){
                        Collections.sort(taskList, Task.TaskIdAscComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Task Creation Date Desc")){
                        Collections.sort(taskList, Task.TaskIdDescComparator);
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Event Tasks")){
                        if (secondTaskList.size() != 0){
                            for (int j=0; j < secondTaskList.size(); j++){
                                taskList.add(secondTaskList.get(j));
                            }
                            secondTaskList.clear();
                            mAdaptor.notifyDataSetChanged();
                        }
                        for (int j=0; j < taskList.size(); j++){
                            if (taskList.get(j).getTaskType().matches("Recurring")){
                                secondTaskList.add(taskList.get(j));
                            }
                        }
                        for (int j=0; j<secondTaskList.size(); j++){
                            taskList.remove(secondTaskList.get(j));
                        }
                        mAdaptor.notifyDataSetChanged();
                    }
                    else if (filterOption.matches("Recurring Tasks")){
                        if (secondTaskList.size() != 0){
                            for (int j=0; j < secondTaskList.size(); j++){
                                taskList.add(secondTaskList.get(j));
                            }
                            secondTaskList.clear();
                            mAdaptor.notifyDataSetChanged();
                        }
                        for (int j=0; j < taskList.size(); j++){
                            if (taskList.get(j).getTaskType().matches("Event")){
                                secondTaskList.add(taskList.get(j));
                            }
                        }
                        for (int j=0; j<secondTaskList.size(); j++){
                            taskList.remove(secondTaskList.get(j));
                        }
                        mAdaptor.notifyDataSetChanged();
                    }
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button uncompletedTaskButton = findViewById(R.id.uncompletedTaskButton);
        Button completedTaskButton = findViewById(R.id.completedTaskButton);

        if (completedTaskButtonClicked == false && uncompletedTaskButtonClicked == false){
            uncompletedTaskButtonClicked = true;
            completedTaskButtonClicked = false;

            uncompletedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));
            completedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));
        }

        uncompletedTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uncompletedTaskButtonClicked = true;
                completedTaskButtonClicked = false;

                uncompletedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonClicked));
                completedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));

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

        completedTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uncompletedTaskButtonClicked = false;
                completedTaskButtonClicked = true;

                uncompletedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));
                completedTaskButton.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonClicked));

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

                        for (int j=0; j<taskList.size(); j++) {
                            cancelNotification(taskList.get(j));
                        }

                        //delete all task entries
                        dbHandler.deleteAllTask(user.getUserID());
                        //replace taskList with empty dbHandler
                        taskList = dbHandler.getTaskData(user.getUserID());
                        //force clear adaptor
                        mAdaptor.clear();

                        //toast to indicate tasks successfully cleared
                        Toast.makeText(TaskActivity.this, "Tasks Cleared", Toast.LENGTH_LONG).show();

                        // update widget
                        Context context = getApplicationContext();
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

                        RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
                        try {
                            mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(dbHandler.getTodayTaskData(user.getUserID()).size())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        appWidgetManager.updateAppWidget(thisWidget, mainView);
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

    public void updateWidgets (Context context, DBHandler dbHandler, User user, Task task) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
        // current date with same formatting as task.getDate()
        Date today = sdf.parse(sdf.format(new Date()));
        Date dateToValidate = sdf.parse(task.getTaskDate()); //convert the string to a Date


        //update the widget if task date is today
        if (dateToValidate.compareTo(today)==0){

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

            RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
            try {
                mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(dbHandler.getTodayTaskData(user.getUserID()).size())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            appWidgetManager.updateAppWidget(thisWidget, mainView);

        }
    }

    public void cancelNotification(Task t) {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AlarmReceiver.class);
        extras.putString("task name", t.getTaskName());
        extras.putString("task alert", t.getAlert());
        intent.putExtras(extras);
        PendingIntent pending = PendingIntent.getBroadcast(this, t.getId(), intent, PendingIntent.FLAG_IMMUTABLE);
        // Cancel notification
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pending);

        NotificationManagerCompat.from(this).cancelAll();

    }
}