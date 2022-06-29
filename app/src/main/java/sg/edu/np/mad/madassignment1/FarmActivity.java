package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityFarmBinding;
import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class FarmActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityFarmBinding activityFarmBinding;
    //define taskList array
    ArrayList<Task> taskList = new ArrayList<>();

    public String GLOBAL_PREF = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Navigate from navigation bar to activity
        //inflate according to activity binding to show
        activityFarmBinding = ActivityFarmBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityFarmBinding.getRoot());
        //set title
        allocateActivityTitle("My Farm");
    }
}
