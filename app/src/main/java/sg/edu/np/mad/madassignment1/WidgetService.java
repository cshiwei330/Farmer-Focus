package sg.edu.np.mad.madassignment1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WidgetService extends RemoteViewsService {

    public String GLOBAL_PREF = "MyPrefs";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(getApplicationContext());
    }

    class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private final Context context;
        private DBHandler dbHandler;
        private ArrayList<Task> todayTaskList = new ArrayList<>();

        public WidgetRemoteViewsFactory(Context context) {
            this.context = context;
            this.dbHandler = new DBHandler(context,null,null,6);
        }

        @Override
        public void onCreate() {

            // getting stored username
            //SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_PREF, 0);
            //String username = sharedPreferences.getString("username", "");
            //User user = dbHandler.findUser(username);

            // Fill taskList with current db data
            //ArrayList<Task> taskList = new ArrayList<>();
            //taskList = dbHandler.getTaskData((user.getUserID()));
            //todayTaskList = findTodayTasks(taskList);

        }

        @Override
        public void onDataSetChanged() {
            // getting stored username
            SharedPreferences sharedPreferences = context.getSharedPreferences(dbHandler.GLOBAL_PREF, 0);
            String username = sharedPreferences.getString("username", "");
            User user = dbHandler.findUser(username);

            // Fill taskList with current db data
            ArrayList<Task> taskList = new ArrayList<>();
            taskList = dbHandler.getTaskData((user.getUserID()));
            todayTaskList = findTodayTasks(taskList);
        }

        @Override
        public void onDestroy() {
            todayTaskList.clear();
        }

        @Override
        public int getCount() {
            return todayTaskList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Task t = todayTaskList.get(position);
            @SuppressLint("RemoteViewLayout") RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_today_task_item);

            remoteView.setTextViewText(R.id.widgetTaskName, t.getTaskName());
            remoteView.setTextViewText(R.id.widgetTaskTime, t.getTaskStartTime());


            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return todayTaskList.get(position).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public ArrayList<Task> findTodayTasks(ArrayList<Task> taskList){

            ArrayList<Task> recentTaskList = new ArrayList<>();

            for (int i = 0; i < taskList.size(); i++){ //loop thru current taskList to find tasks that are upcoming
                Task task = taskList.get(i);

                boolean result = isToday(task.getTaskDate()); //check if task date is within a week

                if (result){
                    recentTaskList.add(task); //if true then add to new list
                }

            }

            return recentTaskList;
        }

        public boolean isToday(String date) { //task.getTaskDate will be a string
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
                Date dateToValidate = sdf.parse(date); //convert the string to a Date

                // current date with same formatting as dateToValidate
                Date today = sdf.parse(sdf.format(new Date()));

                //.getTime() returns a Date so comparison can be made
                if (dateToValidate.compareTo(today)==0){ //tasks that are today are considered within a week
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
}
