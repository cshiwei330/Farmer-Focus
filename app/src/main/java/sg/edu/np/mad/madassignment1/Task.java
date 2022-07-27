package sg.edu.np.mad.madassignment1;

import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Task {

    private String TAG = "Task Class";

    private int Id;
    private int status;
    private String taskName;
    private String taskDesc;

    private String taskDate;
    private String taskStartTime;
    private String taskEndTime;
    private long taskDuration;
    private String alert;
    private String alertDateTime;
    private String taskType;
    private String repeat;

    private int recurringId;

    private String recurringDuration;

    private int taskUserID;

    public Task() {
    }


//    public Task(int Id, int status, String taskName, String taskDesc, int taskHour, int taskMinute, int taskYear, int taskMonth, int taskDayOfMonth) {
    public Task(int Id, int status, String taskName, String taskDesc, String taskDate, String taskStartTime, String taskEndTime, long taskDuration, String alert,
                String alertDateTime, String taskType, String repeat, int recurringId, String recurringDuration, int taskUserID) {
        this.Id = Id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;

        this.taskDate = taskDate;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskDuration = taskDuration;

        this.alert = alert;
        this.alertDateTime = alertDateTime;

        this.taskType = taskType;
        this.repeat = repeat;

        this.recurringId = recurringId;

        this.recurringDuration = recurringDuration;

        this.taskUserID = taskUserID;

    }

    public int getTaskUserID() {
        return taskUserID;
    }

    public void setTaskUserID(int taskUserID) {
        this.taskUserID = taskUserID;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public double getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(long taskDuration) {
        this.taskDuration = taskDuration;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAlertDateTime() {
        return alertDateTime;
    }

    public void setAlertDateTime(String alertDateTime) {
        this.alertDateTime = alertDateTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getRecurringId() {
        return recurringId;
    }

    public void setRecurringId(int recurringId) {
        this.recurringId = recurringId;
    }

    public String getRecurringDuration() {
        return recurringDuration;
    }

    public void setRecurringDuration(String recurringDuration) {
        this.recurringDuration = recurringDuration;
    }

    public static Comparator<Task> TaskNameAscComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getTaskName().compareToIgnoreCase(t2.getTaskName());
        }
    };

    public static Comparator<Task> TaskNameDescComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t2.getTaskName().compareToIgnoreCase(t1.getTaskName());
        }
    };

    public static Comparator<Task> TaskIdAscComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getId() - t2.getId();
        }
    };

    public static Comparator<Task> TaskIdDescComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t2.getId() - t1.getId();
        }
    };

    public static Comparator<Task> TaskDateAscComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {

            Date date1 = null;
            Date date2 = null;

            String t1Date = t1.getTaskDate();
            String[] splitT1Date = t1Date.split("/");

            String t2Date = t2.getTaskDate();
            String[] splitT2Date = t2Date.split("/");

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                date1 = sdf.parse(splitT1Date[2] + "-" + splitT1Date[1] + "-" + splitT1Date[0]);
                date2 = sdf.parse(splitT2Date[2] + "-" + splitT2Date[1] + "-" + splitT2Date[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return date1.compareTo(date2);
        }
    };

    public static Comparator<Task> TaskDateDescComparator = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {

            Date date1 = null;
            Date date2 = null;

            String t1Date = t1.getTaskDate();
            String[] splitT1Date = t1Date.split("/");

            String t2Date = t2.getTaskDate();
            String[] splitT2Date = t2Date.split("/");

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                date1 = sdf.parse(splitT1Date[2] + "-" + splitT1Date[1] + "-" + splitT1Date[0]);
                date2 = sdf.parse(splitT2Date[2] + "-" + splitT2Date[1] + "-" + splitT2Date[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return date2.compareTo(date1);
        }
    };
}

