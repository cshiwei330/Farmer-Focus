package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {
    String newPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        EditText myEditUsername = findViewById(R.id.EditUsername);
        EditText myOldPassword = findViewById(R.id.OldPassword);
        EditText myPassword = findViewById(R.id.NewPassword);
        EditText myConfirmPassword = findViewById(R.id.ConfirmNewPassword);

        Button myButtonSave = findViewById(R.id.SaveBtn2);
        // Navigate back to Account Settings
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to go to Password
                Intent AccountSettingsToPasswordActivity = new Intent(PasswordActivity.this, AccountSettingsActivity.class);

                //put extra
                AccountSettingsToPasswordActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        PasswordActivity.this.finish();
                    }
                });

                while (true){
                    User userDBData = dbHandler.findUser(myEditUsername.getText().toString());
                    //if username entered is incorrect or does not exists
                    if(userDBData == null) {
                        Toast.makeText(PasswordActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else{
                        if(myOldPassword.getText().toString().equals(userDBData.getPassword())){
                            if(myPassword.getText().toString().equals(myConfirmPassword.getText().toString())){
                                newPass = myConfirmPassword.getText().toString();
                                userDBData.setPassword(newPass);
                                dbHandler.updateUser(userDBData);
                                Toast.makeText(PasswordActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();

                                // Display "Saved" msg
                                //Toast.makeText(PasswordActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                                //start activity with result
                                startActivityForResult(AccountSettingsToPasswordActivity,1);
                            }
                            else{
                                Toast.makeText(PasswordActivity.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(PasswordActivity.this, "Invalid Password. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    break;
                }

            }
        });
    }
}