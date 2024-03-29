package sg.edu.np.mad.madassignment1;

import android.content.Intent;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import sg.edu.np.mad.madassignment1.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    public ImageView chicken;

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


        //for chicken animation
        ImageView chicken = findViewById(R.id.lazychicken);
        Glide.with(this).load(R.drawable.lazychicken).into(chicken);

        // image button to go to account settings
        ImageButton myButtonSettings = findViewById(R.id.AccSettingsBtn);
        //add new task listener
        myButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Settings", "test1");
                Intent SettingsActivityToAccountSettingsActivity = new Intent(SettingsActivity.this, AccountSettingsActivity.class);
                //startActivity(SettingsActivityToAccountSettingsActivity);
                //put extra
                SettingsActivityToAccountSettingsActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        SettingsActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(SettingsActivityToAccountSettingsActivity, 1);
            }
        });
        Log.v("Settings", "test2");

    }

}