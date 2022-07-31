/*
RECURRING TASK STATISTICS ACTIVITY
This activity is strictly for recurring tasks only. It has a search view, where the users can search for their recurring tasks. Once the user
enters the task name, a bar chart of the time taken per attempt of that specific recurring task will appear. The bar chart will only show up to
the last 7 attempts to prevent over populating the chart.
 */

package sg.edu.np.mad.madassignment1;

import androidx.annotation.RequiresApi;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.stream.Collectors;

import sg.edu.np.mad.madassignment1.databinding.ActivityRecurringTaskStatsBinding;

public class StatsRecurringTaskActivity extends DrawerBaseActivity {
    ActivityRecurringTaskStatsBinding activityRecurringTaskStatsBinding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate according to activity binding to show
        activityRecurringTaskStatsBinding = ActivityRecurringTaskStatsBinding.inflate(getLayoutInflater());
        // set view to this activity
        setContentView(activityRecurringTaskStatsBinding.getRoot());
        // set title
        allocateActivityTitle("Recurring Tasks");

        //defining DBHandler
        DBHandler dbHandler = new DBHandler(this, null, null, 1);

        // shared preferences to get userid
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        // getting task data and storing in a list
        ArrayList<Task> recurringTask = dbHandler.getRecurringTaskData(user.getUserID());
        Log.v("size of recurringTask", String.valueOf(recurringTask.size()));

        // initialising variables to be used
        ArrayList<String> taskNames = new ArrayList<>();
        ArrayList<String> noDuplicates;
        ArrayList<Long> timeTaken = new ArrayList<>();

        for (int i = 0; i < recurringTask.size(); i++) {
            // separating the names from the whole task object
            taskNames.add(recurringTask.get(i).getTaskName());
        }
        // only adding one of the many repeated names of the recurring tasks
        noDuplicates = (ArrayList<String>) taskNames.stream().distinct().collect(Collectors.toList());

        // setting the dropdown list for the recommended search
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, noDuplicates);
        // calling the auto complete text view from the xml
        AutoCompleteTextView searchTask = findViewById(R.id.searchTasks);
        searchTask.setAdapter(adapter);

        // setting the tick / enter button on user to be onclick
        searchTask.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    // getting the task that is entered by the user
                    String task = searchTask.getText().toString();
                    for (int i = 0; i < recurringTask.size(); i++) {
                        // converting the time taken in the database from milliseconds to minutes
                        long timeInMin = recurringTask.get(i).getTaskDuration() / 60000;
                        // if the time taken is not 0
                        if(recurringTask.get(i).getTaskName().equals(task) && timeInMin != 0) {
                            // adding the time taken to a list
                            timeTaken.add(timeInMin);
                        }
                    }
                    // creating a new list to store the last 7 values of the time taken list
                    ArrayList<Long> last7Durations = new ArrayList<>();
                    last7Durations.addAll(timeTaken.subList(Math.max(timeTaken.size() - 7, 0), timeTaken.size()));

                    BarChart chart = findViewById(R.id.chart); // calling the chart from the xml

                    BarData data = new BarData(getDataSet(last7Durations)); //setting the dataset to be displayed in the chart
                    chart.setData(data);

                    chart.getBarData().setBarWidth(0.5f); //setting the width of the bars
                    chart.animateXY(2000, 2000); //setting animation duration
                    chart.getDescription().setEnabled(false); //setting chart description to be not visible

                    //setting up x axis
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //setting the position of the x axis to be at the bottom
                    xAxis.setDrawAxisLine(true); //showing the line of x axis
                    xAxis.setDrawGridLines(false); // removing x axis texts
                    xAxis.setSpaceMin(0.25f); //setting the space between each x value
                    xAxis.setLabelCount(7); //setting the number of x values that can be shown on the chart
                    xAxis.setDrawLabels(false);

                    //setting up y axis
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setStartAtZero(true); //making sure that the y axis starts from 0
                    yAxis.setDrawGridLines(false); //showing the y axis

                    //removing second y axis
                    chart.getAxisRight().setDrawLabels(false);
                    chart.getAxisRight().setDrawAxisLine(false);

                    chart.invalidate();
                }
                return false;
            }
        });
    }

    // making a method to get the data to be displayed in the bar chart
    private IBarDataSet getDataSet(ArrayList<Long> duration) {
        // creating a new list to be used
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
            BarEntry six = new BarEntry(0, duration.get(0)); //6 attempts ago
            valueSet1.add(six);
            BarEntry five = new BarEntry(1, duration.get(1)); //5 attempts ago
            valueSet1.add(five);
            BarEntry four = new BarEntry(2, duration.get(2)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(3, duration.get(3)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(4, duration.get(4)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(5, duration.get(5)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(6, 0); //not enough data
            valueSet1.add(none);
        }
        //if there are 5 values in the list
        else if (duration.size() == 5) {
            BarEntry five = new BarEntry(0, duration.get(0)); //5 attempts ago
            valueSet1.add(five);
            BarEntry four = new BarEntry(1, duration.get(1)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(2, duration.get(2)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(3, duration.get(3)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(4, duration.get(4)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(5, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none1);
        }
        //if there are 4 values in the list
        else if (duration.size() == 4) {
            BarEntry four = new BarEntry(0, duration.get(0)); //4 attempts ago
            valueSet1.add(four);
            BarEntry three = new BarEntry(1, duration.get(1)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(2, duration.get(2)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(3, duration.get(3)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(4, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(5, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none2);
        }
        //if there are 3 values in the list
        else if (duration.size() == 3) {
            BarEntry three = new BarEntry(0, duration.get(0)); //3 attempts ago
            valueSet1.add(three);
            BarEntry two = new BarEntry(1, duration.get(1)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(2, duration.get(2)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(3, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(4, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(5, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none3);
        }
        //if there are 2 values in the list
        else if (duration.size() == 2) {
            BarEntry two = new BarEntry(0, duration.get(0)); //2 attempts ago
            valueSet1.add(two);
            BarEntry one = new BarEntry(1, duration.get(1)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(2, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(3, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(4, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(5, 0); //not enough data
            valueSet1.add(none3);
            BarEntry none4 = new BarEntry(6, 0); //not enough data
            valueSet1.add(none4);
        }
        //if there is only 1 value in the list
        else if (duration.size() == 1) {
            BarEntry one = new BarEntry(0, duration.get(0)); //1 attempt ago
            valueSet1.add(one);
            BarEntry none = new BarEntry(1, 0); //not enough data
            valueSet1.add(none);
            BarEntry none1 = new BarEntry(2, 0); //not enough data
            valueSet1.add(none1);
            BarEntry none2 = new BarEntry(3, 0); //not enough data
            valueSet1.add(none2);
            BarEntry none3 = new BarEntry(4, 0); //not enough data
            valueSet1.add(none3);
            BarEntry none4 = new BarEntry(5, 0); //not enough data
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

        //creating a new BarDataSet object
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Time taken each attempt (min)");
        //removing data labels
        barDataSet1.setDrawValues(false);
        //setting colours of bars
        barDataSet1.setColor(Color.rgb(162, 149, 116));

        return barDataSet1;
    }
}
