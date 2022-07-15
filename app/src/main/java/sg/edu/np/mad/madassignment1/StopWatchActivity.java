package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

public class StopWatchActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private Button mButtonStartPause2;
    private Button mButtonReset2;
    private TextView mTimeTextView;

    private boolean mTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);

        // Stopwatch
        mButtonStartPause2 = findViewById(R.id.button_start_pause2);
        mButtonReset2 = findViewById(R.id.button_reset2);
        chronometer = findViewById(R.id.chronometer);

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

        // navigate back to timer activity
        ImageView Timer = findViewById(R.id.backToTimer);
        Timer.setOnClickListener(new View.OnClickListener() {
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

    private void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            updateWatchInterface();
        }
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            updateWatchInterface();
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        updateWatchInterface();
    }

    private void updateWatchInterface() {
        if (running) {
            //mButtonReset2.setVisibility(View.INVISIBLE);
            mButtonStartPause2.setText("Pause");
        }
        else {
            mButtonStartPause2.setText("Start");
        }
    }

}