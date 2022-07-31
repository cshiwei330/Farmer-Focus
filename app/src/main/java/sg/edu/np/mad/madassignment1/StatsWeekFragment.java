/*
STATISTICS OF THE WEEK FRAGMENT
This fragment is the bar chart of the number of completed tasks per day. The bar chart shows the data for the week, from Sunday to Saturday.
If the user has yet to complete a task within the week, nothing will show up on the bar chart.
 */

package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatsWeekFragment extends Fragment{
    Context thisContext;
    private final String GLOBAL_PREF = "MyPrefs";

    public StatsWeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_week, container, false);
        thisContext = container.getContext();
        // defining DBHandler
        DBHandler dbHandler = new DBHandler(thisContext, null, null, 1);

        BarChart chart = view.findViewById(R.id.chart); // calling the chart from the xml

        BarData data = new BarData(getDataSet(dbHandler)); //setting the dataset to be displayed in the chart
        chart.setData(data);
        chart.getBarData().setBarWidth(0.5f); //setting the width of the bars
        chart.animateXY(2000, 2000); //setting animation duration
        chart.getDescription().setEnabled(false); //setting chart description to be not visible

        //setting up x axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //setting the position of the x axis to be at the bottom
        xAxis.setTextSize(10f); //setting the text size of x axis
        xAxis.setTextColor(Color.BLACK); //setting the text colour of the x axis
        //showing the x axis
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceMin(0.25f); //setting the space between each x value
        xAxis.setLabelCount(7); //setting the number of x values that can be shown on the chart
        //setting values of x axis
        xAxis.getValueFormatter().getFormattedValue(0, xAxis);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //calling the method to populate the x axis
                return (String) getXAxisValues().get((int) value);
            }
        });
        //setting up y axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(10); //setting 10 values on y axis
        yAxis.setStartAtZero(true); //making sure that the y axis starts from 0
        yAxis.setAxisMaxValue(10); //setting the maximum y value to be 10
        yAxis.setDrawGridLines(false); //showing the y axis

        //removing second y axis
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.invalidate();
        return view;
    }

    // making a method to get the data to be displayed in the bar chart
    private IBarDataSet getDataSet(DBHandler dbHandler) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //setting the date formatter
        // creating a new list to be used
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        // shared preferences to get userid
        SharedPreferences sharedPreferences = thisContext.getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);
        int userID = user.getUserID();

        // a try catch for parse exception
        try {
            Date today = sdf.parse(sdf.format(new Date())); // getting today's date
            Calendar cal = Calendar.getInstance(); // getting a calendar date
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // seeing which day of the week it is today
            int dayItself = dbHandler.getTaskStatus(today, userID); // gives count of completed tasks

            //if mobile date is sunday
            if (dayOfWeek == 1) {
                BarEntry sun = new BarEntry(0, dayItself); //sun
                valueSet1.add(sun);
                BarEntry mon = new BarEntry(1, 0); //mon
                valueSet1.add(mon);
                BarEntry tue = new BarEntry(2, 0); //tue
                valueSet1.add(tue);
                BarEntry wed = new BarEntry(3, 0); //wed
                valueSet1.add(wed);
                BarEntry thu = new BarEntry(4, 0); //thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, 0); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is monday
            else if (dayOfWeek == 2) {
                cal.add(Calendar.DATE, -1); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                BarEntry mon = new BarEntry(1, dayItself); //mon
                valueSet1.add(mon);
                BarEntry tue = new BarEntry(2, 0); //tue
                valueSet1.add(tue);
                BarEntry wed = new BarEntry(3, 0); //wed
                valueSet1.add(wed);
                BarEntry thu = new BarEntry(4, 0); //thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, 0); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is tuesday
            else if (dayOfWeek == 3) {
                cal.add(Calendar.DATE, -2); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID); // gives count of completed tasks
                BarEntry mon = new BarEntry(1, tasksDone2); //mon
                valueSet1.add(mon);
                BarEntry tue = new BarEntry(2, dayItself); //tue
                valueSet1.add(tue);
                BarEntry wed = new BarEntry(3, 0); //wed
                valueSet1.add(wed);
                BarEntry thu = new BarEntry(4, 0); //thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, 0); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is wednesday
            else if (dayOfWeek == 4) {
                cal.add(Calendar.DATE, -3); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID); // gives count of completed tasks
                BarEntry mon = new BarEntry(1, tasksDone2); //mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID); // gives count of completed tasks
                BarEntry tue = new BarEntry(2, tasksDone3); //tue
                valueSet1.add(tue);
                BarEntry wed = new BarEntry(3, dayItself); //wed
                valueSet1.add(wed);
                BarEntry thu = new BarEntry(4, 0); //thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, 0); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is thursday
            else if (dayOfWeek == 5) {
                cal.add(Calendar.DATE, -4); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID); // gives count of completed tasks
                BarEntry mon = new BarEntry(1, tasksDone2); //mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID); // gives count of completed tasks
                BarEntry tue = new BarEntry(2, tasksDone3); //tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID); // gives count of completed tasks
                BarEntry wed = new BarEntry(3, tasksDone4); //wed
                valueSet1.add(wed);
                BarEntry thu = new BarEntry(4, dayItself); //thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, 0); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is friday
            else if (dayOfWeek == 6) {
                cal.add(Calendar.DATE, -5); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID); // gives count of completed tasks
                BarEntry mon = new BarEntry(1, tasksDone2); // mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID); // gives count of completed tasks
                BarEntry tue = new BarEntry(2, tasksDone3); // tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID); // gives count of completed tasks
                BarEntry wed = new BarEntry(3, tasksDone4); // wed
                valueSet1.add(wed);
                cal.add(Calendar.DATE, 1);
                Date thursday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone5 = dbHandler.getTaskStatus(thursday, userID); // gives count of completed tasks
                BarEntry thu = new BarEntry(4, tasksDone5); // thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, dayItself); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is saturday
            else {
                cal.add(Calendar.DATE, -6); // going back to sunday
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID); // gives count of completed tasks
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID); // gives count of completed tasks
                BarEntry mon = new BarEntry(1, tasksDone2); // mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID); // gives count of completed tasks
                BarEntry tue = new BarEntry(2, tasksDone3); // tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID); // gives count of completed tasks
                BarEntry wed = new BarEntry(3, tasksDone4); // wed
                valueSet1.add(wed);
                cal.add(Calendar.DATE, 1);
                Date thursday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone5 = dbHandler.getTaskStatus(thursday, userID); // gives count of completed tasks
                BarEntry thu = new BarEntry(4, tasksDone5); // thu
                valueSet1.add(thu);
                cal.add(Calendar.DATE, 1);
                Date friday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone6 = dbHandler.getTaskStatus(friday, userID); // gives count of completed tasks
                BarEntry fri = new BarEntry(5, tasksDone6); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, dayItself); //sat
                valueSet1.add(sat);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            }

        //creating a new BarDataSet object
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "No. Of Tasks Done");
        //removing data labels
        barDataSet1.setDrawValues(false);
        //setting colours of bars
        barDataSet1.setColor(Color.rgb(162, 149, 116));

        return barDataSet1;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("SUN");
        xAxis.add("MON");
        xAxis.add("TUE");
        xAxis.add("WED");
        xAxis.add("THU");
        xAxis.add("FRI");
        xAxis.add("SAT");
        return xAxis;
    }
}