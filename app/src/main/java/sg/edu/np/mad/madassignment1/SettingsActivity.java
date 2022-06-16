package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;

import sg.edu.np.mad.madassignment1.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    //define activity binding
    ActivitySettingsBinding activitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activitySettingsBinding.getRoot());
        //set title
        allocateActivityTitle("Settings");



    }
}