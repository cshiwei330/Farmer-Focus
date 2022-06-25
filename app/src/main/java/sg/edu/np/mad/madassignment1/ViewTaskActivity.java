package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

public class ViewTaskActivity extends AppCompatActivity {

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        DBHandler dbHandler = new DBHandler(this, null, null,6);

        TextView taskName = findViewById(R.id.taskViewTaskNameDisplay);
        TextView taskDesc = findViewById(R.id.taskViewTaskDescriptionDisplay);
        Button backButton = findViewById(R.id.backButton);
        Button deleteTaskButton = findViewById(R.id.deleteTaskButton);

        Intent receivingEnd = getIntent();
        int newTaskId = receivingEnd.getIntExtra("task id", 0);
//        int newTaskStatus = receivingEnd.getIntExtra("task status", 0);
//        String newTaskName = receivingEnd.getStringExtra("task name");
//        String newTaskDesc = receivingEnd.getStringExtra("task desc");
//        int newTaskHour = receivingEnd.getIntExtra("task hour", 0);
//        int newTaskMin = receivingEnd.getIntExtra("task minute", 0);
//        int newTaskYear = receivingEnd.getIntExtra("task year", 0);
//        int newTaskMonth = receivingEnd.getIntExtra("task month", 0);
//        int newTaskDayOfMonth = receivingEnd.getIntExtra("task dayOfMonth", 0);

        task = dbHandler.findTask(newTaskId);

        taskName.setText(task.getTaskName());
        taskDesc.setText(task.getTaskDesc());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Task deleteTask = new Task(newTaskId, newTaskStatus, newTaskName, newTaskDesc,
//                        newTaskHour, newTaskMin, newTaskYear, newTaskMonth, newTaskDayOfMonth);
//                dbHandler.deleteTask(deleteTask);
                dbHandler.deleteTask(task);
                Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

    }
}