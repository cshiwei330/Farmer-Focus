package sg.edu.np.mad.madassignment1;

import android.content.Context;
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
    private static final int MAX_X_VALUE = 7;
    private static final float BAR_WIDTH = 0.2f;

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
        //data.setBarWidth(1000f);
        chart.setData(data);
        chart.getBarData().setBarWidth(0.5f);
        //chart.
        //chart.setDescription(NoOfTaskChart);
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
        xAxis.getValueFormatter().getFormattedValue(0, xAxis);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (String) getXAxisValues().get((int) value);
            }
        });
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(DAYS));
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
        //ArrayList<BarDataSet> dataSets = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //set the date formatter
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        try {
            Date today = sdf.parse(sdf.format(new Date()));
            Calendar cal = Calendar.getInstance();
            //cal.setTime(today);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            //cal.add( Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek );
            //  int tasksDone = dbHandler.getTaskStatus(today); // gives count of completed tasks


            for (int i = 0; i < MAX_X_VALUE; i++) {

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                cal.get(Calendar.DAY_OF_WEEK); //-3
                cal.add(Calendar.DAY_OF_WEEK,1);
                if(dayOfWeek == 5) {
                    cal.set(Calendar.DAY_OF_WEEK, 1);
                    int tasksDone2 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry mon = new BarEntry(i, tasksDone2); // mon
                    valueSet1.add(mon);
                    cal.set(Calendar.DAY_OF_WEEK, 2);
                    int tasksDone22 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry tue = new BarEntry(i, tasksDone22); // tues
                    valueSet1.add(tue);
                    cal.set(Calendar.DAY_OF_WEEK, 3);
                    int tasksDone3 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry wed = new BarEntry(i, tasksDone3); // tues
                    valueSet1.add(wed);
                    cal.set(Calendar.DAY_OF_WEEK, 4);
                    int tasksDone4 = dbHandler.getTaskStatus(today);
                    BarEntry thu = new BarEntry(i, tasksDone4); // tues
                    valueSet1.add(thu);
                    cal.set(Calendar.DAY_OF_WEEK, 5);
                    int tasksDone5 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry fri = new BarEntry(i, tasksDone5); // tues
                    valueSet1.add(fri);
                    cal.set(Calendar.DAY_OF_WEEK, 6);
                    int tasksDone6 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry sat = new BarEntry(i, tasksDone6); // tues
                    valueSet1.add(sat);
                    cal.set(Calendar.DAY_OF_WEEK, 7);
                    int tasksDone7 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry sun = new BarEntry(i, tasksDone7); // tues
                    valueSet1.add(sun);

                }
                // think need to do for all?? hardcode or what

                /*if(dayOfWeek == 2) {
                    cal.set(Calendar.DAY_OF_WEEK, 2);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                    cal.add(Calendar.DAY_OF_WEEK, 2);
                    int tasksDone22 = dbHandler.getTaskStatus(cal.getTime());
                    BarEntry tue = new BarEntry(i, tasksDone22); // tues
                    valueSet1.add(tue);
                }
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                cal.add(Calendar.DAY_OF_WEEK,3);
                int tasksDone222 = dbHandler.getTaskStatus(cal.getTime());
                BarEntry wed = new BarEntry(i, tasksDone222); // wed
                valueSet1.add(wed);

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                cal.add(Calendar.DAY_OF_WEEK,4);
                int tasksDone2222 = dbHandler.getTaskStatus(cal.getTime());
                BarEntry thu = new BarEntry(i, tasksDone2222); // thurs
                valueSet1.add(thu);

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                cal.add(Calendar.DAY_OF_WEEK,5);
                int tasksDone22222 = dbHandler.getTaskStatus(cal.getTime());
                BarEntry fri = new BarEntry(i, tasksDone22222); // fri
                valueSet1.add(fri);

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                cal.add(Calendar.DAY_OF_WEEK,6);
                int tasksDone3 = dbHandler.getTaskStatus(cal.getTime());
                BarEntry sat = new BarEntry(i, tasksDone3); // sat
                valueSet1.add(sat);

                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfWeek);
                int tasksDone = dbHandler.getTaskStatus(cal.getTime());
                BarEntry sun = new BarEntry(i, tasksDone); // sun
                valueSet1.add(sun);*/
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "No. Of Tasks Done");
        //barDataSet1.setColor(Color.rgb(50, 0, 50));

        return barDataSet1;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList<>();
        xAxis.add("MON");
        xAxis.add("TUE");
        xAxis.add("WED");
        xAxis.add("THU");
        xAxis.add("FRI");
        xAxis.add("SAT");
        xAxis.add("SUN");
        return xAxis;
    }
}