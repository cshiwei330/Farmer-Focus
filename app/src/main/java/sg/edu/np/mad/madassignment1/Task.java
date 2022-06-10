package sg.edu.np.mad.madassignment1;

public class Task {
    private int Id;
    private boolean status;
    private String taskName;
    private String taskDesc;

    public Task(int Id, boolean status, String taskName, String taskDesc) {
        this.Id = Id;
        this.status = status;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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
}
