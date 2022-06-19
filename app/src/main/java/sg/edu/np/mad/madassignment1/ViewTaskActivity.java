package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        TextView taskName = findViewById(R.id.taskViewTaskNameDisplay);
        TextView taskDesc = findViewById(R.id.taskViewTaskDescriptionDisplay);
        Button backButton = findViewById(R.id.backButton);
        Button deleteTaskButton = findViewById(R.id.deleteTaskButton);

        Intent receivingEnd = getIntent();
        String newTaskName = receivingEnd.getStringExtra("task name");
        String newTaskDesc = receivingEnd.getStringExtra("task desc");

        taskName.setText(newTaskName);
        taskDesc.setText(newTaskDesc);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ViewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
            }
        });

//        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }
}