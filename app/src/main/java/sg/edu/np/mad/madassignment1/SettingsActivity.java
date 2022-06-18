package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.ImageButton;

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

        // floating button to go to account settings
        ImageButton accountsettings = findViewById(R.id.AccSettingsBtn);

        //add new task listener
        accountsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to AccountSettings
                Intent SettingsActivityToAccountSettings = new Intent(SettingsActivity.this, AccountSettingsActivity.class);

                //put extra
                SettingsActivityToAccountSettings.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        SettingsActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(SettingsActivityToAccountSettings,1);

            }
        });
    }
}