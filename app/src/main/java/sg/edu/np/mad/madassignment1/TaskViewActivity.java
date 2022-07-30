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

import java.util.ArrayList;

public class TaskViewActivity extends AppCompatActivity {

    private String TAG = "ViewTaskActivity";

    Task task;

    ArrayList<Task> allTasks = new ArrayList<>();
    ArrayList<Task> tasksToDeleteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // Define elements in the xml
        TextView taskName = findViewById(R.id.taskViewTaskNameDisplay);
        TextView taskDesc = findViewById(R.id.taskViewTaskDescriptionDisplay);
        TextView taskDate = findViewById(R.id.taskViewTaskDateDisplay);
        TextView taskStartTime = findViewById(R.id.taskViewTaskStartTimeDisplay);
        TextView taskEndTime = findViewById(R.id.taskViewTaskEndTimeDisplay);
        TextView taskAlert = findViewById(R.id.viewTaskAlertDisplay);
        TextView taskAlertDateTime = findViewById(R.id.viewTaskAlertDateTimeDisplay);
        TextView taskRepeat = findViewById(R.id.viewTaskRepeatDisplay);
        FloatingActionButton deleteTaskButton = findViewById(R.id.deleteTaskButton);
        ImageView backButton = findViewById(R.id.viewTaskBackButton);
        FloatingActionButton editTaskButton = findViewById(R.id.editTaskButton);

        // Receive taskId from adaptor when task is clicked
        Intent receivingEnd = getIntent();
        int newTaskId = receivingEnd.getIntExtra("Task id", 0);
        // Using the id, we find the particular task to show in the TaskViewActivity page
        task = dbHandler.findTask(newTaskId);

        // Set the details of the task
        taskName.setText(task.getTaskName());
        taskDesc.setText(task.getTaskDesc());
        taskDate.setText(task.getTaskDate());
        taskStartTime.setText(task.getTaskStartTime());
        taskEndTime.setText(task.getTaskEndTime());

        // Set Task Alert
        taskAlert.setText(task.getAlert());

        // Set Task Date Time
        if (task.getAlertDateTime().matches(" ")){
            taskAlertDateTime.setText("None");
        }
        else {
            taskAlertDateTime.setText(task.getAlertDateTime());
        }

        // Set Task Repeat
        taskRepeat.setText(task.getRepeat());



        /* When user clicks on edit task button,
        they will be sent to the TaskEditActivity.
         */
        /* At the same time, a bundle is used to send the task id
        as the taskid is needed to find the task and set some of the default values of the elements
        in the edit task page
         */
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent myIntent = new Intent(TaskViewActivity.this, TaskEditActivity.class);
                extras.putInt("Task id", newTaskId);
                myIntent.putExtras(extras);
                startActivity(myIntent);
            }
        });


        // Delete Task Feature
        // This feature deletes this particular task in the database and the task will no longer be retrievable
        // This feature is applicable to both recurring and event task
        // If task type is Event, the task object will be used to delete the task in the database
        // If task type is Recurring, the recurring id will be used to delete current and all future task or the current task only (depending on the user's specification)
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.getTaskType().matches("Recurring")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewActivity.this);
                    // If task type is recurring, Ask user if he or she would like to delete this task or all future recurring tasks
                    builder.setMessage("Would you like to delete this task only or this task including all future recurring task?").setCancelable(true);
                    builder.setPositiveButton("Current And All Future Tasks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // AlertDialog to confirm if user really wants to delete current and all future tasks
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewActivity.this);
                            builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    // Get all tasks from the database
                                    allTasks = dbHandler.getTaskData(task.getTaskUserID());

                                    // Add the tasks to delete to the tasksToDeleteList based on recurring id and task id
                                    // recurring id must be the same as current task but task id must be more than or equal to current task
                                    // this ensures that previous recurring tasks with the same recurring id are not deleted
                                    for (int j=0; j<allTasks.size(); j++) {
                                        if (allTasks.get(j).getRecurringId() == task.getRecurringId() && allTasks.get(j).getId() >= task.getId()){
                                            tasksToDeleteList.add(allTasks.get(j));
                                        }
                                    }

                                    // delete the current and future recurring tasks in the database
                                    for (int k=0; k<tasksToDeleteList.size(); k++) {
                                        dbHandler.deleteTask(tasksToDeleteList.get(k));
                                    }

                                    // Bring the user back to the task list page
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
                    builder.setNegativeButton("Just This Task", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewActivity.this);

                            // Ask user if he would like to just delete this task even if task type is recurring
                            // The process of doing it is the same as deleting an Event task
                            builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    // delete task current task from database
                                    dbHandler.deleteTask(task);

                                    // bring user back to task list page
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
                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete Option");
                    alert.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewActivity.this);

                    // Double confirm with the user if he or she wants to carry out the deletion of the task
                    builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // delete the task from the database
                            dbHandler.deleteTask(task);

                            // bring the user back to the task list page
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
            }
        });

        // Bring the user back to the task list page when back button clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TaskViewActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

    }
}