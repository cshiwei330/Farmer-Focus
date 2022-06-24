package sg.edu.np.mad.madassignment1;


import android.os.Bundle;

import sg.edu.np.mad.madassignment1.databinding.ActivityComingSoonBinding;

public class ComingSoonActivity extends DrawerBaseActivity {

    ActivityComingSoonBinding activityComingSoonBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityComingSoonBinding = ActivityComingSoonBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityComingSoonBinding.getRoot());
        //set title
        allocateActivityTitle("Home");
    }
}