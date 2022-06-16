package sg.edu.np.mad.madassignment1;

public class Task {
    private int Id;
    private int status;
    private String taskName;
    private String taskDesc;

    private int TaskHour;
    private int TaskMinute;
    private int TaskYear;
    private int TaskMonth;
    private int TaskDayOfMonth;

    public Task(){}

    public Task(String taskName, int taskHour, int taskMinute, int taskYear, int taskMonth) {
        this.taskName = taskName;
        TaskHour = taskHour;
        TaskMinute = taskMinute;
        TaskYear = taskYear;
        TaskMonth = taskMonth;
    }

    public Task(int Id, int status, String taskName, String taskDesc, int taskHour, int taskMinute, int taskYear, int taskMonth, int taskDayOfMonth) {
        this.Id = Id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;

        this.TaskHour = taskHour;
        this.TaskMinute = taskMinute;
        this.TaskYear = taskYear;
        this.TaskMonth = taskMonth;
        this.TaskDayOfMonth = taskDayOfMonth;
    }

    public int getId() { return Id; }

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

    public int getTaskHour() { return TaskHour; }

    public void setTaskHour(int taskHour) {
        TaskHour = taskHour;
    }

    public int getTaskMinute() { return TaskMinute; }

    public void setTaskMinute(int taskMinute) { TaskMinute = taskMinute; }

    public int getTaskYear() { return TaskYear; }

    public void setTaskYear(int taskYear) { TaskYear = taskYear; }

    public int getTaskMonth() { return TaskMonth; }

    public void setTaskMonth(int taskMonth) { TaskMonth = taskMonth; }

    public int getTaskDayOfMonth() { return TaskDayOfMonth; }

    public void setTaskDayOfMonth(int taskDayOfMonth) { TaskDayOfMonth = taskDayOfMonth; }
}
