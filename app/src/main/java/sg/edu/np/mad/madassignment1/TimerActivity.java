package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ResultReceiver;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import sg.edu.np.mad.madassignment1.databinding.ActivityTimerBinding;

public class TimerActivity extends DrawerBaseActivity{
    int hour, minute, second;
    //define activity binding
    ActivityTimerBinding activityTimerBinding;

    //time
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private TextView mTextSetTime;
    private ImageView SetTime;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private TextView mTimeTextView;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityTimerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityTimerBinding.getRoot());
        //set title
        allocateActivityTitle("Timer");

        //timer
        mTextSetTime = findViewById(R.id.textview_setTime);
        mEditTextInput = findViewById(R.id.edit_text_minutes);
        mTextViewCountDown = findViewById(R.id.countdown);
        SetTime = findViewById(R.id.GreenTick);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mTimeTextView = findViewById(R.id.countdown);



        //NEW

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // getting stored username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        Intent receivingEnd = getIntent();
        int newTaskId = receivingEnd.getIntExtra("task id", 0);
        task = dbHandler.findTask(newTaskId);
        //setTime(task);

        //define recyclerView
        //RecyclerView recyclerView = findViewById(R.id.UncompletedTaskRecycleView);

        //Finding uncompleted tasks
        //define taskList array
//        ArrayList<Task> taskList = new ArrayList<>();
//        //current taskList with db data
//        taskList = dbHandler.getTaskData(user.getUserID());

        // initialize recyclerview for TASKS
        //set adaptor to TimerRecyclerViewAdaptor, given taskList
//        TimerRecyclerViewAdaptor mAdaptor = new TimerRecyclerViewAdaptor(taskList);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(mAdaptor);
        //setTime(dbHandler.findTask(TaskId)); // use method from dbHandler to get task using taskId

        //NEW

        // displays numeric keyboard
        // only allow user to key in integers 0 to 9 and not anything else when setting the time
        mEditTextInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        // after the user have entered their preferred timing for countdown in "edit_text_minutes",
        // they should click on the image view represented by a green tick, to set the time
//        SetTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String input = mEditTextInput.getText().toString();
//                if (input.length() == 0) {
//                    Toast.makeText(TimerActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                long millisInput = Long.parseLong(input) * 60000; // change to minutes
//                if (millisInput <= 0) {
//                    Toast.makeText(TimerActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                setTime(millisInput);
//                mEditTextInput.setText("");
//            }
//        });

        // if timer is running, start button changes to become pause, else, start button will remain unchanged
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        // reset the timer (this button will only be shown once countdown timer has started and after the user
        // clicked the pause button)
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        // new
//        mTimeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popTimePicker();
//            }
//        });

        // navigate to stopwatch page
        ImageView StopWatch = findViewById(R.id.StopWatchImg);
        StopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TimerActivitytoStopWatchActivity = new Intent(TimerActivity.this, StopWatchActivity.class);
                //put extra
                TimerActivitytoStopWatchActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        TimerActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(TimerActivitytoStopWatchActivity, 1);
            }
        });

        // navigate to task list page to select task
        ImageView SelectTask = findViewById(R.id.selectTaskImg);
        SelectTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TimerActivityToTimerTaskListActivity = new Intent(TimerActivity.this, TimerTaskListActivity.class);
                //put extra
                TimerActivityToTimerTaskListActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        TimerActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(TimerActivityToTimerTaskListActivity, 1);
            }
        });

    }

    //NEW
//    public ArrayList<Task> findUncompletedTasks (ArrayList<Task> taskList){
//        ArrayList<Task> uncompletedTaskList = new ArrayList<>();
//
//        for (int i = 0; i < taskList.size(); i++){ //loop through current taskList to find tasks that are uncompleted
//            Task task = taskList.get(i);
//
//            boolean result = withinAWeek(task.getTaskDate()); //check if task is not complete
//
//            if (result){
//                uncompletedTaskList.add(task); //if true then add to new list
//            }
//        }
//        return uncompletedTaskList; //display this list in the recyclerView
//    }


