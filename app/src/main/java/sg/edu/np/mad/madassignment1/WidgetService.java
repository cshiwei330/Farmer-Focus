package sg.edu.np.mad.madassignment1;

import static java.lang.Integer.valueOf;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*----------------------------------------------------------------------------------------------------*/

/* WidgetService */
/* This is a service that powers the list view through remote view. I get all the tasks that are today from
 * the DBHandler to display them on the widget. Only the task name and time is shown so as to create a
 * minimalistic look on their homescreen. */

/*----------------------------------------------------------------------------------------------------*/

public class WidgetService extends RemoteViewsService {

    public String GLOBAL_PREF = "MyPrefs";
    private String TAG = "WidgetService";

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
            SharedPreferences sharedPreferences = context.getSharedPreferences(dbHandler.GLOBAL_PREF, 0);
            String username = sharedPreferences.getString("username", "");
            User user = dbHandler.findUser(username);

            //create todayTaskList
            try {
                todayTaskList = dbHandler.getTodayTaskData(user.getUserID());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDataSetChanged() {

            todayTaskList.clear();

            // getting stored username
            SharedPreferences sharedPreferences = context.getSharedPreferences(dbHandler.GLOBAL_PREF, 0);
            String username = sharedPreferences.getString("username", "");
            User user = dbHandler.findUser(username);

            //create todayTaskList
            try {
                todayTaskList = dbHandler.getTodayTaskData(user.getUserID());
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_today_task_item);

            remoteView.setTextViewText(R.id.widgetTaskName, t.getTaskName());
            remoteView.setTextViewText(R.id.widgetTaskTime, t.getTaskStartTime());

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        // return one as there is only one widget view
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

    }
}
