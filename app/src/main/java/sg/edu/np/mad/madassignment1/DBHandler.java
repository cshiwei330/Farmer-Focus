package sg.edu.np.mad.madassignment1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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

    public static String COLUMN_HOUR ="TaskHour";
    public static String COLUMN_MINUTE ="TaskMinute";
    public static String COLUMN_YEAR ="TaskYear";
    public static String COLUMN_MONTH ="TaskMonth";
    public static String COLUMN_DAYOFMONTH ="TaskDayOfMonth";

    public static String COLUMN_TASKDATE ="TaskDate";
    public static String COLUMN_TASKTIME ="TaskTime";
    public static String COLUMN_TASKUSERID = "taskUserID";

    //mood tracker
    public static String MOODTRACKER = "MoodTracker";
    public static String COLUMN_MOODDATE = "MoodDate";
    public static String COLUMN_MOOD = "Mood";

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
                + COLUMN_TASKTIME + " STRING, "
                + "FOREIGN KEY ("+COLUMN_USERID+") REFERENCES "+ACCOUNTS +" ("+COLUMN_USERID+")"
                + ")";
        // CREATE TABLE Tasks ( TaskID INTEGER PRIMARY KEY AUTOINCREMENT, TaskStatus INTEGER, TaskName TEXT, TaskDesc TEXT,
        //                         UserID INTEGER, TaskDate DATE, TaskTime TIME )

        // FOR MOOD TRACKER
        String CREATE_DATABASE_MOODTRACKER = "CREATE TABLE " + MOODTRACKER + "(" + COLUMN_MOODDATE + " TEXT," + COLUMN_MOOD + " TEXT" + ")";
        // CREATE TABLE MoodTracker ( MoodDate TEXT, Mood TEXT )

        //execute sql commands
        db.execSQL(CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE_TASK);
        db.execSQL(CREATE_DATABASE_MOODTRACKER);
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

    // adding task data into task table created
    public void addTask(Task taskData){
        ContentValues values = new ContentValues();
        //values.put(COLUMN_TASKID, taskData.getId());
        values.put(COLUMN_TASKSTATUS, taskData.getStatus());
        values.put(COLUMN_TASKNAME, taskData.getTaskName());
        values.put(COLUMN_TASKDESC, taskData.getTaskDesc());

        values.put(COLUMN_TASKDATE, taskData.getTaskDate());
        values.put(COLUMN_TASKTIME, taskData.getTaskTime());
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
        //Cursor cursor = db.rawQuery("select * from " + TASKS, null);
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int status = cursor.getInt(1);
            String name = cursor.getString(2);
            String desc = cursor.getString(3);

            //userID = cursor.getInt(4);
            String taskDate = cursor.getString(5);
            String taskTime = cursor.getString(6);

            Task newTask = new Task(id, status, name, desc, taskDate,taskTime,userID);
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
            queryData.setTaskTime(cursor.getString(6));

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

        Log.v(TAG, "Task Deleted");
        // db.delete("Tasks","TaskName=? and TaskDesc=?",new String[]{taskName,taskDesc});
    }

    public void deleteAllTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TASKS);
        db.close();
    }

    // adding mood entry to moodtracker table
    public void addMood(Mood moodData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOODDATE, moodData.getDate());
        values.put(COLUMN_MOOD, moodData.getMood());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(MOODTRACKER, null, values);
        Log.v(TAG, "Added to db");
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

    public void changeMood (Mood editedMood){
        SQLiteDatabase db = this.getWritableDatabase();
        // edit Mood entry
        String changeMood = "UPDATE " + MOODTRACKER + " SET " + COLUMN_MOOD + " = " + "\""+ editedMood.getMood()+ "\""  + " WHERE " + COLUMN_MOODDATE + " = " + "\""+ editedMood.getDate()+ "\"";
        db.execSQL(changeMood);
        db.close();
    }

    public void changeTaskStatus (Task taskStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        // edit Mood entry
        String changeTaskStatus = "UPDATE " + TASKS + " SET " + COLUMN_TASKSTATUS + " = " + "\""+ taskStatus.getStatus()+ "\""  + " WHERE " + COLUMN_TASKID + " = " + "\""+ taskStatus.getId()+ "\"";
        db.execSQL(changeTaskStatus);
        db.close();
    }


}
