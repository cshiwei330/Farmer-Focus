package sg.edu.np.mad.madassignment1;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;

import java.net.HttpCookie;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.madassignment1.databinding.ActivityMainBinding;

public class TaskAddNewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String TAG = "AddNewTaskActivity";

    ArrayList<Task> taskList = new ArrayList<>();
    int starthour, startminute, endhour, endminute;
    int year, month, dayOfMonth, recurringId;
    long millisToAdd, millisToSubtract;
    long diffInTime;
    String alert, taskType, repeat, startTime, taskDate, date, endTime, strDate, alertDateTime, repeatDuration;
    Date d;

    public String GLOBAL_PREF = "MyPrefs";

    private Spinner spinnerAlert, spinnerRepeat, spinnerRepeatDuration;

    private ActivityMainBinding binding;

    private AlarmManager alarmManager;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_new_task);
        createNotificationChannel();

        //define database
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //get task data from database
        taskList = dbHandler.getTaskData(user.getUserID());

        //define elements in activity
        EditText newTaskName = findViewById(R.id.newTaskNameActivity);
        EditText newTaskDesc = findViewById(R.id.newTaskDescActivity);
        Button createNewTaskButton = findViewById(R.id.createNewTaskButtonActivity);

        spinnerAlert = findViewById(R.id.editTaskAlertDropDown);
        spinnerRepeat = findViewById(R.id.repeatSpinnerDropDown);
        spinnerRepeatDuration = findViewById(R.id.spinnerRepeatDuration);

        // Alert Times Spinner DropDown
        String[] alertTimes = getResources().getStringArray(R.array.alert_times);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, alertTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlert.setAdapter(adapter);

        // Get user choice of alert time
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

        // Get user choice of repeat frequency for recurring tasks
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

        // Repeat Duration Spinner Dropdown
        String[] repeatDurationOptions = getResources().getStringArray(R.array.repeat_duration_options);
        ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, repeatDurationOptions);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeatDuration.setAdapter(adapter3);

        // Get user choice for repeat duration for recurring tasks
        spinnerRepeatDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (repeat.matches("None")) {
                    repeatDuration = "None";
                }
                else {
                    repeatDuration = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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

                    startTime =  String.format("%02d:%02d",starthour,startminute);
                    endTime =  String.format("%02d:%02d",endhour,endminute);

                    if (month < 10) {
                        date = dayOfMonth + "-" + "0" + month + "-" + year;
                    }
                    else {
                        date = dayOfMonth + "-" + month + "-" + year;
                    }



                    // Set Task Alert DateTime
                    // Delete appropriate amount of time from start time based on alert specified
                    DateFormat taskAlertFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    taskDate = date + " " + startTime +":00";

                    try {
                        d = taskAlertFormat.parse(taskDate);

                        if (alert.matches("None")){
                            taskDate = " ";
                        }
                        else if (alert.matches("At time of event")){
                            taskDate = String.valueOf(d);
                        }
                        else if (alert.matches("5 minutes before")){
                            millisToSubtract = 5 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = d.toString();
                        }
                        else if (alert.matches("10 minutes before")){
                            millisToSubtract = 10 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                        else if (alert.matches("15 minutes before")){
                            millisToSubtract = 15 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                        else if (alert.matches("30 minutes before")){
                            millisToSubtract = 30 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                        else if (alert.matches("1 hour before")){
                            millisToSubtract = 60 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                        else if (alert.matches("1 day before")){
                            millisToSubtract = 1440 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                        else {
                            millisToSubtract = 10080 * 60000;
                            d.setTime(d.getTime() - millisToSubtract);
                            taskDate = String.valueOf(d);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Formatting date to a format accepted by the alarm manager
                    if (taskDate.matches(" ") == false) {
                        ArrayList<String> monthsList = new ArrayList<>(
                                Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                        );
                        String[] taskDateSplit = taskDate.split(" ");
                        int monthIndexInt = -1;
                        String monthIndex;
                        for (int i=0; i<monthsList.size(); i++) {
                            if (taskDateSplit[1].matches(monthsList.get(i))) {
                                monthIndexInt = i;
                                break;
                            }
                        }
                        if (monthIndexInt < 10) {
                            monthIndex = "0" + (monthIndexInt+1);
                        }
                        else {
                            monthIndex = String.valueOf(monthIndexInt+1);
                        }
                        taskDate = taskDateSplit[2] + "-" + monthIndex + "-" + taskDateSplit[5] + " " + taskDateSplit[3];
                    }


                    // Add Event Task To Task List and Database or 1st of recurring task to Task List or Database
                    // 0 means false = not completed, 1 means true = completed

                    // Set Task Id
                    int id = taskList.size() + 1;

                    // Get the recurring highest recurring id from the database
                    // Set the new recurring id to be 1 more than the highest
                    // Ensures that all unique recurring tasks has a unique id
                    int maxRecurringId = dbHandler.returnHighestRecurringId(user.getUserID());
                    int newRecurringId = maxRecurringId + 1;



                    // Error Handling
                    // Case 1: Not possible for repeat to be "None" and repeat duration to either be "1 Month" or "1 Year"
                    // Case 2: Not possible for repeat duration to be "None" and repeat to be "Weekly" or "Monthly"
                    // Hence, if Case 1 or Case 2 occurs, assume the other variable as "None"
                    // Have a Toast Message to update the user on what is going on

                    // Case 1
                    if (repeat.matches("None") && (repeatDuration.matches("1 Month") || repeatDuration.matches("1 Year"))) {
                        Log.v(TAG, "Repeat: None and Repeat Duration is 1 Month or 1 Year");
                        repeatDuration = "None";
                        taskType = "Event";
                        Toast.makeText(TaskAddNewActivity.this, "Repeat Duration set to None as there is no repeat frequency indicated.", Toast.LENGTH_SHORT).show();
                    }

                    // Case 2
                    if (repeatDuration.matches("None") && (repeat.matches("Weekly") || repeat.matches("Monthly"))) {
                        Log.v(TAG, "Repeat: Weekly or Monthly and Repeat Duration is None");
                        repeat = "None";
                        taskType = "Event";
                        Toast.makeText(TaskAddNewActivity.this, "Repeat set to None as there is no repeat duration indicated.", Toast.LENGTH_SHORT).show();
                    }



                    // Populate new task fields ---------------------------------------------------------------------------------------------------------------------------------
                    // Add task to database
                    Task newTaskDB = new Task();
                    newTaskDB.setId(id);
                    newTaskDB.setStatus(0);
                    newTaskDB.setTaskName(newTaskNameString);
                    newTaskDB.setTaskDesc(newTaskDescString);
                    newTaskDB.setTaskDate(date);
                    newTaskDB.setTaskStartTime(startTime);
                    newTaskDB.setTaskEndTime(endTime);
                    newTaskDB.setAlert(alert);
                    newTaskDB.setAlertDateTime(taskDate);
                    newTaskDB.setTaskType(taskType);
                    newTaskDB.setRepeat(repeat);
                    newTaskDB.setRecurringDuration(repeatDuration);
                    newTaskDB.setTaskUserID(user.getUserID());

                    // Set recurringId to 0 if the task type is "Event" as "Event" tasks will not need a recurringId
                    // Since an "Event" task does not recur recurringId is set to 0 as it does not need anything to uniquely identify it other than TaskId
                    if (taskType.matches("Event")){
                        newTaskDB.setRecurringId(0);
                    }
                    else {
                        newTaskDB.setRecurringId(newRecurringId);
                    }

                    // Add Task to Database
                    dbHandler.addTask(newTaskDB);

                    // ---------------------------------------------------------------------------------------------------------------------------------------------------------



                    // UPDATE WIDGET ------------------------------------------------------------------------------

                    Context context = getApplicationContext();
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

                    RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
                    try {
                        mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(dbHandler.getTodayTaskData(user.getUserID()).size())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    appWidgetManager.updateAppWidget(thisWidget, mainView);

                    //--------------------------------------------------------------------------------------------



                    Task t = new Task(id, 0, newTaskNameString, newTaskDescString, date, startTime, endTime, diffInTime,
                            alert, taskDate, taskType, repeat, recurringId, repeatDuration, user.getUserID());

                    // Only set notification if alert is not "None"
                    if (t.getAlert().matches("None") == false){
                        setAlarm(t);
                    }



                    // Add remaining tasks to DB based on indicated duration
                    // Check how many other recurring tasks to add based on repeat frequency and repeat duration
                    if (taskType.matches("Recurring")) {

                        int numberOfTimes;

                        if (repeat.matches("Weekly")) {
                            if (repeatDuration.matches("1 Month")) {
                                numberOfTimes = 3;
                            }
                            else {
                                numberOfTimes = 51;
                            }
                        }

                        else {
                            if (repeatDuration.matches("1 Month")) {
                                numberOfTimes = 0;
                            }
                            else {
                                numberOfTimes = 11;
                            }
                        }



                        // Format the task dates to be fit the SimpleDateFormat
                        String startDateString;
                        if (month < 10) {
                            startDateString = dayOfMonth + "-" + "0"+ month + "-" + year;
                        }
                        else {
                            startDateString = dayOfMonth + "-" + month + "-" + year;
                        }



                        // List of taskDates
                        ArrayList<String> taskDateList = new ArrayList<>();

                        Date taskDateRecurring = null;

                        // Convert String to Date
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            taskDateRecurring = dateFormat.parse(startDateString);
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Check interval to add for each task date
                        // Add time (days or months) based on user's indication and add new dates into the taskDateList
                        if (repeat.matches("Weekly")) {
                            millisToAdd = 604800000L;
                            for (int i=0; i<numberOfTimes; i++) {
                                taskDateRecurring.setTime(taskDateRecurring.getTime() + millisToAdd);
                                strDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(taskDateRecurring);
                                strDate = strDate.replace("/", "-");
                                taskDateList.add(strDate);
                            }
                        }
                        else { // if (repeat.matches("Monthly"))
                            for (int i=0; i<numberOfTimes; i++) {
                                millisToAdd = 2629800000L;
                                taskDateRecurring.setTime(taskDateRecurring.getTime() + millisToAdd);
                                strDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(taskDateRecurring);
                                strDate = strDate.replace("/", "-");
                                taskDateList.add(strDate);
                            }
                        }






                        // List of alertDateTime
                        DateFormat alertDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        ArrayList<String> alertDateTimeList = new ArrayList<>();
                        ArrayList<String> alertDateTimeRightFormatList = new ArrayList<>();
                        Date alertD = null;

                        // Check to make sure taskDate (alertDateTime in String) is not " "
                        // Convert taskDate (String) to (Date)
                        // Add time (days or months) as specified by user to taskDate
                        // This will get the new taskDate (the date and time the notification should sound)
                        /* If taskDate is " " (user does not want an alert), " " will be added to the alertDateTimeRightFormatList
                        and " " will be the alertDateTime in the Database
                         */
                        // If taskDate is " " no notification will be created in the alarmManager and NotificationManagerCompat

                        // If alert wanted
                        if (taskDate.matches(" ") == false) {

                            // Convert to Date format
                            try {
                                alertD = alertDateTimeFormat.parse(taskDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // Do calculations for the future alertDateTime based on the repeat frequency
                            for (int i=0; i<numberOfTimes; i++) {
                                alertD.setTime(alertD.getTime() + millisToAdd);
                                alertDateTime = String.valueOf(alertD);
                                Log.v(TAG, "alertDateTime: " + alertDateTime);
                                alertDateTimeList.add(alertDateTime);
                            }

                            // Format taskDate to be accepted by the alarmManager
                            for (int i=0; i<alertDateTimeList.size(); i++){
                                ArrayList<String> monthsList = new ArrayList<>(
                                        Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                                );
                                String[] taskDateSplit = String.valueOf(alertDateTimeList.get(i)).split(" ");
                                int monthIndexInt = -1;
                                String monthIndex;
                                for (int j=0; j<monthsList.size(); j++) {
                                    if (taskDateSplit[1].matches(monthsList.get(j))) {
                                        monthIndexInt = j;
                                        break;
                                    }
                                }
                                if (monthIndexInt < 10) {
                                    monthIndex = "0" + (monthIndexInt+1);
                                }
                                else {
                                    monthIndex = String.valueOf(monthIndexInt+1);
                                }
                                alertDateTime = taskDateSplit[2] + "-" + monthIndex + "-" + taskDateSplit[5] + " " + taskDateSplit[3];
                                alertDateTimeRightFormatList.add(alertDateTime);
                            }

                        }
                        // if no alert wanted
                        else {
                            for (int i=0; i<numberOfTimes; i++) {
                                alertDateTimeRightFormatList.add(" ");
                            }
                        }

                        // Populate task fields
                        // Add additional tasks to DataBase
                        // It will carry out the job mentioned above (lines 526 and 527) more than once based on repeat frequency and repeat duration indicated
                        for (int i=0; i<numberOfTimes; i++) {

                            id = id + 1;
                            newTaskDB.setId(id);
                            newTaskDB.setStatus(0);
                            newTaskDB.setTaskName(newTaskNameString);
                            newTaskDB.setTaskDesc(newTaskDescString);
                            newTaskDB.setTaskDate(taskDateList.get(i));
                            newTaskDB.setTaskStartTime(startTime);
                            newTaskDB.setTaskEndTime(endTime);
                            newTaskDB.setAlert(alert);
                            newTaskDB.setAlertDateTime(alertDateTimeRightFormatList.get(i));
                            newTaskDB.setTaskType(taskType);
                            newTaskDB.setRepeat(repeat);
                            newTaskDB.setRecurringId(newRecurringId);
                            newTaskDB.setRecurringDuration(repeatDuration);
                            newTaskDB.setTaskUserID(user.getUserID());
                            dbHandler.addTask(newTaskDB);

                            t = new Task(id, 0, newTaskNameString, newTaskDescString, strDate, startTime, endTime, diffInTime,
                                    alert, alertDateTime, taskType, repeat, recurringId, repeatDuration, user.getUserID());

                            // Only set notification if alert is not None
                            if (t.getAlert().matches("None") == false){
                                setAlarm(t);
                            }
                        }
                    }

                    //start TaskActivity
                    Intent intent = new Intent(TaskAddNewActivity.this, TaskActivity.class);
                    startActivity(intent);

                    //kill this activity
                    finish();
                }
                else {
                    // Display if task is not valid
                    Toast.makeText(TaskAddNewActivity.this, "Please enter a task "+validity+"!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // setAlarm Method
    /* This method sets the notification using the alarmManager which sets the time for the notification to pop up and will send task data to the AlarmReceiver.java*/
    // The AlarmReceiver.java will then set the notification details such as the title and icon
    private void setAlarm(Task t) {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Bundle extras = new Bundle();
        Intent myIntent = new Intent(TaskAddNewActivity.this, AlarmReceiver.class);
        extras.putString("task name", t.getTaskName());
        extras.putString("task alert", t.getAlert());
        myIntent.putExtras(extras);

        pendingIntent = PendingIntent.getBroadcast(TaskAddNewActivity.this, t.getId(), myIntent,PendingIntent.FLAG_IMMUTABLE);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date Date1 = null;
        long timeInMilliseconds = 0;

        // Converts String to Date and converts the dateTime to milliseconds
        try {
            Date1 = format.parse(t.getAlertDateTime());
            timeInMilliseconds = Date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // set time for notification to pop up
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
    }

    // createNotificationChannel() method
    // This method creates the channel for all the notifications that will be shown
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

    // onDateSet(...) Method
    // This method enables the user to pick a date for their task
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

    // popStartTimePicker(...)
    // This method allows the user to pick a start time for their task
    // a time picker shows up when clicked on the timeTextView
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

    // popEndTimePicker(...)
    // This method allows the user to pick an end time for their task
    // a time picker shows up when clicked on the timeTextView
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

    // OnDateClick(...) method
    // This method makes a datePicker show up. It is in the form of a DialogFragment
    // It shows up whem the user clicks on it to select a date for the task
    public void onDateClick(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    // taskIsValid(...) Method
    // This method checks if the task is valid by making sure all compulsory selection has been selected and the user's task data is indicated
    public String  taskIsValid(String newTaskNameString){
        // ensures that a task has a name as it is compulsory
        if(newTaskNameString.length() < 1){
            return "name";
        }
        // ensures a task has a date as it is compulsory
        if(year == 0 || month == 0 || dayOfMonth == 0){
            return "date";
        }
//        if (alert == null){
//            return "alert";
//        }
//        if (taskType == null){
//            return "task type";
//        }
//        if (repeat == null){
//            return "repeat";
//        }
//        if (taskType.matches("Recurring") && repeat.matches("None")){
//            return "a valid repeat option since recurring task is selected";
//        }
//        // ensures that the user does not
//        if ((taskType.matches("Event") && repeat.matches("Weekly")) || (taskType.matches("Event") && repeat.matches("Monthly"))) {
//            return "an event task cannot be repeated.";
//        }
        return "VALID";
    }
}