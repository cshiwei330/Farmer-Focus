package sg.edu.np.mad.madassignment1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;

import android.util.Log;
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

import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.madassignment1.databinding.ActivityMainBinding;

public class TaskAddNewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String TAG = "AddNewTaskActivity";

    ArrayList<Task> taskList = new ArrayList<>();
    int starthour, startminute, endhour, endminute;
    int year, month, dayOfMonth;
    double diffInTime;
    String alert, taskType, repeat, startTime, taskDate;

    public String GLOBAL_PREF = "MyPrefs";

    private Spinner spinnerAlert, spinnerRepeat;

    private ActivityMainBinding binding;

    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_new_task);
        createNotificationChannel();

        //define elements in fragment
        EditText newTaskName = findViewById(R.id.newTaskNameActivity);
        EditText newTaskDesc = findViewById(R.id.newTaskDescActivity);
        Button createNewTaskButton = findViewById(R.id.createNewTaskButtonActivity);
        ImageView backButton = findViewById(R.id.addNewTaskBackButton);

        spinnerAlert = findViewById(R.id.editTaskAlertDropDown);
        spinnerRepeat = findViewById(R.id.repeatSpinnerDropDown);

        // Alert Times Spinner DropDown
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



        // Repeat Spinner DropDown
        String[] repeatOptions = getResources().getStringArray(R.array.task_repeat_options);
        ArrayAdapter adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, repeatOptions);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(adapter2);

        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeat = adapterView.getItemAtPosition(i).toString();
                if (repeat.matches("Weekly") || repeat.matches("Monthly")){
                    taskType = "Recurring";
                }
                else {
                    taskType = "Event";
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

                    Toast.makeText(TaskAddNewActivity.this, "Task Created", Toast.LENGTH_SHORT).show();

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
                    startTime =  String.format("%02d:%02d",starthour,startminute);
                    newTaskDB.setTaskStartTime(startTime);
                    String endTime =  String.format("%02d:%02d",endhour,endminute);
                    newTaskDB.setTaskEndTime(endTime);

                    newTaskDB.setTaskDuration(0);

                    newTaskDB.setAlert(alert);

                    // Set Task Alert Date Time
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    try {
                        String dateReplace = date.replace("/", "-");
                        taskDate = dateReplace + " " + startTime +":00";
                        long millisToSubtract;
                        Date d = format.parse(taskDate);
                        if (alert.matches("None")){
                            newTaskDB.setAlertDateTime(" ");
                        }
                        else if (alert.matches("At time of event")){
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("5 minutes before")){
                            millisToSubtract = 5 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("10 minutes before")){
                            millisToSubtract = 10 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("15 minutes before")){
                            millisToSubtract = 15 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("30 minutes before")){
                            millisToSubtract = 30 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("1 hour before")){
                            millisToSubtract = 60 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else if (alert.matches("1 day before")){
                            millisToSubtract = 1440 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                        else {
                            millisToSubtract = 10080 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                            newTaskDB.setAlertDateTime(taskDate);
                            Log.v(TAG, "Alert is: " + alert);
                            Log.v(TAG, "Modified Task Date: " + taskDate);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    newTaskDB.setTaskType(taskType);

                    newTaskDB.setRepeat(repeat);

                    newTaskDB.setTaskUserID(user.getUserID());

                    //add new task to db
                    dbHandler.addTask(newTaskDB);

                    Task t = new Task(id, status, newTaskNameString, newTaskDescString, date, startTime, endTime, diffInTime,
                            alert, taskDate, taskType, repeat, user.getUserID());

                    // Only set notification if alert is not None
                    if (t.getAlert().matches("None") == false){
                        setAlarm(t);
                    }

                    //start TaskActivity
                    Intent intent = new Intent(TaskAddNewActivity.this, TaskActivity.class);
                    startActivity(intent);

                    //kill this activity
                    finish();
                }
                else {
                    Toast.makeText(TaskAddNewActivity.this, "Please enter a valid "+validity+"!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TaskAddNewActivity.this, TaskActivity.class);
                startActivity(myIntent);
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
            Date1 = format.parse(startTime);
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

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "taskalertnotificationchannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("taskalertnotification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

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
        if (taskType == null){
            return "task type";
        }
        if (repeat == null){
            return "repeat";
        }
        if (taskType.matches("Recurring") && repeat.matches("None")){
            return "a valid repeat option since recurring task is selected";
        }
        if ((taskType.matches("Event") && repeat.matches("Weekly")) || (taskType.matches("Event") && repeat.matches("Monthly"))) {
            return "an event task cannot be repeated.";
        }

        return "VALID";
    }
}