package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpPage extends AppCompatActivity {
    public String TAG = "Sign Up Page";
    public String GLOBAL_PREF = "MyPrefs";
    public String MY_USERNAME = "MyUsername";
    public String MY_PASSWORD = "MyPassword";
    public String MY_EMAIL = "MyEmail";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText myCreateUsername = findViewById(R.id.editTextCreateUsername);
        EditText myCreatePassword = findViewById(R.id.editTextCreatePassword);
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
                sharedPreferences = getSharedPreferences(GLOBAL_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MY_USERNAME, myCreateUsername.getText().toString());
                editor.putString(MY_PASSWORD, myCreatePassword.getText().toString());
                editor.putString(MY_EMAIL, myCreateEmail.getText().toString());
                editor.apply();

                Toast.makeText(SignUpPage.this, "Created Account Successfully", Toast.LENGTH_SHORT).show();

                Intent myCreateIntent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(myCreateIntent);
            }
        });


    }
}