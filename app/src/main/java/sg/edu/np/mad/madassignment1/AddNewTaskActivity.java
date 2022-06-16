package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewTaskActivity extends AppCompatActivity {

    ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        //define elements in fragment
        EditText newTaskName = findViewById(R.id.newTaskNameActivity);
        EditText newTaskDesc = findViewById(R.id.newTaskDescActivity);
        Button createNewTaskButton = findViewById(R.id.createNewTaskButtonActivity);
        Button dateButton = findViewById(R.id.datePickerButtonActivity);

        //define database
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        //get task data from database
        taskList = dbHandler.getTaskData();

        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getBaseContext(), "Task Created", Toast.LENGTH_SHORT).show();

                String newTaskNameString = newTaskName.getText().toString();
                String newTaskDescString = newTaskDesc.getText().toString();

                // 0 means false = not completed, 1 means true = completed
                int status = 0;
                int id = taskList.size() + 1;

                //populate new task fields
                Task newTaskDB = new Task();
                newTaskDB.setId(id);
                newTaskDB.setStatus(status);
                newTaskDB.setTaskName(newTaskNameString);
                newTaskDB.setTaskDesc(newTaskDescString);

                //add new task to db
                dbHandler.addTask(newTaskDB);

                //start TaskActivity
                Intent intent = new Intent(getBaseContext(), TaskActivity.class);
                startActivity(intent);

                //end this activity
                finish();
            }
        });
    }
}