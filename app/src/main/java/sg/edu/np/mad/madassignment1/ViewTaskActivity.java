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

                dbHandler.deleteTask(task);
                Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

    }
}