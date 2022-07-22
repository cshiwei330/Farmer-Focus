package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityRecurringTaskStatsBinding;

public class StatsRecurringTaskActivity extends DrawerBaseActivity {
    ActivityRecurringTaskStatsBinding activityRecurringTaskStatsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityRecurringTaskStatsBinding = ActivityRecurringTaskStatsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityRecurringTaskStatsBinding.getRoot());
        //set title
        allocateActivityTitle("Recurring Tasks");

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        ArrayList<Task> recurringTask = dbHandler.getTaskData(user.getUserID());
        ArrayList<String> taskNames = new ArrayList<>();
        for (int i = 0; i < recurringTask.size(); i++) {
            taskNames.add(recurringTask.get(i).getTaskName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, taskNames);
        AutoCompleteTextView searchTask = findViewById(R.id.searchTasks);
        searchTask.setAdapter(adapter);
    }
}
