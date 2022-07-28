package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityStatisticsBinding;

public class StatisticsActivity extends DrawerBaseActivity {
    ActivityStatisticsBinding activityStatisticsBinding;
    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityStatisticsBinding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityStatisticsBinding.getRoot());
        //set title
        allocateActivityTitle("Statistics");

        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        ArrayList<Task> taskList = (ArrayList<Task>) dbHandler.getTaskData(user.getUserID());
        int completedTasks = 0;
        int pendingTasks = 0;
        for(int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getStatus() == 1) {
                completedTasks ++;
            }
            else{
                pendingTasks ++;
            }
        }

        TextView completed = findViewById(R.id.NoOfCompletedTasks);
        completed.setText(String.valueOf(completedTasks));
        TextView pending = findViewById(R.id.NoOfPendingTasks);
        pending.setText(String.valueOf(pendingTasks));
    }
}