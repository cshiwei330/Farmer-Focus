package sg.edu.np.mad.madassignment1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private String taskAlert, taskTitle, taskDesc;

    @Override
    public void onReceive(Context context, Intent intent) {

        taskAlert = intent.getStringExtra("task alert");
        taskDesc = intent.getStringExtra("task name");

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

        Intent i = new Intent(context, TaskActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "taskalertnotification")
                .setSmallIcon(R.drawable.barn_icon)
                .setContentTitle(taskTitle)
                .setContentText(taskDesc)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
