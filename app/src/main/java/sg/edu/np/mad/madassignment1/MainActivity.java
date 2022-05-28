package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList <Task> taskList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task taskOne = new Task("Finish Math Homework", "Page 1-10");
        Task taskTwo = new Task("Finish English Homework", "Page 1-5");
        Task taskThree = new Task("Finish Science Homework", "Page 1-9");
        Task taskFour = new Task("Finish Chinese Homework", "Page 1-7");
        Task taskFive = new Task("Finish FP2 Homework", "Page 1-4");

        taskList.add(taskOne);
        taskList.add(taskTwo);
        taskList.add(taskThree);
        taskList.add(taskFour);
        taskList.add(taskFive);

        RecyclerView recyclerView = findViewById(R.id.toDoListRecycleView);
        MyAdaptor mAdaptor = new MyAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        Button test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testlogin = new Intent(MainActivity.this, LoginPage.class);
                startActivity(testlogin);
            }
        });
    }
}