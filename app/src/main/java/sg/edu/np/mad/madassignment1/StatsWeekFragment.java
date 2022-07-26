package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class StatsWeekFragment extends Fragment{
    Context thisContext;
    private String GLOBAL_PREF = "MyPrefs";

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
        DBHandler dbHandler = new DBHandler(thisContext, null, null, 1);


        BarChart chart = view.findViewById(R.id.chart);

        BarData data = new BarData(getDataSet(dbHandler));
        chart.setData(data);
        chart.getBarData().setBarWidth(0.5f);
        chart.animateXY(2000, 2000);
        //setting chart description to be not visible
        chart.getDescription().setEnabled(false);
        //setting up x axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceMin(0.25f);
        xAxis.setLabelCount(7);
        xAxis.getValueFormatter().getFormattedValue(0, xAxis);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (String) getXAxisValues().get((int) value);
            }
        });
        //setting up y axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setLabelCount(10);
        yAxis.setStartAtZero(true);
        yAxis.setAxisMaxValue(10);
        yAxis.setDrawGridLines(false);
        //making sure there is only one y axis
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.invalidate();
        return view;
    }

    private IBarDataSet getDataSet(DBHandler dbHandler) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        SharedPreferences sharedPreferences = thisContext.getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);
        int userID = user.getUserID();


        try {
            Date today = sdf.parse(sdf.format(new Date()));
            Calendar cal = Calendar.getInstance();
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
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
                cal.add(Calendar.DATE, -1);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
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
                cal.add(Calendar.DATE, -2);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID);
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
                cal.add(Calendar.DATE, -3);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID);
                BarEntry mon = new BarEntry(1, tasksDone2); //mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID);
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
                cal.add(Calendar.DATE, -4);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID);
                BarEntry mon = new BarEntry(1, tasksDone2); //mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID);
                BarEntry tue = new BarEntry(2, tasksDone3); //tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID);
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
                cal.add(Calendar.DATE, -5);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID);
                BarEntry mon = new BarEntry(1, tasksDone2); // mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID);
                BarEntry tue = new BarEntry(2, tasksDone3); // tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID);
                BarEntry wed = new BarEntry(3, tasksDone4); // wed
                valueSet1.add(wed);
                cal.add(Calendar.DATE, 1);
                Date thursday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone5 = dbHandler.getTaskStatus(thursday, userID);
                BarEntry thu = new BarEntry(4, tasksDone5); // thu
                valueSet1.add(thu);
                BarEntry fri = new BarEntry(5, dayItself); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, 0); //sat
                valueSet1.add(sat);
            }
            //if mobile date is saturday
            else {
                cal.add(Calendar.DATE, -6);
                Date sunday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone1 = dbHandler.getTaskStatus(sunday, userID);
                BarEntry sun = new BarEntry(0, tasksDone1); // sun
                valueSet1.add(sun);
                cal.add(Calendar.DATE, 1);
                Date monday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone2 = dbHandler.getTaskStatus(monday, userID);
                BarEntry mon = new BarEntry(1, tasksDone2); // mon
                valueSet1.add(mon);
                cal.add(Calendar.DATE, 1);
                Date tuesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone3 = dbHandler.getTaskStatus(tuesday, userID);
                BarEntry tue = new BarEntry(2, tasksDone3); // tue
                valueSet1.add(tue);
                cal.add(Calendar.DATE, 1);
                Date wednesday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone4 = dbHandler.getTaskStatus(wednesday, userID);
                BarEntry wed = new BarEntry(3, tasksDone4); // wed
                valueSet1.add(wed);
                cal.add(Calendar.DATE, 1);
                Date thursday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone5 = dbHandler.getTaskStatus(thursday, userID);
                BarEntry thu = new BarEntry(4, tasksDone5); // thu
                valueSet1.add(thu);
                cal.add(Calendar.DATE, 1);
                Date friday = sdf.parse(sdf.format(cal.getTime()));
                int tasksDone6 = dbHandler.getTaskStatus(friday, userID);
                BarEntry fri = new BarEntry(5, tasksDone6); //fri
                valueSet1.add(fri);
                BarEntry sat = new BarEntry(6, dayItself); //sat
                valueSet1.add(sat);
            }
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "No. Of Tasks Done");
        barDataSet1.setDrawValues(false);
        barDataSet1.setColor(Color.rgb(162, 149, 116));

        return barDataSet1;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList<>();
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