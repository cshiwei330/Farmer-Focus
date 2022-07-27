package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import sg.edu.np.mad.madassignment1.databinding.ActivityStatisticsBinding;

public class StatisticsActivity extends DrawerBaseActivity {
    ActivityStatisticsBinding activityStatisticsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityStatisticsBinding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityStatisticsBinding.getRoot());
        //set title
        allocateActivityTitle("Statistics");
    }
}