//    public void uncompleted () {
//        for (int j=0; j < taskList.size(); j++){
//            if (taskList.get(j).getStatus() == 1){
//                secondTaskList.add(taskList.get(j));
//            }
//        }
//        for (int j=0; j<secondTaskList.size(); j++){
//            taskList.remove(secondTaskList.get(j));
//        }
//    }

//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String filterOption = adapterView.getItemAtPosition(i).toString();
//        if (filterOption.matches("Not Completed")){
//            Collections.sort(taskList, Task.TaskIdAscComparator);
//        }
//    }




    // set the time on the timer
//    private void setTime(long milliseconds) {
//        mStartTimeInMillis = milliseconds;
//        resetTimer();
//        closeKeyboard();
//    }

//    private void setTime(Task t) {
//        // receive from bundle
//        //Intent receivingEnd = getIntent();
//        //int TaskId = receivingEnd.getIntExtra("task id", -1);
//        double milliseconds = (t.getTaskDuration() * 60000.0);
//        mStartTimeInMillis = (new Double(milliseconds)).longValue();
//        //Double.valueOf(milliseconds).longValue();
//        resetTimer();
//        //closeKeyboard();
//    }



    // start the timer
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        // set the count down interval to every 1000 milliseconds which is 1 second
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) ((mTimeLeftInMillis / 1000) / 3600);
        int minutes = (int) (((mTimeLeftInMillis / 1000) % 3600) / 60); // change milliseconds to minutes after accounting for hours
        int seconds = (int) ((mTimeLeftInMillis / 1000) % 60); // change milliseconds to seconds after accounting for minutes

        // the hours place will only be shown in the event when the user selects more than or equals to 60 minutes which is equivalent to an hour
        // if the user selects less than 60 minutes, only the minutes and seconds place will be shown on the countdown timer
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    // when the timer has started, only the timer countdown and pause button will be shown on the screen.
    // when paused is clicked, a reset button will pop out on the screen to allow users to reset the timer
    // if users wants to change the time on the timer, they have to key in their preferred value at the edit
    // text location which they wishes to change the timer countdown to (in minutes)

    private void updateWatchInterface() {
        if (mTimerRunning) {
            SetTime.setVisibility(View.INVISIBLE);
            mTextSetTime.setVisibility(View.INVISIBLE);
            mEditTextInput.setVisibility(View.INVISIBLE);
            //mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        }
        else {
            mTextSetTime.setVisibility(View.VISIBLE);
            SetTime.setVisibility(View.VISIBLE);
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

//            if (mTimeLeftInMillis < 1000) {
//                mButtonStartPause.setVisibility(View.INVISIBLE);
//            } else {
//                mButtonStartPause.setVisibility(View.VISIBLE);
//            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }
    }

    // closes the keyboard once user has selected a time by clicking on the tick button to confirm the time
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


//    private class NumericKeyBoardTransformationMethod extends TimerActivity {
//        public CharSequence getTransformation(CharSequence source, View view) {
//            return source;
//        }
//    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0)
            {
                //mTimeLeftInMillis = 0;
                mTimeLeftInMillis = mStartTimeInMillis;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            }
            else
            {
                startTimer();
            }
        }
    }


//    @Override
//    public void onItemClick(int position, Task t)
//    {
//        Log.v("test", "1");
//        Toast.makeText(TimerActivity.this, "Task selected", Toast.LENGTH_SHORT).show();
//        double milliseconds = (t.getTaskDuration() * 60000.0);
//        mStartTimeInMillis = (new Double(milliseconds)).longValue();
//        //Double.valueOf(milliseconds).longValue();
//        //mStartTimeInMillis = millis;
//        resetTimer(); // update count down text method inside resetTimer
//
//    }


}