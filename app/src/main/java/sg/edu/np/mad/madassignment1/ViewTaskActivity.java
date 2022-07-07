package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ViewTaskActivity extends AppCompatActivity {

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        DBHandler dbHandler = new DBHandler(this, null, null,6);

        TextView taskName = findViewById(R.id.taskViewTaskNameDisplay);
        TextView taskDesc = findViewById(R.id.taskViewTaskDescriptionDisplay);
        TextView taskDate = findViewById(R.id.taskViewTaskDateDisplay);
        TextView taskTime = findViewById(R.id.taskViewTaskTimeDisplay);
        TextView taskAlert = findViewById(R.id.viewTaskAlertDisplay);
        ImageView backButton = findViewById(R.id.backButton);
        FloatingActionButton deleteTaskButton = findViewById(R.id.deleteTaskButton);
        FloatingActionButton editTaskButton = findViewById(R.id.editTaskButton);

        Intent receivingEnd = getIntent();
        int newTaskId = receivingEnd.getIntExtra("task id", 0);

        task = dbHandler.findTask(newTaskId);

        taskName.setText(task.getTaskName());
        taskDesc.setText(task.getTaskDesc());
        taskDate.setText(task.getTaskDate());
        taskTime.setText(task.getTaskTime());
        taskAlert.setText(task.getAlert());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent myIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
                extras.putInt("task id", newTaskId);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }
        });

        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewTaskActivity.this);
                builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.deleteTask(task);
                        Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                        startActivity(myIntent);

                        //toast to indicate tasks successfully cleared
                        Toast.makeText(ViewTaskActivity.this, "Task Cleared", Toast.LENGTH_LONG).show();
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