/*
SIGN UP PAGE ACTIVITY
This activity is the sign up page, where the user can create a new account. The user cannot have a blank input for the username or passwords.
The user also has to agree to the terms and conditions before creating a new account. When the "SIGN UP" button is clicked, the newly created
account details will be added into the database.
 */

package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpPageActivity extends AppCompatActivity {
    sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // getting username and password
        EditText myCreateUsername = findViewById(R.id.editTextCreateUsername);
        EditText myCreatePassword = findViewById(R.id.editTextCreatePassword);
        EditText myConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // setting up back button
        Button myButtonCancel = findViewById(R.id.backToLogin);
        myButtonCancel.setOnClickListener(view -> {
            // starting activity, bringing user back to the login page
            Intent cancelCreation = new Intent(SignUpPageActivity.this, LoginPageActivity.class);
            startActivity(cancelCreation);
        });

        // setting up terms and conditions button
        CheckBox tandc = findViewById(R.id.TermsAndConditions);
        tandc.setOnClickListener(view -> {
            if (!tandc.isChecked()) {
                // making sure that the user agree to the terms and conditions before signing up an account
                Toast.makeText(SignUpPageActivity.this, "Agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
            }
        });

        // setting up sign up button
        Button CreateAccount = findViewById(R.id.signUpButton);
        CreateAccount.setOnClickListener(view -> {
            // making sure that the username and password created are not empty strings
            if (!myCreateUsername.getText().toString().equals("") && !myCreatePassword.getText().toString().equals("")) {
                // finding username entered in database to make sure there is no existing username
                User userDBData = dbHandler.findUser(myCreateUsername.getText().toString());
                if (userDBData == null) {
                    // making sure that the passwords entered matches
                    if (myCreatePassword.getText().toString().equals(myConfirmPassword.getText().toString())) {
                        // making sure that the user agreed to the terms and conditions
                        if (tandc.isChecked()) {
                            // creating a new user object
                            User userDataDB = new User();
                            // setting username and password of object
                            userDataDB.setUsername(myCreateUsername.getText().toString());
                            userDataDB.setPassword(myConfirmPassword.getText().toString());
                            // make ImageId equals to 0 when default
                            //userDataDB.setImageID(0);
                            // adding object into database
                            dbHandler.addUser(userDataDB);
                            // telling user that their account has been created successfully
                            Toast.makeText(SignUpPageActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            // starting activity, bringing user back to the login page to log into the app with their newly created account
                            Intent myCreateIntent = new Intent(SignUpPageActivity.this, LoginPageActivity.class);
                            startActivity(myCreateIntent);

                            //kill this activity
                            finish();
                        } else {
                            // telling user to agree to terms and conditions
                            Toast.makeText(SignUpPageActivity.this, "Agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // telling user that the passwords that they entered do not match
                        Toast.makeText(SignUpPageActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // telling user that the username has been taken already
                    Toast.makeText(SignUpPageActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            } else{
                // telling user to not leave the edit texts empty
                Toast.makeText(SignUpPageActivity.this, "Please input something!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}