package sg.edu.np.mad.madassignment1;

public class Task {
    private int Id;
    private int status;
    private String taskName;
    private String taskDesc;

    private int taskHour;
    private int taskMinute;
    private int taskYear;
    private int taskMonth;
    private int taskDayOfMonth;

    public Task() {
    }


    public Task(int Id, int status, String taskName, String taskDesc, int taskHour, int taskMinute, int taskYear, int taskMonth, int taskDayOfMonth) {
        this.Id = Id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;

        this.taskHour = taskHour;
        this.taskMinute = taskMinute;
        this.taskYear = taskYear;
        this.taskMonth = taskMonth;
        this.taskDayOfMonth = taskDayOfMonth;
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

    public int getTaskHour() {
        return taskHour;
    }

    public void setTaskHour(int taskHour) {
        this.taskHour = taskHour;
    }

    public int getTaskMinute() {
        return taskMinute;
    }

    public void setTaskMinute(int taskMinute) {
        this.taskMinute = taskMinute;
    }

    public int getTaskYear() {
        return taskYear;
    }

    public void setTaskYear(int taskYear) {
        this.taskYear = taskYear;
    }

    public int getTaskMonth() {
        return taskMonth;
    }

    public void setTaskMonth(int taskMonth) {
        this.taskMonth = taskMonth;
    }

    public int getTaskDayOfMonth() {
        return taskDayOfMonth;
    }

    public void setTaskDayOfMonth(int taskDayOfMonth) {
        this.taskDayOfMonth = taskDayOfMonth;
    }

}
