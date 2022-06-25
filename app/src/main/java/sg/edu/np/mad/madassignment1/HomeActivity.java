package sg.edu.np.mad.madassignment1;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.TextView;

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
        //String username = User.getUsername();
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

        // initialize recyclerview
        //set adaptor to MyAdaptor, given recentTaskList
        MyAdaptor mAdaptor = new MyAdaptor(findRecentTasks(recentTaskList));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

    }

    public ArrayList<Task> findRecentTasks (ArrayList<Task> taskList){

        ArrayList<Task> recentTaskList = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++){
            Task task = taskList.get(i);

            //form date string from obj
//            String checkDate = String.format("%d/%d/%d",task.getTaskDayOfMonth(),task.getTaskMonth(),task.getTaskYear());

            //check if within a week
//            boolean result = withinAWeek(checkDate);
            boolean result = withinAWeek(task.getTaskDate());

            if (result){
                recentTaskList.add(task);
            }

        }

        return recentTaskList;
    }

    public boolean withinAWeek (String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateToValidate = sdf.parse(date);

            // current date with same formatting as dateToValidate
            Date today = sdf.parse(sdf.format(new Date()));

            // current date after 1 week
            Calendar currentDateAfter1Week = Calendar.getInstance();
            currentDateAfter1Week.add(Calendar.DAY_OF_MONTH, 7);

            // current date before 1 week
            //Calendar currentDateBefore1Week = Calendar.getInstance();
            //currentDateBefore1Week.add(Calendar.DAY_OF_MONTH, -7);

            //if (date.before(currentDateAfter1Week.getTime())
                    //&& date.after(currentDateBefore1Week.getTime()))

            if (dateToValidate.after(today) && dateToValidate.before(currentDateAfter1Week.getTime())){
                //ok everything is fine, date in range
                return true;
            }
            else if (dateToValidate.compareTo(today)==0){
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
}