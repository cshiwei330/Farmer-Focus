package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private final String TAG = "Edit Task Activity";

    int hour, minute;
    int year, month, dayOfMonth;

    public String GLOBAL_PREF = "MyPrefs";

    DBHandler dbHandler = new DBHandler(this, null, null,6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        EditText newTaskName = findViewById(R.id.editTaskNameDisplay);
        EditText newTaskDesc = findViewById(R.id.editTaskDescDisplay);
        TextView newTaskDate = findViewById(R.id.editTaskDatePicker);
        TextView newTaskTime = findViewById(R.id.editTaskTimePicker);
        Button saveDetailsButton = findViewById(R.id.saveDetailsButton);

        // receive from bundle
        Intent receivingEnd = getIntent();
        int oldTaskId = receivingEnd.getIntExtra("task id", 0);

        // shared preferences to get username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        Task currentTask = dbHandler.findTask(oldTaskId);

        // set old task details
        newTaskName.setText(currentTask.getTaskName());
        newTaskDesc.setText(currentTask.getTaskDesc());
        newTaskDate.setText(currentTask.getTaskDate());
        newTaskTime.setText(currentTask.getTaskTime());

        saveDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalTaskName = newTaskName.getText().toString();
                String finalTaskDesc = newTaskDesc.getText().toString();

                // prevents error from occurring if task description is blank
                if (finalTaskDesc.matches("")){
                    finalTaskDesc = " ";
                }

                // checks if user changed date
                // if user change date, use new date
                // or else use the old date
                // Note: if this isn't done, if the user does not change the date, the date will be 00/00/0000 which is an invalid date
                if(year == 0 || month == 0 || dayOfMonth == 0){
                    String[] separatedDate = currentTask.getTaskDate().split("/");
                    year = Integer.valueOf(separatedDate[2]);
                    month = Integer.valueOf(separatedDate[1]);
                    dayOfMonth = Integer.valueOf(separatedDate[0]);
                }

                // split time for checking purpose
                String[] separatedTime = currentTask.getTaskTime().split(":");

                // checks if user changed time
                // if user did change time, use new time
                // or else use the old time
                // Note: if this isn't done, if the user does not change the time, the time wil become 00:00
                if (hour == 0 && minute == 0){
                    if (Integer.valueOf(separatedTime[0]) == 0 && Integer.valueOf(separatedTime[1]) == 0){
                        hour = 0;
                        minute = 0;
                    }
                    else {
                        hour = Integer.valueOf(separatedTime[0]);
                        minute = Integer.valueOf(separatedTime[1]);
                    }
                }

                String finalTaskDate = String.format("%02d/%02d/%02d",dayOfMonth,month,year);
                String finalTaskTime =  String.format("%02d:%02d",hour,minute);

                String validity = taskIsValid(finalTaskName);

                // check if task is valid
                if (validity.equals("VALID")){

                    Task editedTask = new Task(oldTaskId, currentTask.getStatus(), finalTaskName, finalTaskDesc, finalTaskDate, finalTaskTime, user.getUserID());

                    dbHandler.editTask(editedTask);

                    Bundle extras = new Bundle();
                    Intent myIntent = new Intent(EditTaskActivity.this, ViewTaskActivity.class);
                    extras.putInt("task id", oldTaskId);
                    myIntent.putExtras(extras);
                    startActivity(myIntent);
                }
                else {
                    Toast.makeText(EditTaskActivity.this, "Please enter a valid "+validity+"!", Toast.LENGTH_SHORT).show();
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
        month = selectedMonth+1;
        dayOfMonth = selectedDayOfMonth;

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.editTaskDatePicker);
        textView.setText(currentDateString);
    }

    public void popTimePicker(View view)
    {
        TextView timeTextView = findViewById(R.id.editTaskTimePicker);

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

    public String taskIsValid(String newTaskNameString){
        if(newTaskNameString.length() < 1){
            return "name";
        }
        if(year == 0 || month == 0 || dayOfMonth == 0){
            return "date";
        }

        return "VALID";
    }
}