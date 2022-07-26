package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final String TAG = "Edit Task Activity";

    int starthour, startminute, endhour, endminute;
    int year, month, dayOfMonth, alertIndex, repeatIndex;
    double diffInTime;

    String alert, repeat, taskType, finalTaskStartTime;
    private Spinner spinnerAlert, spinnerRepeat;

    public String GLOBAL_PREF = "MyPrefs";

    ArrayList<String> alertOptionsList = new ArrayList<>();
    ArrayList<String> repeatOptionsList = new ArrayList<>();

    DBHandler dbHandler = new DBHandler(this, null, null, 6);

    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;

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
        spinnerRepeat = findViewById(R.id.editTaskRepeatSpinnerDropDown);

        // receive from bundle
        Intent receivingEnd = getIntent();
        int oldTaskId = receivingEnd.getIntExtra("Task id", 0);

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




        // Spinner for alert times option
        String[] alertTimes = getResources().getStringArray(R.array.alert_times);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alertTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlert.setAdapter(adapter);

        // add all alert times into the optionList
        for (int i=0; i< alertTimes.length; i++){
            alertOptionsList.add(alertTimes[i]);
        }

        // get index for the option in the optionsList
        for (int i=0; i<alertOptionsList.size(); i++) {
            if (alertOptionsList.get(i).matches(currentTask.getAlert())) {
                alertIndex = i;
                break;
            }
        }

        // set default value of the spinner to be original value from database
        spinnerAlert.setSelection(alertIndex);

        // get the user's choice if edited
        spinnerAlert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                alert = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });




        // Spinner for repeat options
        String[] repeatOptions = getResources().getStringArray(R.array.task_repeat_options);
        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, repeatOptions);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(adapter2);

        // add all alert times into the optionList
        for (int i=0; i< repeatOptions.length; i++){
            repeatOptionsList.add(repeatOptions[i]);
        }

        // get index for the option in the optionsList
        for (int i=0; i<repeatOptionsList.size(); i++) {
            if (repeatOptionsList.get(i).matches(currentTask.getRepeat())) {
                repeatIndex = i;
                break;
            }
        }

        spinnerRepeat.setSelection(repeatIndex);

        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeat = adapterView.getItemAtPosition(i).toString();
                if (repeat.matches("Weekly") || repeat.matches("Monthly")){
                    taskType = "Recurring";
                }
                else {
                    taskType = currentTask.getTaskType();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // Save edited details
        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (alert == null){
                    alert = currentTask.getAlert();
                }

                if (repeat == null){
                    repeat = currentTask.getRepeat();
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

                String finalTaskDate = String.format("%02d-%02d-%02d", dayOfMonth, month, year);
                finalTaskStartTime = String.format("%02d:%02d", starthour, startminute);
                String finalTaskEndTime = String.format("%02d:%02d", endhour, endminute);

                diffInTime = currentTask.getTaskDuration();

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String dateReplace = finalTaskDate.replace("/", "-");
                String taskDate = dateReplace + " " + finalTaskStartTime +":00";
                try {
                    long millisToSubtract;
                    Date d = format.parse(taskDate);
                    if (alert.matches("None")){
                        taskDate = " ";
                        cancelNotification(currentTask);
                    }
                    else if (alert.matches("At time of event")){
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("5 minutes before")){
                        millisToSubtract = 5 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("10 minutes before")){
                        millisToSubtract = 10 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("15 minutes before")){
                        millisToSubtract = 15 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("30 minutes before")){
                        millisToSubtract = 30 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("1 hour before")){
                        millisToSubtract = 60 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else if (alert.matches("1 day before")){
                        millisToSubtract = 1440 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                    else {
                        millisToSubtract = 10080 * 60000;
                        d.setTime(d.getTime() - millisToSubtract);
                        taskDate = String.valueOf(d);
                        setAlarm(currentTask);
                        Log.v(TAG, "Alert is: " + alert);
                        Log.v(TAG, "Modified Task Date: " + taskDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int recurringId = currentTask.getRecurringId();

                String recurringDuration = currentTask.getRecurringDuration();

                String validity = taskIsValid(finalTaskName);

                // check if task is valid
                if (validity.equals("VALID")) {

                    Task editedTask = new Task(oldTaskId, currentTask.getStatus(), finalTaskName, finalTaskDesc, finalTaskDate,
                            finalTaskStartTime, finalTaskEndTime, diffInTime, alert, taskDate, taskType, repeat, recurringId, recurringDuration, user.getUserID());

                    if (taskType.matches("Recurring")) {
                        // Check if date changes
                        // If yes, date all recurring task dates
                        // If no, leave it
                    }

                    dbHandler.editTask(editedTask);

                    Bundle extras = new Bundle();
                    Intent myIntent = new Intent(TaskEditActivity.this, TaskViewActivity.class);
                    extras.putInt("Task id", oldTaskId);
                    myIntent.putExtras(extras);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(TaskEditActivity.this, "Please enter a valid " + validity + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void setAlarm(Task t) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Bundle extras = new Bundle();
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        extras.putString("task name", t.getTaskName());
        extras.putString("task alert", t.getAlert());
        myIntent.putExtras(extras);

        pendingIntent = PendingIntent.getBroadcast(this, t.getId(), myIntent,0);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date Date1 = null;
        long timeInMilliseconds = 0;
        try {
            Date1 = format.parse(finalTaskStartTime);
            timeInMilliseconds = Date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
        }
        else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
        }
    }

    public void cancelNotification(Task t) {
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, AlarmReceiver.class);
        extras.putString("task name", t.getTaskName());
        extras.putString("task alert", t.getAlert());
        intent.putExtras(extras);
        PendingIntent pending = PendingIntent.getBroadcast(this, t.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Cancel notification
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pending);
    }

    // Set Date
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

    // Set Start Time
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

    // Set End Time
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
        return "VALID";
    }
}