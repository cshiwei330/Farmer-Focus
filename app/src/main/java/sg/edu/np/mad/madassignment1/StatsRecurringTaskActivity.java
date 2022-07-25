package sg.edu.np.mad.madassignment1;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityRecurringTaskStatsBinding;

public class StatsRecurringTaskActivity extends DrawerBaseActivity {
    ActivityRecurringTaskStatsBinding activityRecurringTaskStatsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityRecurringTaskStatsBinding = ActivityRecurringTaskStatsBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityRecurringTaskStatsBinding.getRoot());
        //set title
        allocateActivityTitle("Recurring Tasks");

        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        //getting task data
        ArrayList<Task> recurringTask = dbHandler.getRecurringTaskData(user.getUserID());
        ArrayList<String> taskNames = new ArrayList<>();
        ArrayList<Integer> timeTaken = new ArrayList<>();
        for (int i = 0; i < recurringTask.size(); i++) {
            taskNames.add(recurringTask.get(i).getTaskName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, taskNames);
        AutoCompleteTextView searchTask = findViewById(R.id.searchTasks);
        searchTask.setAdapter(adapter);

        searchTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String task = searchTask.getText().toString();
                    for (int i = 0; i < recurringTask.size(); i++) {
                        if(recurringTask.get(i).getTaskName().equals(task)) {
                            timeTaken.add((int)recurringTask.get(i).getTaskDuration());
                        }
                    }
                    ArrayList<Integer> last7Durations = new ArrayList<>();
                    last7Durations.addAll(timeTaken.subList(Math.max(timeTaken.size() - 7, 0), timeTaken.size()));

                    BarChart chart = findViewById(R.id.chart);
                    BarData data = new BarData(getDataSet(dbHandler, last7Durations));
                    chart.setData(data);
                    chart.getBarData().setBarWidth(0.5f);
                    chart.animateXY(2000, 2000);
                    //setting up x axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(10f);
                    xAxis.setTextColor(Color.BLACK);
                    xAxis.setDrawAxisLine(true);
                    xAxis.setDrawGridLines(false);
                    xAxis.setSpaceMin(0.25f);
                    xAxis.setLabelCount(7);
                    xAxis.setDrawLabels(false);
                    //setting up y axis
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setLabelCount(10);
                    yAxis.setStartAtZero(true);
                    //yAxis.setAxisMaxValue(10);
                    yAxis.setDrawGridLines(false);
                    //making sure there is only one y axis
                    chart.getAxisRight().setDrawLabels(false);
                    chart.getAxisRight().setDrawAxisLine(false);

                    chart.invalidate();
                }
                return false;
            }
        });


    }

    // get task
    // get durations
    // display last 7 durations
    // if not enough for last 7, show all the durations
    // for each task, get diff duration list
    private IBarDataSet getDataSet(DBHandler dbHandler, ArrayList<Integer> duration) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        //if there are full 7 values in the list
        if (duration.size() == 7) {
            BarEntry seven = new BarEntry(0, duration.get(0)); //7 attempts ago
            valueSet1.add(seven);
            BarEntry six = new BarEntry(1, duration.get(1)); //6 attempts ago
            valueSet1.add(six);
            BarEntry five = new BarEntry(2, duration.get(2)); //5 attempts ago
            valueSet1.add(five);
            BarEntry four = new BarEntry(3, duration.get(3)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(4, duration.get(4)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(5, duration.get(5)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(6)); //1 attempt ago
            valueSet1.add(one);
        }
        //if there are 6 values in the list
        else if (duration.size() == 6) {
            BarEntry six = new BarEntry(1, duration.get(0)); //6 attempts ago
            valueSet1.add(six);
            BarEntry five = new BarEntry(2, duration.get(1)); //5 attempts ago
            valueSet1.add(five);
            BarEntry four = new BarEntry(3, duration.get(2)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(4, duration.get(3)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(5, duration.get(4)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(5)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
        }
        //if there are 5 values in the list
        else if (duration.size() == 5) {
            BarEntry five = new BarEntry(2, duration.get(0)); //5 attempts ago
            valueSet1.add(five);
            BarEntry four = new BarEntry(3, duration.get(1)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(4, duration.get(2)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(5, duration.get(3)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(4)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
        }
        //if there are 4 values in the list
        else if (duration.size() == 4) {
            BarEntry four = new BarEntry(3, duration.get(0)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(4, duration.get(1)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(5, duration.get(2)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(3)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
        }
        //if there are 3 values in the list
        else if (duration.size() == 3) {
            BarEntry three = new BarEntry(4, duration.get(0)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(5, duration.get(1)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(2)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none3);
        }
        //if there are 2 values in the list
        else if (duration.size() == 2) {
            BarEntry two = new BarEntry(5, duration.get(0)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(6, duration.get(1)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none3);
            BarEntry none4 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none4);
        }
        //if there is only 1 value in the list
        else if (duration.size() == 1) {
            BarEntry one = new BarEntry(6, duration.get(0)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none3);
            BarEntry none4 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none4);
            BarEntry none5 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none5);
        }
        //if there is nothing in the list
        else{
            BarEntry one = new BarEntry(6,0); //not enough data
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none3);
            BarEntry none4 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none4);
            BarEntry none5 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none5);
        }


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Time taken each attempt");
        //barDataSet1.setColor(Color.rgb(50, 0, 50));

        return barDataSet1;
    }


}
