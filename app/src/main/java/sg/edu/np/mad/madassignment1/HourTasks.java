package sg.edu.np.mad.madassignment1;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourTasks {
    private String time;
    private ArrayList<Task> tasks;

    public HourTasks(String time, ArrayList<Task> tasks) {
        this.time = time;
        this.tasks = tasks;
    }

    public HourTasks() { }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

}
