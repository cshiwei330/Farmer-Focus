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

        //define elements in fragment
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

        String[] repeatDurationOptions = getResources().getStringArray(R.array.repeat_duration_options);
        ArrayAdapter adapter3 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, repeatDurationOptions);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeatDuration.setAdapter(adapter3);

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


                    // Add Event Task To List or 1st of recurring task
                    // 0 means false = not completed, 1 means true = completed

                    int id = taskList.size() + 1;

                    int maxRecurringId = dbHandler.returnHighestRecurringId(user.getUserID());
                    int newRecurringId = maxRecurringId + 1;

                    //populate new task fields
                    Task newTaskDB = new Task();
                    newTaskDB.setId(id);
                    newTaskDB.setStatus(0);
                    newTaskDB.setTaskName(newTaskNameString);
                    newTaskDB.setTaskDesc(newTaskDescString);
                    newTaskDB.setTaskDate(date);
                    newTaskDB.setTaskStartTime(startTime);
                    newTaskDB.setTaskEndTime(endTime);
                    //newTaskDB.setTaskDuration(0);
                    newTaskDB.setAlert(alert);
                    newTaskDB.setAlertDateTime(taskDate);
                    newTaskDB.setTaskType(taskType);
                    newTaskDB.setRepeat(repeat);
                    newTaskDB.setRecurringDuration(repeatDuration);
                    newTaskDB.setTaskUserID(user.getUserID());
                    if (taskType.matches("Event")){
                        newTaskDB.setRecurringId(0);
                    }
                    else {
                        newTaskDB.setRecurringId(newRecurringId);
                    }
                    dbHandler.addTask(newTaskDB);

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
                    // Only set notification if alert is not None
                    if (t.getAlert().matches("None") == false){
                        setAlarm(t);
                    }



                    // Add remaining tasks to DB based on indicated duration
                    if (taskType.matches("Recurring")) {

                        // Decides how many tasks to add
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

                        // List of taskDates
                        String startDateString;
                        if (month < 10) {
                            startDateString = dayOfMonth + "-" + "0"+ month + "-" + year;
                        }
                        else {
                            startDateString = dayOfMonth + "-" + month + "-" + year;
                        }


                        ArrayList<String> taskDateList = new ArrayList<>();

                        Date taskDateRecurring = null;

                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            taskDateRecurring = dateFormat.parse(startDateString);
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Check interval to add for each task date and task alert datetime
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
                        Date alertDRightFormat = null;


                        if (taskDate.matches(" ") == false) {

                            try {
                                alertD = alertDateTimeFormat.parse(taskDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            for (int i=0; i<numberOfTimes; i++) {
                                alertD.setTime(alertD.getTime() + millisToAdd);
                                alertDateTime = String.valueOf(alertD);
                                Log.v(TAG, "alertDateTime: " + alertDateTime);
                                alertDateTimeList.add(alertDateTime);
                            }

                            Log.v(TAG, "List: " + String.valueOf(alertDateTimeList.size()));

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

                            Log.v(TAG, "List Right Format: " + String.valueOf(alertDateTimeRightFormatList.size()));

                        }
                        else {
                            for (int i=0; i<numberOfTimes; i++) {
                                alertDateTimeRightFormatList.add(" ");
                            }
                        }


                        for (int i=0; i<numberOfTimes; i++) {

                            // Set Task Id, Status, Name; Desc
                            id = id + 1;
                            newTaskDB.setId(id);
                            newTaskDB.setStatus(0);
                            newTaskDB.setTaskName(newTaskNameString);
                            newTaskDB.setTaskDesc(newTaskDescString);
                            newTaskDB.setTaskDate(taskDateList.get(i));
                            newTaskDB.setTaskStartTime(startTime);
                            newTaskDB.setTaskEndTime(endTime);
                            //newTaskDB.setTaskDuration(0);
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
                    Toast.makeText(TaskAddNewActivity.this, "Please enter a valid "+validity+"!", Toast.LENGTH_SHORT).show();
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
        Log.v(TAG, "t.getAlertDateTime: " + t.getAlertDateTime());

        try {
            Date1 = format.parse(t.getAlertDateTime());
            timeInMilliseconds = Date1.getTime();
            Log.v(TAG, String.valueOf(timeInMilliseconds));
        } catch (ParseException e) {
            Log.v(TAG, "Not found");
            e.printStackTrace();
        }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent); //
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