package sg.edu.np.mad.madassignment1;

/*----------------------------------------------------------------------------------------------------*/

                                    /* TIMER ACTIVITY */
/* The Timer Activity is created for users to focus better with a dedicated countdown timer.
This activity allows users to set the timer by selecting the task which they would like to start
doing by clicking on the container view. When timer has started, start will change to finish in the event
when user finishes the task before the duration is up. If user sits through the entire duration of the task,
there will be an alert sound and chicken gif showing up. */

/*----------------------------------------------------------------------------------------------------*/

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.edu.np.mad.madassignment1.databinding.ActivityTimerBinding;

public class TimerActivity extends DrawerBaseActivity{
    // creating a new array list
    ArrayList<Task> taskList = new ArrayList<>();

    //define activity binding
    ActivityTimerBinding activityTimerBinding;

    //time
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonGiveUp;
    private Button mButtonStopwatch;
    private Button mButtonTimer;
    private TextView mTimeTextView;
    private ImageView sheep;
    private ImageView chicken;
    private TextView TaskName;
    private TextView Taskmsg;
    private View nameContainer;
    private View descContainer;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private long mDuration;
    private long mTimeTaken;

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
        mTextViewCountDown = findViewById(R.id.countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonStopwatch = findViewById(R.id.Stopwatch);
        mButtonTimer = findViewById(R.id.Timer);
        mTimeTextView = findViewById(R.id.countdown);
        mButtonGiveUp = findViewById(R.id.giveUpBtn);
        sheep = findViewById(R.id.sheepGif);
        chicken = findViewById(R.id.chicken);
        TaskName = findViewById(R.id.taskName);
        Taskmsg = findViewById(R.id.textmsg);
        nameContainer = findViewById(R.id.view3);
        descContainer = findViewById(R.id.searchTaskContainer2);

        // setting colour of buttons
        mButtonTimer.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonClicked));
        mButtonStopwatch.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));

        //NEW

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // getting stored username using Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //get task data from database
        taskList = dbHandler.getTaskData(user.getUserID());


        Intent received = getIntent();
        Bundle taskId = received.getBundleExtra("task id");
        Log.v("testing1", String.valueOf(taskId));



        // if timer is running, start button closes and finish button shows up
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    int taskId = extras.getInt("task id", -1);
                    task = dbHandler.findTask(taskId);

                    if (task != null) {
                        setTime(task);
                    }

                    if (mTimerRunning) {
                        //pauseTimer();
                    }
                    else {
                        startTimer();
                    }
                }
                else {
                    Toast.makeText(TimerActivity.this, "Please select a task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // give up button (stop doing the task)
        mButtonGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this);
                builder.setTitle("Are you sure you have finished?");
                builder.setMessage("Task will be mark as completed once clicked on this button " +
                        "\n\nWould you like to stop doing the task?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){


                        // pause timer
                        pauseTimer();

                        // get duration
                        mTimeTaken = getDuration();

                        // edit task duration column in db
                        task.setTaskDuration(mTimeTaken);
                        dbHandler.updateDuration(task);

                        // mark task as completed
                        task.setStatus(1);
                        dbHandler.changeTaskStatus(task);

                        mCountDownTimer.cancel();

                        // set timer countdown text to 00:00
                        mTextViewCountDown.setText("00:00");

                        task = null;

                        //to "refresh" the change
                        Intent myIntent = new Intent(TimerActivity.this, TimerActivity.class);
                        startActivity(myIntent);

                        // show msg tat task is completed
                        Toast.makeText(TimerActivity.this, "Congrats! You have completed this task!", Toast.LENGTH_SHORT).show();

                    }
                });
                // if no is clicked, timer continues to run
                builder.setNegativeButton("No", new
                        DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });

                AlertDialog promptTaskCompleted = builder.create();
                promptTaskCompleted.show();
            }
        });


        //for sheep animation
        ImageView sheep = findViewById(R.id.sheepGif);
        Glide.with(this).load(R.drawable.sheep).into(sheep);


        // navigate to the stopwatch page
        mButtonStopwatch.setOnClickListener(new View.OnClickListener() {
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
                //startActivity(TimerActivitytoStopWatchActivity);
                startActivityForResult(TimerActivitytoStopWatchActivity, 1);
            }
        });


    }


    // setting of time in the timer
    private void setTime(Task t) {
        // Get Task Duration (set by user)
        Log.v("test", "testing");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        try {
            Date Date1 = simpleDateFormat.parse(t.getTaskStartTime());
            Date Date2 = simpleDateFormat.parse(t.getTaskEndTime());
            long difference = Date2.getTime() - Date1.getTime();
            if(difference<0)
            {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference=(dateMax.getTime() -Date1.getTime() )+(Date2.getTime()-dateMin.getTime());
            }
            double milliseconds = (double) difference;
            mStartTimeInMillis = new Double(milliseconds).longValue(); // get initial task duration (what user sets)
            mDuration = mStartTimeInMillis - mTimeLeftInMillis; // get how long user does the task for
            resetTimer(); // set the time in countdown textView
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private long getDuration() {
        // get duration (how much time user spends on doing the tasks)
        mDuration = mStartTimeInMillis - mTimeLeftInMillis;
        return mDuration;
    }



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
                // timer alarm plays for 16 sec after time runs out
                MediaPlayer ring = MediaPlayer.create(TimerActivity.this, R.raw.alarmringtone);
                ring.start();

                //for chicken animation
                ImageView chicken = findViewById(R.id.chicken);
                Glide.with(TimerActivity.this).load(R.drawable.chicken).into(chicken);

                //define dbHandler
                DBHandler dbHandler = new DBHandler(TimerActivity.this, null, null,6);

                // mark task as completed
                if (task != null) {
                    task.setStatus(1);
                    dbHandler.changeTaskStatus(task);
                }


                // show msg tat task is completed
                Toast.makeText(TimerActivity.this, "Congrats! You have completed this task!", Toast.LENGTH_SHORT).show();
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();

    }

    // pause the timer
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    // reset the timer
    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    // update timer countdown text
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

    // when the timer has started, only the timer countdown and finish button will be shown on the screen.
    // when finish is clicked, an alert dialog will pop out to ask user if they really want to stop doing the task
    // if yes, task will be mark as completed and timer will be reset to 00:00
    private void updateWatchInterface() {
        if (mTimerRunning) {
            nameContainer.setVisibility(View.VISIBLE);
            TaskName.setText("Time set for: " + task.getTaskName());
            TaskName.setVisibility(View.VISIBLE);
            Taskmsg.setVisibility(View.VISIBLE);
            mButtonGiveUp.setVisibility(View.VISIBLE);
            sheep.setVisibility(View.VISIBLE);
            mButtonStartPause.setVisibility(View.INVISIBLE);
            chicken.setVisibility(View.INVISIBLE);
            descContainer.setVisibility(View.INVISIBLE);
        }
        else {
            nameContainer.setVisibility(View.INVISIBLE);
            TaskName.setVisibility(View.INVISIBLE);
            Taskmsg.setVisibility(View.INVISIBLE);
            mButtonStartPause.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            mButtonGiveUp.setVisibility(View.INVISIBLE);
            sheep.setVisibility(View.INVISIBLE);
            chicken.setVisibility(View.VISIBLE);

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

    // SHARED PREFERENCES to ensure that timer continues to run even after the app is closed
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
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
        else {
            mTextViewCountDown.setText("00:00");
        }
    }



}