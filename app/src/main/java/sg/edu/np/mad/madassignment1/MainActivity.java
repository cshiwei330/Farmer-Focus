package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public String GLOBAL_PREF = "MyPrefs";

    private static final int DELAY = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // seeing if remember me was checked when logging in, if checked, app launches in home page, if not, app launches in login page
        new Handler().postDelayed(() -> {
            // getting the stored value from login page in the shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
            // getting value
            String remember = sharedPreferences.getString("remember", "");
            User user = new User();
            // getting stored username
            String username = sharedPreferences.getString("username", "");
            user.setUsername(username);

            Intent intent;
            if (remember.equals("false")) {
                // starting new activity, bringing user to login page if the user did not check the remember me box
                intent = new Intent(MainActivity.this, LoginPageActivity.class);
            }
            else {
                // starting new activity, bringing user to home page if the user checked the remember me box
                intent = new Intent(MainActivity.this, HomeActivity.class);
            }
            startActivity(intent);
        }, DELAY);
    }
}