package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskViewActivity extends AppCompatActivity {

    private String TAG = "ViewTaskActivity";
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        DBHandler dbHandler = new DBHandler(this, null, null,6);

        TextView taskName = findViewById(R.id.taskViewTaskNameDisplay);
        TextView taskDesc = findViewById(R.id.taskViewTaskDescriptionDisplay);
        TextView taskDate = findViewById(R.id.taskViewTaskDateDisplay);
        TextView taskStartTime = findViewById(R.id.taskViewTaskStartTimeDisplay);
        TextView taskEndTime = findViewById(R.id.taskViewTaskEndTimeDisplay);
        TextView taskDuration = findViewById(R.id.taskViewTaskDurationDisplay);
        TextView taskAlert = findViewById(R.id.viewTaskAlertDisplay);
        TextView taskAlertDateTime = findViewById(R.id.viewTaskAlertDateTimeDisplay);
        TextView taskType = findViewById(R.id.viewTaskTypeDisplay);
        TextView taskRepeat = findViewById(R.id.viewTaskRepeatDisplay);
        ImageView backButton = findViewById(R.id.backButton);
        FloatingActionButton deleteTaskButton = findViewById(R.id.deleteTaskButton);
        FloatingActionButton editTaskButton = findViewById(R.id.editTaskButton);

        Intent receivingEnd = getIntent();
        int newTaskId = receivingEnd.getIntExtra("task id", 0);

        task = dbHandler.findTask(newTaskId);

        taskName.setText(task.getTaskName());
        taskDesc.setText(task.getTaskDesc());
        taskDate.setText(task.getTaskDate());
        taskStartTime.setText(task.getTaskStartTime());
        taskEndTime.setText(task.getTaskEndTime());

        double hoursDecimal = task.getTaskDuration() / 60;
        int hours = (int) hoursDecimal;
        int minutes = (int) task.getTaskDuration() - (hours * 60);

        // Set Task Duration
        if (hours == 0 && minutes == 0){
            taskDuration.setText("0 mins");
        }
        else if (hours == 0 && minutes == 1){
            taskDuration.setText(minutes + " Min ");
        }
        else if (hours == 0 && minutes > 1){
            taskDuration.setText(minutes + " Mins ");
        }
        else if (hours == 1 && minutes == 0){
            taskDuration.setText(hours + " Hour ");
        }
        else if (hours == 1 && minutes == 1){
            taskDuration.setText(hours + " Hour " + minutes + " Min");
        }
        else if (hours == 1 && minutes > 1){
            taskDuration.setText(hours + " Hour " + minutes + " Mins");
        }
        else if (hours > 1 && minutes == 0){
            taskDuration.setText(hours + " Hours ");
        }
        else if (hours > 1 && minutes == 1){
            taskDuration.setText(hours + " Hours " + minutes + " Min");
        }
        else {
            taskDuration.setText(hours + " Hours " + minutes + " Mins");
        }

        // Set Task Alert
        taskAlert.setText(task.getAlert());

        // Set Task Date Time
        if (task.getAlertDateTime().matches(" ")){
            taskAlertDateTime.setText("None");
        }
        else {
            taskAlertDateTime.setText(task.getAlertDateTime());
        }

        taskType.setText(task.getTaskType());

        taskRepeat.setText(task.getRepeat());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TaskViewActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent myIntent = new Intent(TaskViewActivity.this, TaskEditActivity.class);
                extras.putInt("task id", newTaskId);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewActivity.this);
                builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.deleteTask(task);
                        Intent myIntent = new Intent(TaskViewActivity.this, TaskActivity.class);
                        startActivity(myIntent);

                        //toast to indicate tasks successfully cleared
                        Toast.makeText(TaskViewActivity.this, "Task Cleared", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setTitle("Delete task");
                alert.show();
            }
        });

    }
}