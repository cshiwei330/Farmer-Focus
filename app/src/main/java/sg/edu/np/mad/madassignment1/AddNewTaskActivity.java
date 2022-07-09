package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String TAG = "AddNewTaskActivity";

    ArrayList<Task> taskList = new ArrayList<>();
    int starthour, startminute, endhour, endminute;
    int year, month, dayOfMonth;
    String alert;

    public String GLOBAL_PREF = "MyPrefs";

    private Spinner spinnerAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        //define elements in fragment
        EditText newTaskName = findViewById(R.id.newTaskNameActivity);
        EditText newTaskDesc = findViewById(R.id.newTaskDescActivity);
        Button createNewTaskButton = findViewById(R.id.createNewTaskButtonActivity);
        ImageView backButton = findViewById(R.id.addNewTaskBackButton);

        spinnerAlert = findViewById(R.id.editTaskAlertDropDown);

        String[] alertTimes = getResources().getStringArray(R.array.alert_times);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alertTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlert.setAdapter(adapter);

        spinnerAlert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                alert = adapterView.getItemAtPosition(i).toString();
                if (alert.matches("Choose Alert")){
                    alert = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //define database
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //get task data from database
        taskList = dbHandler.getTaskData(user.getUserID());

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

                    String date = String.format("%02d/%02d/%02d",dayOfMonth,month,year);
                    newTaskDB.setTaskDate(date);
                    String startTime =  String.format("%02d:%02d",starthour,startminute);
                    newTaskDB.setTaskStartTime(startTime);
                    String endTime =  String.format("%02d:%02d",endhour,endminute);
                    newTaskDB.setTaskEndTime(endTime);
                    newTaskDB.setAlert(alert);
                    newTaskDB.setTaskUserID(user.getUserID());

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddNewTaskActivity.this, TaskActivity.class);
                startActivity(myIntent);
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

        TextView textView = (TextView) findViewById(R.id.editTaskDatePicker);
        textView.setText(currentDateString);
    }

    public void popStartTimePicker(View view)
    {
        TextView timeTextView = findViewById(R.id.editTaskStartTimePicker);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                starthour = selectedHour;
                startminute = selectedMinute;
                timeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d",starthour, startminute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, starthour, startminute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popEndTimePicker(View view)
    {
        TextView timeTextView = findViewById(R.id.newTaskEndTimePicker);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                endhour = selectedHour;
                endminute = selectedMinute;
                timeTextView.setText(String.format(Locale.getDefault(), "%02d:%02d",endhour, endminute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, endhour, endminute, true);

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
        if(year == 0 || month == 0 || dayOfMonth == 0){
            return "date";
        }
        if (alert == null){
            return "alert";
        }

        return "VALID";
    }
}