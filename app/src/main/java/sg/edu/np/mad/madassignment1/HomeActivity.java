package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivityHomeBinding;

public class HomeActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityHomeBinding.getRoot());
        //set title
        allocateActivityTitle("Home");
    }
}