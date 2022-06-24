package sg.edu.np.mad.madassignment1;

import android.content.Intent;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import sg.edu.np.mad.madassignment1.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    // Dark/Light mode
    private View mySettings;
    private Switch themeSwitch;
    private TextView titleTV, themeTV;
    private DarkMode mode;

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
//        initWidgets();
//        loadSharedPreferences();
//        initSwitchListener();

    }

//    private void initWidgets() {
//        titleTV = findViewById(R.id.titleTV);
//        mySettings = findViewById(R.id.mySettings);
//        themeSwitch = findViewById(R.id.themeSwitch);
//        themeTV = findViewById(R.id.themeTV);
//    }
//
//    private void loadSharedPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences(DarkMode.PREFERENCES, MODE_PRIVATE);
//        String theme = sharedPreferences.getString(DarkMode.CUSTOM_THEME, DarkMode.LIGHT_THEME);
//        mode.setCustomTheme(theme);
//        updateView();
//    }
//
//    private void initSwitchListener() {
//        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                if(checked)
//                    mode.setCustomTheme(DarkMode.DARK_THEME);
//                else
//                    mode.setCustomTheme(DarkMode.LIGHT_THEME);
//
//                SharedPreferences.Editor editor = getSharedPreferences(DarkMode.PREFERENCES, MODE_PRIVATE).edit();
//                editor.putString(DarkMode.CUSTOM_THEME, mode.getCustomTheme());
//                editor.apply();
//                updateView();
//            }
//
//        });
//
//    }
//
//    private void updateView() {
//        final int black = ContextCompat.getColor(this, R.color.black);
//        final int white = ContextCompat.getColor(this, R.color.white);
//
//        if(mode.getCustomTheme().equals(DarkMode.DARK_THEME)){
//            titleTV.setTextColor(white);
//            themeTV.setTextColor(white);
//            mySettings.setBackgroundColor(black);
//            themeSwitch.setChecked(true);
//        }
//        else{
//            titleTV.setTextColor(black);
//            themeTV.setTextColor(black);
//            mySettings.setBackgroundColor(white);
//            themeSwitch.setChecked(false);
//        }
//    }

}