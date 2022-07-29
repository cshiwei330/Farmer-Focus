package sg.edu.np.mad.madassignment1;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    private String taskAlert, taskTitle, taskDesc;
    private int taskId;
    NotificationManagerCompat notificationManagerCompat;
    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieve data from the intent from the add new task activity
        taskAlert = intent.getStringExtra("task alert");
        taskDesc = intent.getStringExtra("task name");
        taskId = intent.getIntExtra("task id", -1);

        /* Assign the correct title to the notification based on the
         alert specified by the user for the current task */
        if (taskAlert.matches("At time of event")){
            taskTitle = "Task";
        }
        else if (taskAlert.matches("5 minutes before")){
            taskTitle = "Task in 5 Mins!";
        }
        else if (taskAlert.matches("10 minutes before")){
            taskTitle = "Task in 10 Mins!";
        }
        else if (taskAlert.matches("15 minutes before")){
            taskTitle = "Task in 15 Mins!";
        }
        else if (taskAlert.matches("30 minutes before")){
            taskTitle = "Task in 30 Mins!";
        }
        else if (taskAlert.matches("1 hour before")){
            taskTitle = "Task in an hour!";
        }
        else if (taskAlert.matches("1 day before")){
            taskTitle = "Task in a day!";
        }
        else {
            taskTitle = "Task in a week!";
        }

        // Set intent for the notification when clicked
        // The user will be brought to the task list page when notification is clicked
        Intent i = new Intent(context, TaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,taskId,i,PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        // Give the notification its attributes
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "taskalertnotification")
                .setSmallIcon(R.drawable.barn_icon)
                .setContentTitle(taskTitle)
                .setContentText(taskDesc)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        // Create the notification
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(taskId, builder.build());
    }
}
