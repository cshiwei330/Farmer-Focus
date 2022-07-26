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

import java.util.ArrayList;

public class TimerTaskListActivity extends AppCompatActivity {
    public String GLOBAL_PREF = "MyPrefs";

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
}