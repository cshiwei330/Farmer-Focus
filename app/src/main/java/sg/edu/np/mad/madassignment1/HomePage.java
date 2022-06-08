package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView tasksIcon = findViewById(R.id.tasksIcon);
        TextView tasksTexView = findViewById(R.id.tasksTextView);
        Fragment taskFragment = new TimerFragment();

        tasksIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.add(R.id.fragmentLayout, taskFragment);
                ft.show(taskFragment);
                ft.commit();
            }
        });

        tasksTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define fragment transaction
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // set fragment to tasks fragment
                ft.add(R.id.fragmentLayout, taskFragment);
                ft.show(taskFragment);
                ft.commit();
            }
        });


        Button test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testlogin = new Intent(HomePage.this, LoginPage.class);
                startActivity(testlogin);
            }
        });


    }
}