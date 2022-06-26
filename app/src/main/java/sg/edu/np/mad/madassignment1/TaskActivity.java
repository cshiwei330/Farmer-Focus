package sg.edu.np.mad.madassignment1;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class TaskActivity extends DrawerBaseActivity{

    //define activity binding
    ActivityTaskBinding activityTaskBinding;
    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();

    public String GLOBAL_PREF = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        RecyclerView recyclerView = findViewById(R.id.toDoListRecycleView);
        // button to clear all tasks
        Button clearAllTaskButton = findViewById(R.id.clearAllTaskButton);
        // floating button to go to addnewtask
        FloatingActionButton addNewTask = findViewById(R.id.addNewTaskButton);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //fill taskList with db data
        taskList = dbHandler.getTaskData(user.getUserID());


        //set textview to show number of tasks
        setTotalTaskTextView(taskList);

        // toast to indicate to user there are no tasks
        if (taskList.size() == 0){
            Toast.makeText(this, "No Tasks", Toast.LENGTH_LONG).show();
        }

        // initialize recyclerview
        //set adaptor to MyAdaptor, given taskList
        MyAdaptor mAdaptor = new MyAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        //clear all tasks listener
        clearAllTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //delete all task entries
                dbHandler.deleteAllTask();
                //replace taskList with empty dbHandler
                taskList = dbHandler.getTaskData(user.getUserID());
                Log.v("GOD",String.valueOf(taskList.size()));
                //force clear adaptor
                mAdaptor.clear();

                //set textview to show number of tasks
                setTotalTaskTextView(taskList);

                //toast to indicate tasks successfully cleared
                Toast.makeText(TaskActivity.this, "Tasks Cleared", Toast.LENGTH_LONG).show();
            }
        });

        //add new task listener
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to AddNewTaskActivity
                Intent TaskActivityToAddNewTaskActivity = new Intent(TaskActivity.this,AddNewTaskActivity.class);

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

    public void setTotalTaskTextView(ArrayList<Task> taskList){

        //define totalTask textview
        TextView totalTask = findViewById(R.id.totalTasks);

        //set  text to total number of tasks
        String totalTaskText = "Total: " + String.valueOf(taskList.size());
        totalTask.setText(totalTaskText);
    }
}