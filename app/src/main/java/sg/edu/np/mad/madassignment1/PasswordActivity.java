package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.Pack200;

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
        EditText myNewPassword = findViewById(R.id.NewPassword);
        EditText myConfirmPassword = findViewById(R.id.ConfirmNewPassword);

        Button myButtonSave = findViewById(R.id.SaveBtn2);
        // Navigate back to Account Settings
        myButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                while (true){
                    User userDBData = dbHandler.findUser(myEditUsername.getText().toString());
                    //if username entered is incorrect or does not exists
                    if(userDBData == null) {
                        Toast.makeText(PasswordActivity.this, "Username does not exist!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else{
                        // ensure that the password entered is valid
                        if(myOldPassword.getText().toString().equals(userDBData.getPassword()) && !(myOldPassword.getText().toString()).equals("")){
                            // ensure that the user must enter a new password (field cannot be left empty)
                            if(!(myNewPassword.getText().toString()).equals("")) {
                                // ensure that the new password is not equals to the original password
                                if(!(myNewPassword.getText().toString()).equals(myOldPassword.getText().toString()) /*&& !(myNewPassword.getText().toString()).equals("")*/) {
                                    // ensure that the new password must be the same as the confirm password
                                    if (myNewPassword.getText().toString().equals(myConfirmPassword.getText().toString())) {
                                        newPass = myConfirmPassword.getText().toString();
                                        userDBData.setPassword(newPass);
                                        dbHandler.updateUser(userDBData);
                                        Toast.makeText(PasswordActivity.this, "Password successfully changed!", Toast.LENGTH_SHORT).show();

                                        //kill TaskActivity
                                        ((ResultReceiver) getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                                        //create intent to go to Password
                                        Intent AccountSettingsToPasswordActivity = new Intent(PasswordActivity.this, AccountSettingsActivity.class);

                                        //start activity with result
                                        startActivityForResult(AccountSettingsToPasswordActivity, 1);

                                        //kill this activity
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(PasswordActivity.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(PasswordActivity.this, "Password already exists. Please enter a different one.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(PasswordActivity.this, "Please enter a new password. This field cannot be left empty.", Toast.LENGTH_SHORT).show();
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