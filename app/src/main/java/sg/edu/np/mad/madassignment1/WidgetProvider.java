package sg.edu.np.mad.madassignment1;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */

public class WidgetProvider extends AppWidgetProvider {

    private String TAG = "WidgetProvider";

    public String GLOBAL_PREF = "MyPrefs";
    private Context context;
    private DBHandler dbHandler;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String GLOBAL_PREF, DBHandler dbHandler) throws ParseException {

        //Task Number
        ArrayList<Task> todayTaskList = new ArrayList<>();

        // getting stored username
        SharedPreferences sharedPreferences = context.getSharedPreferences(GLOBAL_PREF, 0);
        String username = sharedPreferences.getString("username", "");
        dbHandler = new DBHandler(context, null, null, 6);
        User user = dbHandler.findUser(username);

        // Fill taskList with current db data
        todayTaskList = dbHandler.getTodayTaskData(user.getUserID());

        // To display number of task
        int taskNo = todayTaskList.size();
        RemoteViews mainView = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
        mainView.setTextViewText(R.id.widgetTaskNo, ("   " + String.valueOf(taskNo)));

        // For List View
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        mainView.setRemoteAdapter(R.id.widgetListView, serviceIntent);
        mainView.setEmptyView(R.id.widgetListView, R.id.empty);

        // Launch app when widget is clicked
        Intent configIntent = new Intent(context, HomeActivity.class);
        PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        mainView.setOnClickPendingIntent(R.id.widgetTaskNo, launchAppPendingIntent);
        mainView.setOnClickPendingIntent(R.id.widgetTodayText, launchAppPendingIntent);
        mainView.setOnClickPendingIntent(R.id.widgetDivider, launchAppPendingIntent);
        mainView.setOnClickPendingIntent(R.id.widgetAddTaskButton, launchAppPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, mainView);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update all of widgets
        for (int appWidgetId : appWidgetIds) {

            Log.v(TAG, "Update");

            try {
                updateAppWidget(context, appWidgetManager, appWidgetId, GLOBAL_PREF, dbHandler);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // will trigger onDataSetChanged() in WidgetService
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetListView);

            Log.v(TAG, "Update completed");
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

}