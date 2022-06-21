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

    public String getString;
    public static final String SHARED_PREF = "shared";
    public static final String TEXT = "text";
    public String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_settings);
        Button myButtonSave = findViewById(R.id.SaveBtn);
        Button myButtonCancel = findViewById(R.id.BackBtn);
        TextView myUserName = findViewById(R.id.Nickname);
        TextView editText = findViewById(R.id.EditAccountUsername);
        TextView myPassword = findViewById(R.id.ChangePass);
        ImageView myProfilePic = findViewById(R.id.ProfilePic);


        myProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to Images Activity
                Intent AccountSettingsActivityToImagesActivity = new Intent(AccountSettingsActivity.this, ImagesActivity.class);
                startActivity(AccountSettingsActivityToImagesActivity);


                //myProfilePic.setImageResource(R.drawable.img1);


//                switch(view.getId())
//                {
//                    case R.id.Image1:
//                        myProfilePic.setImageResource(R.drawable.img1);
//                        break;
//                }
//                if(tag == R.drawable.img1){
//                    myProfilePic.setImageResource(R.drawable.img1);
//                    myProfilePic.setTag(R.drawable.img1);
//                }
            }
        });

        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go back to Settings
                Intent AccountSettingsToSettingsActivity = new Intent(AccountSettingsActivity.this, SettingsActivity.class);

                //put extra
                AccountSettingsToSettingsActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        AccountSettingsActivity.this.finish();
                    }
                });

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
                startActivityForResult(AccountSettingsToSettingsActivity,1);

                //kill this activity
                finish();
            }
        });

        //Display saved nickname
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
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

        // Navigate back to settings if cancel is pressed
        myButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to Password
                Intent AccountSettingsToSettingsActivity2 = new Intent(AccountSettingsActivity.this, SettingsActivity.class);

                //put extra
                AccountSettingsToSettingsActivity2.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        AccountSettingsActivity.this.finish();
                    }
                });
                // Display "Back" msg
                Toast.makeText(AccountSettingsActivity.this, "Back to Settings", Toast.LENGTH_SHORT).show();

                //start activity with result
                startActivityForResult(AccountSettingsToSettingsActivity2,1);
            }
        });

    }
}