package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ArrayList<Task> taskList = new ArrayList<>();
    int hour, minute;
    int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        //define elements in fragment
        EditText newTaskName = findViewById(R.id.newTaskNameActivity);
        EditText newTaskDesc = findViewById(R.id.newTaskDescActivity);
        Button createNewTaskButton = findViewById(R.id.createNewTaskButtonActivity);

        //define database
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        //get task data from database
        taskList = dbHandler.getTaskData();

        //confirm create new task button listener
        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTaskNameString = newTaskName.getText().toString();
                String newTaskDescString = newTaskDesc.getText().toString();
                String validity = taskIsValid(newTaskNameString);
                //if "VALID" task is properly filled in
                if (validity.equals("VALID")){
                    //kill TaskActivity
                    ((ResultReceiver)getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                    Toast.makeText(AddNewTaskActivity.this, "Task Created", Toast.LENGTH_SHORT).show();

                    // 0 means false = not completed, 1 means true = completed
                    int status = 0;
                    int id = taskList.size() + 1;

                    //populate new task fields
                    Task newTaskDB = new Task();
                    newTaskDB.setId(id);
                    newTaskDB.setStatus(status);
                    newTaskDB.setTaskName(newTaskNameString);
                    newTaskDB.setTaskDesc(newTaskDescString);
                    newTaskDB.setTaskHour(hour);
                    newTaskDB.setTaskMinute(minute);
                    newTaskDB.setTaskYear(year);
                    newTaskDB.setTaskMonth(month);
                    newTaskDB.setTaskDayOfMonth(dayOfMonth);

                    //add new task to db
                    dbHandler.addTask(newTaskDB);

                    //start TaskActivity
                    Intent intent = new Intent(AddNewTaskActivity.this, TaskActivity.class);
                    startActivity(intent);

                    //kill this activity
                    finish();
                }
                else {
                    Toast.makeText(AddNewTaskActivity.this, "Please enter a valid "+validity+"!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, selectedYear);
        c.set(Calendar.MONTH, selectedMonth);
        c.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);

        year = selectedYear;
        month = selectedMonth+1;
        dayOfMonth = selectedDayOfMonth;

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.datePickerTextView);
        textView.setText(currentDateString);
    }

    public void popTimePicker(View view)
    {
        TextView timeTextView = findViewById(R.id.timePickerTextView);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        //int style = AlertDialog.THEME_TRADITIONAL;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void onDateClick(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    public String  taskIsValid(String newTaskNameString){
        if(newTaskNameString.length() < 1){
            return "name";
        }
        if(year == 0 && month == 0 && dayOfMonth != 0){
            return "date";
        }

        return "VALID";
    }
}