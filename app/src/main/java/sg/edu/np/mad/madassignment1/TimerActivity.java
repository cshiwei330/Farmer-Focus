package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivitySettingsBinding;
import sg.edu.np.mad.madassignment1.databinding.ActivityTimerBinding;

public class TimerActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityTimerBinding activityTimerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityTimerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityTimerBinding.getRoot());
        //set title
        allocateActivityTitle("Timer");
    }
}