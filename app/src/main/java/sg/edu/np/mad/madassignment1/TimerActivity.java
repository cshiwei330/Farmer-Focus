package sg.edu.np.mad.madassignment1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import sg.edu.np.mad.madassignment1.databinding.ActivityTimerBinding;

public class TimerActivity extends DrawerBaseActivity {

    //define activity binding
    ActivityTimerBinding activityTimerBinding;

    //time
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private TextView mTextSetTime;
    private ImageView SetTime;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;


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

        SetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(TimerActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000; // change to minutes
                if (millisInput <= 0) {
                    Toast.makeText(TimerActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

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

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        //closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

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

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            SetTime.setVisibility(View.INVISIBLE);
            mTextSetTime.setVisibility(View.INVISIBLE);
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mTextSetTime.setVisibility(View.VISIBLE);
            SetTime.setVisibility(View.VISIBLE);
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }
    }

//    private void closeKeyboard() {
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }

}