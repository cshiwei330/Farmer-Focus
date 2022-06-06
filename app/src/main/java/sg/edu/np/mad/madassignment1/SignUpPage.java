package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpPage extends AppCompatActivity {
    public String TAG = "Sign Up Page";
    public String GLOBAL_PREF = "MyPrefs";
    public String MY_USERNAME = "MyUsername";
    public String MY_PASSWORD = "MyPassword";
    public String MY_EMAIL = "MyEmail";
    sg.edu.np.mad.week4.DBHandler dbHandler = new sg.edu.np.mad.week4.DBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText myCreateUsername = findViewById(R.id.editTextCreateUsername);
        EditText myCreatePassword = findViewById(R.id.editTextCreatePassword);
        EditText myConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText myCreateEmail = findViewById(R.id.editTextCreateEmail);

        Button myButtonCancel = findViewById(R.id.backToLogin);
        myButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelCreation = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(cancelCreation);
            }
        });

        Button CreateAccount = findViewById(R.id.signUpButton);
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox tandc = findViewById(R.id.TermsAndCondistions);
                tandc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tandc.isChecked()) {
                            User userDBData = dbHandler.findUser(myCreateUsername.getText().toString());
                            if (userDBData == null) {
                                if (myCreatePassword.getText().toString().equals(myConfirmPassword.getText().toString())) {
                                    User userDataDB = new User();
                                    userDataDB.setUsername(myCreateUsername.getText().toString());
                                    userDataDB.setPassword(myCreatePassword.getText().toString());
                                    dbHandler.addUser(userDataDB);
                                    Toast.makeText(SignUpPage.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                    Intent myCreateIntent = new Intent(SignUpPage.this, LoginPage.class);
                                    startActivity(myCreateIntent);
                                }
                                else {
                                    Toast.makeText(SignUpPage.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(SignUpPage.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SignUpPage.this, "Agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

        // here onwards is my attempt on the checkbox
        /*Button CreateAccount = findViewById(R.id.signUpButton);
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox tandc = findViewById(R.id.TermsAndCondistions);
                tandc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tandc.isChecked()) {
                            User userDBData = dbHandler.findUser(myCreateUsername.getText().toString());
                            if (userDBData == null) {
                                User userDataDB = new User();
                                userDataDB.setUsername(myCreateUsername.getText().toString());
                                userDataDB.setPassword(myCreatePassword.getText().toString());
                                dbHandler.addUser(userDataDB);
                                Toast.makeText(SignUpPage.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                Intent myCreateIntent = new Intent(SignUpPage.this, LoginPage.class);
                                startActivity(myCreateIntent);
                            } else {
                                Toast.makeText(SignUpPage.this, "User already exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpPage.this, "Agree to Terms and Conditions!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });*/