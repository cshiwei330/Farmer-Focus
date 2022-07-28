package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimerTaskListActivity extends AppCompatActivity {
    public String GLOBAL_PREF = "MyPrefs";

    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Task> secondTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_task_list);

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // getting stored username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //define recyclerView
        RecyclerView recyclerView = findViewById(R.id.UncompletedTaskRecycleView);

        //Finding uncompleted tasks
        //define taskList array
        ArrayList<Task> taskList = new ArrayList<>();
        //current taskList with db data
        taskList = dbHandler.getTaskData(user.getUserID());
        //use method
        //ArrayList<Task> recentTaskList = findRecentTasks(taskList);
        ArrayList<Task> uncompletedTaskList = findUncompletedTasks(taskList);

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        TimerRecyclerViewAdaptor mAdaptor = new TimerRecyclerViewAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        // navigate back to timer
        ImageView backBtn = findViewById(R.id.backToTimer2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent TimerTaskListActivityToTimerActivity = new Intent(TimerTaskListActivity.this, TimerActivity.class);
                //put extra
                TimerTaskListActivityToTimerActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        TimerTaskListActivity.this.finish();
                    }
                });

                // Display "Back" msg
                Toast.makeText(TimerTaskListActivity.this, "Back to Timer", Toast.LENGTH_SHORT).show();

                //start activity with result
                startActivityForResult(TimerTaskListActivityToTimerActivity, 1);
            }
        });
    }

    //NEW
    public ArrayList<Task> findRecentTasks (ArrayList<Task> taskList){

        ArrayList<Task> recentTaskList = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++){ //loop through current taskList to find tasks that are upcoming (within the following week)
            Task task = taskList.get(i);

            boolean result = withinAWeek(task.getTaskDate()); //check if task date is within a week

            if (result){
                recentTaskList.add(task); //if true then add to new list
            }

        }

        return recentTaskList; //display this list in the recyclerView
    }

    public boolean withinAWeek (String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
            Date dateToValidate = sdf.parse(date); //convert the string to a Date

            // current date with same formatting as dateToValidate
            Date today = sdf.parse(sdf.format(new Date()));

            // current date after 1 week
            Calendar currentDateAfter1Week = Calendar.getInstance();
            currentDateAfter1Week.add(Calendar.DAY_OF_MONTH, 7);

            //.getTime() returns a Date so comparison can be made
            if (dateToValidate.after(today) && dateToValidate.before(currentDateAfter1Week.getTime())){ //define the date range
                //ok everything is fine, date in range
                return true;
            }
            else if (dateToValidate.compareTo(today)==0){ //tasks that are today are considered within a week
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Task> findUncompletedTasks (ArrayList<Task> taskList){

        ArrayList<Task> uncompletedTaskList = new ArrayList<>();

        if (secondTaskList.size() != 0){
            for (int j=0; j < secondTaskList.size(); j++){
                taskList.add(secondTaskList.get(j));
            }
            secondTaskList.clear();
        }
        for (int j=0; j < taskList.size(); j++){
            if (taskList.get(j).getStatus() == 1){
                secondTaskList.add(taskList.get(j));
            }
        }
        for (int j=0; j<secondTaskList.size(); j++){
            taskList.remove(secondTaskList.get(j));
        }

        return uncompletedTaskList; //display this list in the recyclerView
    }
}