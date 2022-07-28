package sg.edu.np.mad.madassignment1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = "DB Handler";

    //database
    public static String DATABASE_NAME = "accountsDB.db";

    //accounts
    public static String ACCOUNTS = "Accounts"; //table name
    public static String COLUMN_USERNAME = "Username";
    public static String COLUMN_PASSWORD = "Password";
    public static String COLUMN_IMAGEID = "ImageId";

    public static String COLUMN_USERID = "UserID";

    //tasks
    public static String TASKS = "Tasks";
    public static String COLUMN_TASKID = "TaskId";
    public static String COLUMN_TASKSTATUS = "TaskStatus";
    public static String COLUMN_TASKNAME = "TaskName";
    public static String COLUMN_TASKDESC = "TaskDesc";

    public static String COLUMN_TASKDATE ="TaskDate";
    public static String COLUMN_TASKSTARTTIME ="TaskStartTime";
    public static String COLUMN_TASKENDTIME ="TaskEndTime";
    public static String COLUMN_TASKDURATION = "TaskDuration";
    public static String COLUMN_TASKALERT = "TaskAlert";
    public static String COLUMN_TASKALERTDATETIME = "TaskAlertDateTime";
    public static String COLUMN_TASKTYPE = "TaskType";
    public static String COLUMN_TASKREPEAT = "TaskRepeat";
    public static String COLUMN_RECURRINGID = "RecurringId";
    public static String COLUMN_RECURRINGDURATION = "RecurringDuration";
    public static String COLUMN_TASKUSERID = "taskUserID";

    //mood tracker
    public static String MOODTRACKER = "MoodTracker";
    public static String COLUMN_MOODDATE = "MoodDate";
    public static String COLUMN_MOOD = "Mood";

    //farm
    public static String FARM = "Farm";
    public static String COLUMN_BARNLEVEL = "BarnLevel";
    public static String COLUMN_SILOLEVEL = "SiloLevel";
    public static String COLUMN_SILONUMBER = "SiloNumber";

    public static int DATABASE_VERSION = 6;

    public String GLOBAL_PREF = "MyPrefs";

    //define DBHandler
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //create tables
    public void onCreate(SQLiteDatabase db){

        String CREATE_DATABASE = "CREATE TABLE " + ACCOUNTS + "("
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_IMAGEID + " TEXT, "
                + COLUMN_USERID
                + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + ")";
        // CREATE TABLE Accounts ( Username TEXT, Password TEXT, ImageId TEXT, UserID INTEGER)

        String CREATE_DATABASE_TASK = "CREATE TABLE " + TASKS + "("
                + COLUMN_TASKID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASKSTATUS + " INTEGER, "
                + COLUMN_TASKNAME + " TEXT, "
                + COLUMN_TASKDESC + " TEXT, "
                + COLUMN_USERID + " INTEGER, "
                + COLUMN_TASKDATE + " STRING, "
                + COLUMN_TASKSTARTTIME + " STRING, "
                + COLUMN_TASKENDTIME + " STRING, "
                + COLUMN_TASKDURATION + " LONG, "
                + COLUMN_TASKALERT + " STRING, "
                + COLUMN_TASKALERTDATETIME + " STRING, "
                + COLUMN_TASKTYPE + " STRING, "
                + COLUMN_TASKREPEAT + " STRING, "
                + COLUMN_RECURRINGID + " INTEGER, "
                + COLUMN_RECURRINGDURATION + " STRING, "
                + "FOREIGN KEY ("+COLUMN_USERID+") REFERENCES "+ACCOUNTS +" ("+COLUMN_USERID+")"
                + ")";

        // FOR MOOD TRACKER
        String CREATE_DATABASE_MOODTRACKER = "CREATE TABLE " + MOODTRACKER + "(" + COLUMN_MOODDATE + " TEXT," + COLUMN_MOOD + " TEXT" + ")";
        // CREATE TABLE MoodTracker ( MoodDate TEXT, Mood TEXT )

        // FOR MY FARM
        String CREATE_DATABASE_MY_FARM = "CREATE TABLE " + FARM + "("
                + COLUMN_USERID + " INTEGER, "
                + COLUMN_BARNLEVEL + " STRING, "
                + COLUMN_SILOLEVEL + " STRING "
                + ")";
        // CREATE TABLE Farm ( UserID TEXT, BarnLevel INTEGER, SiloLevel INTEGER)


        //execute sql commands
        db.execSQL(CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE_TASK);
        db.execSQL(CREATE_DATABASE_MOODTRACKER);
        db.execSQL(CREATE_DATABASE_MY_FARM);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);
        onCreate(db);
    }

    public User findUser(String username){
        String query = "SELECT * FROM " + ACCOUNTS + " WHERE " + COLUMN_USERNAME + "=\"" + username + "\"";
        // select * from Accounts where username = "??"
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User queryData = new User();

        if(cursor.moveToFirst()){
            queryData.setUsername(cursor.getString(0));
            queryData.setPassword(cursor.getString(1));
            queryData.setImageID(cursor.getInt(2));
            queryData.setUserID(cursor.getInt(3));
            cursor.close();
        }
        else {
            queryData = null;
        }
        db.close();
        return queryData;
    }

    // adding user data into user table created
    public void addUser(User userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_PASSWORD, userData.getPassword());
        //values.put(COLUMN_IMAGEID, userData.getImageID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ACCOUNTS, null, values);
        db.close();
    }

    public void updateUser(User userDBData){
        SQLiteDatabase db = this.getWritableDatabase();
        // update password
        String update = "UPDATE " + ACCOUNTS + " SET " + COLUMN_PASSWORD + " = " + "\""+ userDBData.getPassword()+ "\""  + " WHERE " + COLUMN_USERNAME + " = " + "\""+ userDBData.getUsername()+ "\"";
        db.execSQL(update);
        db.close();
    }
    //the naming convention here is so non existent im actually crying from this
    public void updateProfile(User userDBData){
        SQLiteDatabase db = this.getWritableDatabase();
        // update profile picture
        String update = "UPDATE " + ACCOUNTS + " SET " + COLUMN_IMAGEID + " = " + "\""+ userDBData.getImageID()+ "\""  + " WHERE " + COLUMN_USERNAME + " = " + "\""+ userDBData.getUsername()+ "\"";
        db.execSQL(update);
        db.close();
    }

    public void updateDuration(Task t){
        SQLiteDatabase db = this.getWritableDatabase();
        String update = "UPDATE " + TASKS + " SET " + COLUMN_TASKDURATION + " = " + "\"" + t.getTaskDuration() + "\"" + " WHERE " + COLUMN_TASKID + " = " + "\"" + t.getId() + "\"";
        db.execSQL(update);
        db.close();
    }

    // adding task data into task table created
    public void addTask(Task taskData){
        ContentValues values = new ContentValues();
        //values.put(COLUMN_TASKID, taskData.getId());
        values.put(COLUMN_TASKSTATUS, taskData.getStatus());
        values.put(COLUMN_TASKNAME, taskData.getTaskName());
        values.put(COLUMN_TASKDESC, taskData.getTaskDesc());

        values.put(COLUMN_TASKDATE, taskData.getTaskDate());
        values.put(COLUMN_TASKSTARTTIME, taskData.getTaskStartTime());
        values.put(COLUMN_TASKENDTIME, taskData.getTaskEndTime());
        values.put(COLUMN_TASKDURATION, taskData.getTaskDuration());
        values.put(COLUMN_TASKALERT, taskData.getAlert());
        values.put(COLUMN_TASKALERTDATETIME, taskData.getAlertDateTime());
        values.put(COLUMN_TASKTYPE, taskData.getTaskType());
        values.put(COLUMN_TASKREPEAT, taskData.getRepeat());
        values.put(COLUMN_RECURRINGID, taskData.getRecurringId());
        values.put(COLUMN_RECURRINGDURATION, taskData.getRecurringDuration());
        values.put(COLUMN_USERID, taskData.getTaskUserID());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TASKS, null, values);
        Log.v(TAG, "Added to db");
        db.close();
    }

    public ArrayList<Task> getTaskData(int userID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> taskArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TASKS + " WHERE " + COLUMN_USERID + "=" + userID;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int status = cursor.getInt(1);
            String name = cursor.getString(2);
            String desc = cursor.getString(3);

            String taskDate = cursor.getString(5);
            String taskStartTime = cursor.getString(6);
            String taskEndTime = cursor.getString(7);
            long taskDuration = cursor.getLong(8);
            String taskAlert = cursor.getString(9);
            String taskAlertDateTime = cursor.getString(10);
            String taskType = cursor.getString(11);
            String taskRepeat = cursor.getString(12);
            int recurringId = cursor.getInt(13);
            String recurringDuration = cursor.getString(14);

            Task newTask = new Task(id, status, name, desc, taskDate, taskStartTime, taskEndTime, taskDuration, taskAlert,
                    taskAlertDateTime, taskType, taskRepeat, recurringId, recurringDuration, userID);
            taskArrayList.add(newTask);
        }
        db.close();
        return taskArrayList;
    }

    public Task findTask(int taskID){
        String query = "SELECT * FROM " + TASKS + " WHERE " + COLUMN_TASKID + "=" + taskID;
        // select * from Accounts where taskId = "??"
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Task queryData = new Task();

        if(cursor.moveToFirst()){
            queryData.setId(cursor.getInt(0));
            queryData.setStatus(cursor.getInt(1));
            queryData.setTaskName(cursor.getString(2));
            queryData.setTaskDesc(cursor.getString(3));
            queryData.setTaskUserID(cursor.getInt(4));
            queryData.setTaskDate(cursor.getString(5));
            queryData.setTaskStartTime(cursor.getString(6));
            queryData.setTaskEndTime(cursor.getString(7));
            queryData.setTaskDuration(cursor.getLong(8));
            queryData.setAlert(cursor.getString(9));
            queryData.setAlertDateTime(cursor.getString(10));
            queryData.setTaskType(cursor.getString(11));
            queryData.setRepeat(cursor.getString(12));
            queryData.setRecurringId(cursor.getInt(13));
            queryData.setRecurringDuration(cursor.getString(14));

        }
        else {
            queryData = null;
        }

        db.close();
        return queryData;
    }

    public void deleteTask(Task deleteTask){ // String taskName, String taskDesc
        SQLiteDatabase db = this.getWritableDatabase();

        //shortened .delete command to delete task by id
        db.delete("Tasks","TaskId=?", new String[]{String.valueOf(deleteTask.getId())});
    }

    public void deleteAllTask(int userID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TASKS + " WHERE " + COLUMN_USERID + "=" + userID);
        db.close();
    }

    public void editTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TASKNAME, task.getTaskName());
        contentValues.put(COLUMN_TASKDESC, task.getTaskDesc());
        contentValues.put(COLUMN_TASKDATE, task.getTaskDate());
        contentValues.put(COLUMN_TASKSTARTTIME, task.getTaskStartTime());
        contentValues.put(COLUMN_TASKENDTIME, task.getTaskEndTime());
        contentValues.put(COLUMN_TASKDURATION, task.getTaskDuration());
        contentValues.put(COLUMN_TASKALERT, task.getAlert());
        contentValues.put(COLUMN_TASKALERTDATETIME, task.getAlertDateTime());
        contentValues.put(COLUMN_TASKTYPE, task.getTaskType());
        contentValues.put(COLUMN_TASKREPEAT, task.getRepeat());
        contentValues.put(COLUMN_RECURRINGID, task.getRecurringId());
        contentValues.put(COLUMN_RECURRINGDURATION, task.getRecurringDuration());

        db.update("TASKS", contentValues, COLUMN_TASKID + " = ?", new String[]{String.valueOf(task.getId())});

        db.close();
    }

    // adding mood entry to moodtracker table
    public void addMood(Mood moodData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOODDATE, moodData.getDate());
        values.put(COLUMN_MOOD, moodData.getMood());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(MOODTRACKER, null, values);
        //Log.v(TAG, "Added to db");
        db.close();
    }

    // put mood data into list
    public ArrayList<Mood> getMoodData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Mood> moodArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + MOODTRACKER, null);
        while (cursor.moveToNext()){
            String date = cursor.getString(0);
            String mood = cursor.getString(1);
            Mood newMoodEntry = new Mood(date, mood);
            moodArrayList.add(newMoodEntry);
        }
        db.close();
        return moodArrayList;
    }

    //edit the data entry if user wants to change their mood
    public void changeMood (Mood editedMood){
        SQLiteDatabase db = this.getWritableDatabase();
        // edit Mood entry
        String changeMood = "UPDATE " + MOODTRACKER + " SET " + COLUMN_MOOD + " = " + "\""+ editedMood.getMood()+ "\""  + " WHERE " + COLUMN_MOODDATE + " = " + "\""+ editedMood.getDate()+ "\"";
        db.execSQL(changeMood);
        db.close();
    }

    public void changeTaskStatus (Task taskStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        // edit Task status
        String changeTaskStatus = "UPDATE " + TASKS + " SET " + COLUMN_TASKSTATUS + " = " + "\""+ taskStatus.getStatus()+ "\""  + " WHERE " + COLUMN_TASKID + " = " + "\""+ taskStatus.getId()+ "\"";
        db.execSQL(changeTaskStatus);
        db.close();
    }

    public int getTaskStatus(Date date, int userID) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateParsed = sdf.format(date);
        String getTaskStatus = "SELECT " + COLUMN_TASKSTATUS + " FROM " + TASKS + " WHERE " + COLUMN_TASKSTATUS + " = 1"
               + " AND " + COLUMN_TASKDATE + " = " + " \"" + dateParsed + "\"" + " AND " + COLUMN_USERID + " = " + userID;
        //select column task status from tasks where taskstatus == 1 (complete)
        Cursor cursor = db.rawQuery(getTaskStatus, null);
        int count = cursor.getCount();
        db.close();
        return count;
    }

    public int returnHighestRecurringId(int userId) {
        ArrayList<Task> taskList =  getTaskData(userId);
        ArrayList<Integer> recurringIdList = new ArrayList<>();

        if (taskList.size() == 0) {
            return 0;
        }

        for (int i=0; i<taskList.size(); i++) {
            int idToAdd =  taskList.get(i).getRecurringId();
            recurringIdList.add(idToAdd);
        }

        Collections.sort(recurringIdList, Collections.reverseOrder());
        return recurringIdList.get(0);
    }

    public ArrayList<Task> getRecurringTaskData(int userID){
        ArrayList<Task> recurring_task = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String countTask = "SELECT * FROM "+ TASKS + " WHERE " + COLUMN_TASKNAME +
                " IN(" + " SELECT " + COLUMN_TASKNAME + " FROM " + TASKS + " GROUP BY " + COLUMN_TASKNAME + " HAVING COUNT(*) > 1)" +
                " AND " + COLUMN_USERID + " = " + userID;
        Cursor cursor = db.rawQuery(countTask,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int status = cursor.getInt(1);
            String name = cursor.getString(2);
            String desc = cursor.getString(3);

            String taskDate = cursor.getString(5);
            String taskStartTime = cursor.getString(6);
            String taskEndTime = cursor.getString(7);
            long taskDuration = cursor.getLong(8);
            String taskAlert = cursor.getString(9);
            String taskAlertDateTime = cursor.getString(10);
            String taskType = cursor.getString(11);
            String taskRepeat = cursor.getString(12);
            int recurringId = cursor.getInt(13);
            String recurringDuration = cursor.getString(14);

            Task newTask = new Task(id, status, name, desc, taskDate, taskStartTime, taskEndTime, taskDuration, taskAlert,
                    taskAlertDateTime, taskType, taskRepeat, recurringId, recurringDuration, userID);
            recurring_task.add(newTask);
        }
        db.close();
        return recurring_task;
    }

    public ArrayList<Task> getTodayTaskData(int userID) throws ParseException {

        ArrayList<Task> taskList = getTaskData(userID);
        ArrayList<Task> todayTaskList = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++){ //loop thru current taskList to find tasks that are today
            Task task = taskList.get(i);

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); //set the date formatter
                Date dateToValidate = sdf.parse(task.getTaskDate()); //convert the string to a Date

                // current date with same formatting as dateToValidate
                Date today = sdf.parse(sdf.format(new Date()));

                if (dateToValidate.compareTo(today)==0){ // check if tasks that are today
                    todayTaskList.add(task); //if true then add to new list;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return todayTaskList;
    }

    public void addFarm(User userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERID, userData.getUserID());
        values.put(COLUMN_BARNLEVEL, "0");
        values.put(COLUMN_SILOLEVEL, "0:0:0:0");

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(FARM, null, values);
        db.close();
    }

    public ArrayList<String> findFarm(int userID){
        String query = "SELECT * FROM " + FARM + " WHERE " + COLUMN_USERID + "=" + userID;
        // select * from Farm where UserId = "??"
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String>farmData = new ArrayList<String>();

        if(cursor.moveToFirst()){
            farmData.add(cursor.getString(1));
            farmData.add(cursor.getString(2));
        }
        return farmData;
    }

    public void upgradeBarn(Integer userID, Integer upgradeLevel){
        SQLiteDatabase db = this.getWritableDatabase();
        // upgrade barn
        String update = "UPDATE " + FARM + " SET " + COLUMN_BARNLEVEL + " = " + "\""+ upgradeLevel+ "\""  + " WHERE " + COLUMN_USERID + " = " + "\""+ userID+ "\"";
        db.execSQL(update);
        db.close();
    }
}
