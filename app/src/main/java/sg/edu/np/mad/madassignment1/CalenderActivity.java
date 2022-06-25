package sg.edu.np.mad.madassignment1;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;


import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;

import sg.edu.np.mad.madassignment1.databinding.ActivityCalenderBinding;

public class CalenderActivity extends DrawerBaseActivity implements CalenderViewAdaptor.OnItemListener{

    ActivityCalenderBinding activityCalenderBinding;

    ArrayList<Task> taskList = new ArrayList<>();

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;


    private Calendar calendar;
    private Date currentDate;

    private CalenderAdaptor calenderAdaptor;
    private CalenderViewAdaptor calendarViewAdapter;
    private RecyclerView recyclerView;

    private TextView todayTasksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCalenderBinding = ActivityCalenderBinding.inflate(getLayoutInflater());
        setContentView(activityCalenderBinding.getRoot());
        allocateActivityTitle("Calender");

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);
        //fill taskList with db data
        taskList = dbHandler.getTaskData();

        //define recyclerView
        recyclerView = findViewById(R.id.calenderToDoListRecycleView);

        // initialize recyclerview
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //set adaptor to MyAdaptor, given taskList
        calenderAdaptor = new CalenderAdaptor(taskList);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(calenderAdaptor);

        //set recycler view to be invisible until a date has been selected
        recyclerView.setVisibility(View.GONE);

        //calenderView
        //initialize views
        initWidgets();

        //set text for number of task on date
        todayTasksTextView.setText("Select a date to see tasks!");

        //get current date
        calendar = Calendar.getInstance();

        //set recyclerview
        setMonthView();

    }
    //initialize views for calenderView
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        todayTasksTextView = findViewById(R.id.todayTasksTextView);
    }
    //set the top textView and date numbers, initialize the recyclerView inside calenderView
    private void setMonthView()
    {
        //get current date
        currentDate = calendar.getTime();

        //set date format
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        //apply date format to current date
        String monthDayText = df.format(currentDate);
        //set text
        monthYearText.setText(monthDayText);

        //get number of days in a month
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //get array of length 42 that have blanks to show in recyclerView
        ArrayList<String> daysInMonth = daysInMonthArray(numberOfDaysInMonth);
        //get all the tasks that are in this month
        ArrayList<String> tasksThisMonth = tasksThisMonth(taskList);

        //initialize recyclerview
        calendarViewAdapter = new CalenderViewAdaptor(daysInMonth, CalenderActivity.this,tasksThisMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarViewAdapter);
    }

    //method to convert number of days to array with spacing
    private ArrayList<String> daysInMonthArray(int numberOfDaysInMonth)
    {
        //define array
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        //gets day int, monday =0, tuesday =1
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //make array of date numbers
        //42 is number of calender cells
        for(int i = 1; i <= 42; i++)
        {
            //less than first day of week, OR greater than total days in month, add empty
            if(i <= dayOfWeek || i > numberOfDaysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            //else, add the month date number
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    //cycle month backwards
    public void previousMonthAction(View view)
    {
        //remove one month from calendar
        calendar.add(Calendar.MONTH, -1);
        //reset recyclerView
        setMonthView();
    }

    //cycle month backwards
    public void nextMonthAction(View view)
    {
        //remove one month from calendar
        calendar.add(Calendar.MONTH, 1);
        //reset recyclerView
        setMonthView();
    }

    //when a date is clicked
    @Override
    public void onItemClick(int position, String dayText)
    {
        //if not a blank date cell is clicked
        if(!dayText.equals(""))
        {
            //set recycler view to be visible when a date has been selected
            if (recyclerView.getVisibility()== View.GONE){
                recyclerView.setVisibility(View.VISIBLE);
            }

            //set date format
            SimpleDateFormat df = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
            //apply date format to current date
            String dateString = df.format(currentDate);
            String stringDate = dayText+"/"+dateString;

            //set text to see how many tasks
            int numberOfSameDateTasks = tasksWhiteList(stringDate,taskList).size();
            todayTasksTextView.setText("You have " + numberOfSameDateTasks + " tasks on " + stringDate);

            //set shading and todolist filters
            calendarViewAdapter.getFilter().filter(stringDate);
            calenderAdaptor.getFilter().filter(stringDate);
            Log.v("Calender button clicked",stringDate);



        }
    }
    //filter tasks for selected date
    public ArrayList<Task> tasksWhiteList(String dateString, ArrayList<Task> taskList){
        //split string into day,month,year
        String[] stringDateArr = dateString.split("/",3);
        //force initialize to null
        int[] intDateArr = new int[stringDateArr.length];
        //convert stingDateArr to int
        for(int i = 0;i < stringDateArr.length;i++)
        {
            //intDateArr[0] == dayOfMonth
            //intDateArr[1] == month
            //intDateArr[2] == year
            intDateArr[i] = Integer.parseInt(stringDateArr[i]);
        }
        //check all tasks in data to filter date, if date is the same, add to filter
        ArrayList<Task> taskFilter = new ArrayList<>();
        for(Task task: taskList){
            Log.v("Filter",task.getTaskYear()+ "/" + (task.getTaskMonth()) + "/" + task.getTaskDayOfMonth());
            if(task.getTaskYear() == intDateArr[2] &&
                    task.getTaskMonth() == intDateArr[1] &&
                    task.getTaskDayOfMonth() == intDateArr[0]){
                taskFilter.add(task);
            }
        }
        return taskFilter;
    }

    public ArrayList<String> tasksThisMonth(ArrayList<Task> taskList){
        ArrayList<String> tasksInThisMonth = new ArrayList<>();
        for(Task task: taskList){
            if(task.getTaskYear() == calendar.get(Calendar.YEAR) && task.getTaskMonth() == calendar.get(Calendar.MONTH)+1){
                tasksInThisMonth.add(String.valueOf(task.getTaskDayOfMonth()));
            }
        }
        return tasksInThisMonth;
    }
}