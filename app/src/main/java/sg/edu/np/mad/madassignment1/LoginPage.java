package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {
    private String TAG = "Login Page";
    public String GLOBAL_PREF = "MyPrefs";
    public String MY_USERNAME = "MyUsername";
    public String MY_PASSWORD = "MyPassword";
    //SharedPreferences sharedPreferences;
    sg.edu.np.mad.week4.DBHandler dbHandler = new sg.edu.np.mad.week4.DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etMyUsername = findViewById(R.id.editTextUsername);
                EditText etMyPassword = findViewById(R.id.editTextPassword);

                if (isValidCredentials(etMyUsername.getText().toString(), etMyPassword.getText().toString())) {
                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginPage.this, MainActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(LoginPage.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateAccount = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(CreateAccount);
            }
        });
    }

    public boolean isValidCredentials(String username, String password) {
        /*sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
        String sharedUsername = sharedPreferences.getString(MY_USERNAME, "");
        String sharedPassword = sharedPreferences.getString(MY_PASSWORD, "");

        if (username.equals(sharedUsername) && password.equals(sharedPassword)) {
            return true;
        }
        return false;
        }*/

        User userDBData = dbHandler.findUser(username);
        if (userDBData == null) {
            Toast.makeText(LoginPage.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
        } else {
            if (userDBData.getUsername().equals(username) && userDBData.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
