package sg.edu.np.mad.madassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import java.util.ArrayList;

import sg.edu.np.mad.madassignment1.databinding.ActivityCalenderBinding;

public class CalenderActivity extends DrawerBaseActivity {

    public static boolean filterCheck;

    ActivityCalenderBinding activityCalenderBinding;

    ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCalenderBinding = ActivityCalenderBinding.inflate(getLayoutInflater());
        setContentView(activityCalenderBinding.getRoot());
        allocateActivityTitle("Calender");

        CalendarView calendarView = findViewById(R.id.calendarView);

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);
        //fill taskList with db data
        taskList = dbHandler.getTaskData();
        //define recyclerView
        RecyclerView recyclerView = findViewById(R.id.calenderToDoListRecycleView);

        // initialize recyclerview
        //set adaptor to MyAdaptor, given taskList
        CalenderAdaptor calenderAdaptor = new CalenderAdaptor(taskList);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(calenderAdaptor);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                //i = year
                //i1 = month
                //i2 = day
                String dateString = String.format("%d/%d/%d",i2,i1+1,i);

                calenderAdaptor.getFilter().filter(dateString);
                //calenderAdaptor.notifyDataSetChanged();
            }
        });
    }
}