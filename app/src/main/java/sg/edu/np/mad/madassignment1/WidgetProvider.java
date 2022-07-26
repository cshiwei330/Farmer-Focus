package sg.edu.np.mad.madassignment1;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */

public class WidgetProvider extends AppWidgetProvider {

    public String GLOBAL_PREF = "MyPrefs";
    private Context context;
    private DBHandler dbHandler;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String GLOBAL_PREF, DBHandler dbHandler) {

        //Task Number
        ArrayList<Task> todayTaskList = new ArrayList<>();

        // getting stored username
        SharedPreferences sharedPreferences = context.getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        dbHandler = new DBHandler(context, null, null, 6);
        User user = dbHandler.findUser(username);

        // Fill taskList with current db data
        ArrayList<Task> taskList = new ArrayList<>();
        taskList = dbHandler.getTaskData((user.getUserID()));
        todayTaskList = findTodayTasks(taskList);

        // To see how many tasks
        int taskNo = todayTaskList.size();
        RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
        mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(taskNo)));

        // For List View
        Intent serviceIntent = new Intent(context, WidgetService.class);
        mainView.setRemoteAdapter(R.id.widgetListView, serviceIntent);
        mainView.setEmptyView(R.id.widgetListView, R.id.empty);


        // Add New Task Button
        //Intent buttonIntent = new Intent(context, TaskAddNewActivity.class);
        //PendingIntent pendingButtonIntent = PendingIntent.getActivity(context,0, buttonIntent,0);
        //mainView.setOnClickPendingIntent(R.id.widgetAddTaskButton, pendingButtonIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, mainView);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, GLOBAL_PREF, dbHandler);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static ArrayList<Task> findTodayTasks(ArrayList<Task> taskList){

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

    public static boolean isToday(String date) { //task.getTaskDate will be a string
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