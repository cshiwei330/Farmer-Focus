package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyLoginPrefs";
    private String TAG = "Login Page";
    public String GLOBAL_PREF = "MyPrefs";
    public String MY_USERNAME = "MyUsername";
    public String MY_PASSWORD = "MyPassword";
    sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        CheckBox remember = findViewById(R.id.rememberUser);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginPageActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("remember", true);
                    editor.commit();
                    Toast.makeText(LoginPageActivity.this, "Remembering you...", Toast.LENGTH_SHORT).show();
                }
                else if(!compoundButton.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginPageActivity.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("remember", false);
                    editor.commit();
                }
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etMyUsername = findViewById(R.id.editTextUsername);
                EditText etMyPassword = findViewById(R.id.editTextPassword);

                if (isValidCredentials(etMyUsername.getText().toString(), etMyPassword.getText().toString())) {
                    Toast.makeText(LoginPageActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(LoginPageActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button newUser = findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CreateAccount = new Intent(LoginPageActivity.this, SignUpPageActivity.class);
                startActivity(CreateAccount);
            }
        });
    }

    public boolean isValidCredentials(String username, String password) {
        User userDBData = dbHandler.findUser(username);
        if (userDBData == null) {
            Toast.makeText(LoginPageActivity.this, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
        } else {
            if (userDBData.getUsername().equals(username) && userDBData.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}