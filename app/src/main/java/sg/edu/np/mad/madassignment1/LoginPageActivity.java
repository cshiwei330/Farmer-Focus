package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {
    public String GLOBAL_PREF = "MyPrefs";
    sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // setting up remember me function
        CheckBox remember = findViewById(R.id.rememberUser);
        remember.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                //creating shared preferences to store a value if user wants to be remembered or not
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // when user checks the check box, remember will be set as true
                editor.putString("remember", "true");
                editor.apply();
                // telling user that the app will remember their account
                Toast.makeText(LoginPageActivity.this, "Remembering you...", Toast.LENGTH_SHORT).show();
            }
            else if(!compoundButton.isChecked()){
                //creating shared preferences to store a value if user wants to be remembered or not
                SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // when user does not checks the check box, remember will be set as false
                editor.putString("remember", "false");
                editor.apply();
            }
        });

        // setting up login button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            // getting username and password
            EditText etMyUsername = findViewById(R.id.editTextUsername);
            EditText etMyPassword = findViewById(R.id.editTextPassword);
            // creating shared preferences to access username in other activities
            SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", etMyUsername.getText().toString());
            String imageIDString;
            try{
                imageIDString = String.valueOf(dbHandler.findUser(etMyUsername.getText().toString()).getImageID());
            }
            catch (NullPointerException e){
                imageIDString = "0";
            }
            editor.putString("imageID", imageIDString);
            editor.apply();

            // checking if username and password entered are valid
            if (isValidCredentials(etMyUsername.getText().toString(), etMyPassword.getText().toString())) {
                // telling user that they have successfully logged in
                Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                //starting new activity, bringing user to the home page
                Intent myIntent = new Intent(LoginPageActivity.this, HomeActivity.class);
                startActivity(myIntent);

                //kill this activity
                finish();

            } else {
                // telling user that their username or password is invalid
                Toast.makeText(LoginPageActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        });

        // setting up sign up button
        Button newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(view -> {
            // starting activity, bringing user to sign up page
            Intent CreateAccount = new Intent(LoginPageActivity.this, SignUpPageActivity.class);
            startActivity(CreateAccount);

        });
    }

    // method to check if the username and password entered belongs to an existing account
    public boolean isValidCredentials(String username, String password) {
        // finding username entered in the table of the database
        User userDBData = dbHandler.findUser(username);
        if (userDBData == null) {
            // telling user that the username entered does not exist
            Toast.makeText(LoginPageActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
        }
        else if (userDBData.getUsername().equals(username) && userDBData.getPassword().equals(password)) {
            // if username and password entered matches up with the account in the database, details entered for login is correct
            return true;
        }
        return false;
    }
}
