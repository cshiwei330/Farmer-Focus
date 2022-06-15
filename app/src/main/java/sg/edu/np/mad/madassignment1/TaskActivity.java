package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivityTaskBinding;

public class TaskActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityTaskBinding activityTaskBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityTaskBinding = ActivityTaskBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityTaskBinding.getRoot());
        //set title
        allocateActivityTitle("Task");
    }
}