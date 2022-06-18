package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingsActivity extends AppCompatActivity {

    public String getString;
    public static final String SHARED_PREF = "shared";
    public static final String TEXT = "text";
    public String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Button myButtonSave = findViewById(R.id.SaveBtn);
        Button myButtonCancel = findViewById(R.id.CancelBtn);
        TextView myUserName = (TextView) findViewById(R.id.UserName);
        TextView editText = (TextView) findViewById(R.id.EditAccountUsername);
        TextView myPassword = (TextView) findViewById(R.id.ChangePass);

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
                Intent AccountSettingsToSettingsActivity = new Intent(AccountSettingsActivity.this, SettingsActivity.class);

                //put extra
                AccountSettingsToSettingsActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        AccountSettingsActivity.this.finish();
                    }
                });
                // Display "Cancelled" msg
                Toast.makeText(AccountSettingsActivity.this, "Cancelled!", Toast.LENGTH_SHORT).show();

                //start activity with result
                startActivityForResult(AccountSettingsToSettingsActivity,1);
            }
        });

    }
}