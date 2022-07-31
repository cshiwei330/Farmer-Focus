/*----------------------------------------------------------------------------------------------------*/

                                        /* HOME ACTIVITY*/
/* This Fragment shows a mood selector whereby users can input their mood for the day. It also shows
* upcoming tasks for the users in a recycler view. Tasks are swipeable so users can choose to edit or delete
* the task without navigating to the Task. When these actions are performed, I have a method that updates the
* app widget. I also have a grass animation that makes the home page more lively. */

/*----------------------------------------------------------------------------------------------------*/


package sg.edu.np.mad.madassignment1;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.edu.np.mad.madassignment1.databinding.ActivityHomeBinding;

public class HomeActivity extends DrawerBaseActivity {

    public String GLOBAL_PREF = "MyPrefs";


    //define activity binding
    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate according to activity binding to show
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        //set view to this activity
        setContentView(activityHomeBinding.getRoot());
        //set title
        allocateActivityTitle("Home");

        //define dbHandler
        DBHandler dbHandler = new DBHandler(this, null, null,6);

        // getting stored username
        SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        User user = dbHandler.findUser(username);

        // For Greetings
        TextView greetings = findViewById(R.id.greetings);
        greetings.setText("What's up, " + username + "!");

        //define recyclerView
        RecyclerView recyclerView = findViewById(R.id.recentTasksRecyclerView);

        //Finding recent tasks
        //define taskList array
        ArrayList<Task> taskList = new ArrayList<>();
        //current taskList with db data
        taskList = dbHandler.getTaskData(user.getUserID());
        //use method
        ArrayList<Task> recentTaskList = findRecentTasks(taskList);

        // toast to indicate to user there are no recent tasks
        if (recentTaskList.size() == 0){
            Toast.makeText(this, "Hooray! No Upcoming Tasks.", Toast.LENGTH_SHORT).show();
        }

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        TaskRecyclerViewAdaptor mAdaptor = new TaskRecyclerViewAdaptor(recentTaskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        //For Swiping Menu
        TaskRecyclerTouchListener touchListener = new TaskRecyclerTouchListener(this, recyclerView);
        touchListener.setClickable(new TaskRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                //Toast.makeText(getApplicationContext(),taskList.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.edit_task, R.id.delete_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new TaskRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) throws ParseException {

                        Task task = recentTaskList.get(position);

                        switch (viewID){
                            case R.id.edit_task:
                                Bundle extras = new Bundle();
                                Intent myIntent = new Intent(HomeActivity.this, TaskEditActivity.class);
                                extras.putInt("Task id", task.getId());
                                myIntent.putExtras(extras);
                                startActivity(myIntent);

                                // update widget
                                updateWidgets(getApplicationContext(), dbHandler, user, task);

                                break;

                            case R.id.delete_task:
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dbHandler.deleteTask(task);

                                        //to "refresh" the change
                                        Intent myIntent = new Intent(HomeActivity.this, HomeActivity.class);
                                        startActivity(myIntent);

                                        //toast to indicate tasks successfully cleared
                                        Toast.makeText(HomeActivity.this, "Deleted Task", Toast.LENGTH_LONG).show();

                                        // update widget
                                        try {
                                            updateWidgets(getApplicationContext(), dbHandler, user, task);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Delete task");
                                alert.show();
                                mAdaptor.notifyDataSetChanged();
                                break;
                        }

                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);


        //for grass animation
        ImageView grass = findViewById(R.id.grassGif);
        Glide.with(this).load(R.drawable.homepagegrass).into(grass);

    }

    //Method to find tasks that are upcoming based on tasklist
    public ArrayList<Task> findRecentTasks (ArrayList<Task> taskList){

        ArrayList<Task> recentTaskList = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++){ //loop thru current taskList to find tasks that are upcoming
            Task task = taskList.get(i);

            boolean result = withinAWeek(task.getTaskDate()); //check if task date is within a week

            if (result){
                recentTaskList.add(task); //if true then add to new list
            }

        }

        return recentTaskList; //display this list in the recyclerView
    }

    //Method to return boolean if the task date is within a week
    public boolean withinAWeek (String date) { //task.getTaskDate will be a string
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
            Date dateToValidate = sdf.parse(date); //convert the string to a Date

            // current date with same formatting as dateToValidate
            Date today = sdf.parse(sdf.format(new Date()));

            // current date after 1 week
            Calendar currentDateAfter1Week = Calendar.getInstance();
            currentDateAfter1Week.add(Calendar.DAY_OF_MONTH, 7);

                                                                    //.getTime() returns a Date so comparison can be made
            if (dateToValidate.after(today) && dateToValidate.before(currentDateAfter1Week.getTime())){ //define the date range
                //ok everything is fine, date in range
                return true;
            }
            else if (dateToValidate.compareTo(today)==0){ //tasks that are today are considered within a week
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Method to update app widget

    public void updateWidgets (Context context, DBHandler dbHandler, User user, Task task) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
        // current date with same formatting as task.getDate()
        Date today = sdf.parse(sdf.format(new Date()));
        Date dateToValidate = sdf.parse(task.getTaskDate()); //convert the string to a Date


        //update the widget if task date is today
        if (dateToValidate.compareTo(today)==0){

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);

            RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
            try {
                mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(dbHandler.getTodayTaskData(user.getUserID()).size())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            appWidgetManager.updateAppWidget(thisWidget, mainView);

        }
    }
}