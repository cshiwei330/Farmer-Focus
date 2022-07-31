/*
STATISTICS ACTIVITY
This activity holds all the fragments of statistics related items. It also has the cards to display the total number of completed and pending
tasks. The main goal of the statistics is to boost the user's motivation and let them track their progress.
 */

package sg.edu.np.mad.madassignment1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityStatisticsBinding;

public class StatisticsActivity extends DrawerBaseActivity {
    ActivityStatisticsBinding activityStatisticsBinding;
    // defining the DBHandler
    DBHandler dbHandler = new DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate according to activity binding to show
        activityStatisticsBinding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        // set view to this activity
        setContentView(activityStatisticsBinding.getRoot());
        // set title
        allocateActivityTitle("Statistics");

        // shared preferences to get userid
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        // creating a task list
        ArrayList<Task> taskList = dbHandler.getTaskData(user.getUserID());
        // initialising the variables to use
        int completedTasks = 0;
        int pendingTasks = 0;
        for(int i = 0; i < taskList.size(); i++){
            // if the task status is saved as completed
            if(taskList.get(i).getStatus() == 1) {
                // counting the number of completed tasks
                completedTasks ++;
            }
            // if the task status is saved as incomplete
            else{
                // counting the number of incomplete tasks
                pendingTasks ++;
            }
        }

        // setting the texts of the cards displaying how many completed / incomplete tasks there are
        TextView completed = findViewById(R.id.NoOfCompletedTasks);
        completed.setText(String.valueOf(completedTasks));
        TextView pending = findViewById(R.id.NoOfPendingTasks);
        pending.setText(String.valueOf(pendingTasks));
    }
}