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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class StatsWeekFragment extends Fragment {
    Context thisContext;

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
        sg.edu.np.mad.madassignment1.DBHandler dbHandler = new sg.edu.np.mad.madassignment1.DBHandler(thisContext, null, null, 1);


        BarChart chart = view.findViewById(R.id.chart);

        BarData data = new BarData(getDataSet(dbHandler));
        chart.setData(data);
        //chart.setDescription("NoOfTaskChart");
        chart.animateXY(2000, 2000);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setValueFormatter(new MyCustomFormatter());
        chart.invalidate();

        return view;
    }

    private IBarDataSet getDataSet(DBHandler dbHandler) {
        ArrayList<BarDataSet> dataSets = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //set the date formatter
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        try {
            Date today = sdf.parse(sdf.format(new Date()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int tasksDone = dbHandler.getTaskStatus(today); // gives count of completed tasks

            if(dayOfWeek == 1) {
                BarEntry sun = new BarEntry(110.000f, tasksDone); // sun
                valueSet1.add(sun);
            }
            else if(dayOfWeek == 2) {
                BarEntry mon = new BarEntry(40.000f, tasksDone); // mon
                valueSet1.add(mon);
            }
            else if(dayOfWeek == 3) {
                BarEntry tue = new BarEntry(60.000f, tasksDone); // tues
                valueSet1.add(tue);
            }
            else if(dayOfWeek == 4) {
                BarEntry wed = new BarEntry(30.000f, tasksDone); // wed
                valueSet1.add(wed);
            }
            else if(dayOfWeek == 5) {
                BarEntry thu = new BarEntry(90.000f, tasksDone); // thurs
                valueSet1.add(thu);
            }
            else if(dayOfWeek == 6) {
                BarEntry fri = new BarEntry(100.000f, tasksDone); // fri
                valueSet1.add(fri);
            }
            else{
                BarEntry sat = new BarEntry(120.000f, tasksDone); // sat
                valueSet1.add(sat);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "No. Of Tasks Done");
        barDataSet1.setColor(Color.rgb(100, 0, 100));
        ;
        return barDataSet1;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList<>();
        xAxis.add("Mon");
        xAxis.add("Tue");
        xAxis.add("Wed");
        xAxis.add("Thu");
        xAxis.add("Fri");
        xAxis.add("Sat");
        xAxis.add("Sun");
        //BarDataSet xAxisSet = new BarDataSet(xAxis, "Brand 1");
        return xAxis;
    }
}