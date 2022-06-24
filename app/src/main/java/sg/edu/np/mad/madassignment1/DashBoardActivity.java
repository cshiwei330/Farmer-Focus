package sg.edu.np.mad.madassignment1;

import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivityDashBoardBinding;

public class DashBoardActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityDashBoardBinding activityDashBoardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityDashBoardBinding.getRoot());
        //set title
        allocateActivityTitle("Dashboard");
    }
}