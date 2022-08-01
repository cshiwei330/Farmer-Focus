package sg.edu.np.mad.madassignment1;

/*----------------------------------------------------------------------------------------------------*/

                                        /* STOPWATCH ACTIVITY */
/* This activity is created for users to track their time taken to complete an activity with a inbuilt stopwatch.
Once the stopwatch has started, a cow gif will show up on the screen to show that the stopwatch is currently running.
WHen the stopwatch has started, the start button will change to pause to allow users to pause the stopwatch whenever
they want to. In the event when users want to reset the stopwatch, they could pause the stopwatch first then click
on reset for the stopwatch to go back down to 00:00. */

/*----------------------------------------------------------------------------------------------------*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import sg.edu.np.mad.madassignment1.databinding.ActivityStopWatchBinding;

public class StopWatchActivity extends DrawerBaseActivity {
    ActivityStopWatchBinding activityStopWatchBinding;

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private Button mButtonStartPause2;
    private Button mButtonReset2;
    private Button mButtonTimer;
    private Button mButtonStopWatch;

    private ImageView cow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        //inflate according to activity binding to show
        activityStopWatchBinding = ActivityStopWatchBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityStopWatchBinding.getRoot());
        //set title
        allocateActivityTitle("Stop Watch");

        // Stopwatch
        mButtonStartPause2 = findViewById(R.id.button_start_pause2);
        mButtonReset2 = findViewById(R.id.button_reset2);
        chronometer = findViewById(R.id.chronometer);
        mButtonTimer = findViewById(R.id.timer2);
        mButtonStopWatch = findViewById(R.id.stopwatch2);

        mButtonTimer.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonNotClicked));
        mButtonStopWatch.setBackgroundColor(getResources().getColor(R.color.taskCompletionButtonClicked));

//        //for cow animation
//        cow = findViewById(R.id.cow);
//        Glide.with(this).load(R.drawable.cow).into(cow);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // set stopwatch to 23h 59mins 59secs
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 86399000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    //Toast.makeText(MainActivity.this, "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // if timer is running, start button changes to become pause, else, start button will remain unchanged
        mButtonStartPause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    pauseChronometer();
                } else {
                    startChronometer();
                }
            }
        });

        // reset the timer (this button will only be shown once countdown timer has started and after the user
        // clicked the pause button)
        mButtonReset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetChronometer();
            }
        });

        // for navigation back to the timer
        mButtonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StopWatchActivityToTimerActivity = new Intent(StopWatchActivity.this, TimerActivity.class);
                //put extra
                StopWatchActivityToTimerActivity.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    //when result code =1, received from bundle, kill this activity
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        StopWatchActivity.this.finish();
                    }
                });
                //start activity with result
                startActivityForResult(StopWatchActivityToTimerActivity, 1);

            }
        });

    }

    // starting of the stopwatch
    private void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            updateWatchInterface();
        }
    }

    // when pause is clicked
    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            updateWatchInterface();
        }
    }

    // when reset is clicked set time back to 00:00
    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        updateWatchInterface();
    }

    // cow gif only runs when stopwatch is running and start button will change to
    // pause when it is running
    private void updateWatchInterface() {
        if (running) {
            //for cow animation
            cow = findViewById(R.id.cow);
            Glide.with(this).load(R.drawable.cow).into(cow);
            cow.setVisibility(View.VISIBLE);
            mButtonStartPause2.setText("Pause");
        }
        else {
            cow.setVisibility(View.INVISIBLE);
            mButtonStartPause2.setText("Start");
        }
    }


}