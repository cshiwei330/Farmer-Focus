package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.mad.madassignment1.databinding.ActivityAccountSettingsBinding;

public class AccountSettingsActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityAccountSettingsBinding activityAccountSettingsBinding;

    public String getString;
    public static final String SHARED_PREF = "shared";
    public static final String TEXT = "text";
    public String text;

    public String GLOBAL_PREF = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityAccountSettingsBinding = ActivityAccountSettingsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityAccountSettingsBinding.getRoot());
        //set title
        allocateActivityTitle("Settings");

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        setContentView(R.layout.activity_account_settings);
        Button myButtonSave = findViewById(R.id.SaveBtn);
        //Button myButtonCancel = findViewById(R.id.BackBtn);
        TextView myUserName = findViewById(R.id.Nickname);
        TextView editText = findViewById(R.id.EditAccountUsername);
        TextView myPassword = findViewById(R.id.ChangePass);
        ImageView myProfilePic = findViewById(R.id.ProfilePic);

        // shared preferences to store latest username to set profile pic
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");

        int[] imageList = new int [] {R.drawable.android, R.drawable.a3, R.drawable.farmer, R.drawable.a1};

        User userDBdata = dbHandler.findUser(username);
       //ImageView myProfilePic = new ImageView(R.id.ProfilePic);

        //if user has not a default profile pic, set profile pic to image
        if (userDBdata.getImageID() != 0){
            myProfilePic.setImageResource(imageList[userDBdata.getImageID()-1]);
        }
        else{
            myProfilePic.setImageResource(R.drawable.profile);
        }

        //go to images activity
        myProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Go to Images Activity
                Intent AccountSettingsActivityToImagesActivity = new Intent(AccountSettingsActivity.this, ImagesActivity.class);

                //put extra
                AccountSettingsActivityToImagesActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        AccountSettingsActivity.this.finish();
                    }
                });

                //start activity with result
                startActivityForResult(AccountSettingsActivityToImagesActivity,1);
            }
        });

        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go back to Settings
                //Intent AccountSettingsToSettingsActivity = new Intent(AccountSettingsActivity.this, SettingsActivity.class);

                //put extra
//                AccountSettingsToSettingsActivity.putExtra("finisher", new ResultReceiver(null) {
//                    @Override
//                    //when result code =1, received from bundle, kill this activity
//                    protected void onReceiveResult(int resultCode, Bundle resultData) {
//                        AccountSettingsActivity.this.finish();
//                    }
//                });

                getString = editText.getText().toString();
                myUserName.setText(getString);

                // shared preferences
                Log.v("SP", "test");
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT, myUserName.getText().toString());
                editor.apply();

                // Display "Saved" msg
                Toast.makeText(AccountSettingsActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                //start activity with result
                //startActivityForResult(AccountSettingsToSettingsActivity,1);

                //kill this activity
                //finish();
            }
        });

        //Display saved nickname
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        myUserName.setText(text);

        // Navigate to PasswordActivity
        myPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to Password
                Intent AccountSettingsToPasswordActivity = new Intent(AccountSettingsActivity.this, PasswordActivity.class);

                //put extra
                AccountSettingsToPasswordActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        AccountSettingsActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(AccountSettingsToPasswordActivity,1);
            }
        });


    }
}