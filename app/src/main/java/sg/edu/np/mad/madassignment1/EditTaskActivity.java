package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final String TAG = "Edit Task Activity";

    int starthour, startminute, endhour, endminute;
    int year, month, dayOfMonth;

    String alert;
    private Spinner spinnerAlert;
    int alertIndex;

    public String GLOBAL_PREF = "MyPrefs";

    DBHandler dbHandler = new DBHandler(this, null, null, 6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        EditText newTaskName = findViewById(R.id.editTaskNameDisplay);
        EditText newTaskDesc = findViewById(R.id.editTaskDescDisplay);
        TextView newTaskDate = findViewById(R.id.editTaskDatePicker);
        TextView newTaskStartTime = findViewById(R.id.editTaskStartTimePicker);
        TextView newTaskEndTime = findViewById(R.id.editTaskEndTimePicker);
        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);

        spinnerAlert = findViewById(R.id.editTaskAlertDropDown);

        // receive from bundle
        Intent receivingEnd = getIntent();
        int oldTaskId = receivingEnd.getIntExtra("task id", 0);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        Task currentTask = dbHandler.findTask(oldTaskId);

        Log.v(TAG, currentTask.getTaskEndTime());

        // set old task details
        newTaskName.setText(currentTask.getTaskName());
        newTaskDesc.setText(currentTask.getTaskDesc());
        newTaskDate.setText(currentTask.getTaskDate());
        newTaskStartTime.setText(currentTask.getTaskStartTime());
        newTaskEndTime.setText(currentTask.getTaskEndTime());

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

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (alert == null){
                    alert = currentTask.getAlert();
                }

                String finalTaskName = newTaskName.getText().toString();
                String finalTaskDesc = newTaskDesc.getText().toString();

                // prevents error from occurring if task description is blank
                if (finalTaskDesc.matches("")) {
                    finalTaskDesc = " ";
                }

                // checks if user changed date
                // if user change date, use new date
                // or else use the old date
                // Note: if this isn't done, if the user does not change the date, the date will be 00/00/0000 which is an invalid date
                if (year == 0 || month == 0 || dayOfMonth == 0) {
                    String[] separatedDate = currentTask.getTaskDate().split("/");
                    year = Integer.valueOf(separatedDate[2]);
                    month = Integer.valueOf(separatedDate[1]);
                    dayOfMonth = Integer.valueOf(separatedDate[0]);
                }

                // split time for checking purpose
                String[] separatedStartTime = currentTask.getTaskStartTime().split(":");

                // checks if user changed time
                // if user did change time, use new time
                // or else use the old time
                // Note: if this isn't done, if the user does not change the time, the time wil become 00:00
                if (starthour == 0 && startminute == 0) {
                    if (Integer.valueOf(separatedStartTime[0]) == 0 && Integer.valueOf(separatedStartTime[1]) == 0) {
                        starthour = 0;
                        startminute = 0;
                    } else {
                        starthour = Integer.valueOf(separatedStartTime[0]);
                        startminute = Integer.valueOf(separatedStartTime[1]);
                    }
                }

                // split time for checking purpose
                String[] separatedEndTime = currentTask.getTaskEndTime().split(":");

                // checks if user changed time
                // if user did change time, use new time
                // or else use the old time
                // Note: if this isn't done, if the user does not change the time, the time wil become 00:00
                if (endhour == 0 && endminute == 0) {
                    if (Integer.valueOf(separatedEndTime[0]) == 0 && Integer.valueOf(separatedEndTime[1]) == 0) {
                        endhour = 0;
                        endminute = 0;
                    } else {
                        endhour = Integer.valueOf(separatedEndTime[0]);
                        endminute = Integer.valueOf(separatedEndTime[1]);
                    }
                }

                String finalTaskDate = String.format("%02d/%02d/%02d", dayOfMonth, month, year);
                String finalTaskStartTime = String.format("%02d:%02d", starthour, startminute);
                String finalTaskEndTime = String.format("%02d:%02d", endhour, endminute);

                String validity = taskIsValid(finalTaskName);

                // check if task is valid
                if (validity.equals("VALID")) {

                    Task editedTask = new Task(oldTaskId, currentTask.getStatus(), finalTaskName, finalTaskDesc, finalTaskDate, finalTaskStartTime, finalTaskEndTime, alert, user.getUserID());

                    dbHandler.editTask(editedTask);

                    Bundle extras = new Bundle();
                    Intent myIntent = new Intent(EditTaskActivity.this, ViewTaskActivity.class);
                    extras.putInt("task id", oldTaskId);
                    myIntent.putExtras(extras);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(EditTaskActivity.this, "Please enter a valid " + validity + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, selectedYear);
        c.set(Calendar.MONTH, selectedMonth);
        c.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);

        year = selectedYear;
        month = selectedMonth + 1;
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
        TextView timeTextView = findViewById(R.id.editTaskEndTimePicker);

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

    public String taskIsValid(String newTaskNameString) {
        if (newTaskNameString.length() < 1) {
            return "name";
        }
        if (year == 0 || month == 0 || dayOfMonth == 0) {
            return "date";
        }
        if (alert == null) {
            return "alert";
        }

        return "VALID";
    }
}