package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static int DELAY = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(LoginPage.PREFS_NAME, 0);
                boolean remember = sharedPreferences.getBoolean("remember", false);
                if(remember){
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);
                }
            }
        }, DELAY);

    }
}