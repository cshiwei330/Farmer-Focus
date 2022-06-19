package sg.edu.np.mad.madassignment1;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private final String TAG = "DB Handler";

    //database
    public static String DATABASE_NAME = "accountsDB.db";

    //accounts
    public static String ACCOUNTS = "Accounts"; //table name
    public static String COLUMN_USERNAME = "Username";
    public static String COLUMN_PASSWORD = "Password";

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

    public static int DATABASE_VERSION = 6;
    //define DBHandler
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //create tables
    public void onCreate(SQLiteDatabase db){
        String CREATE_DATABASE = "CREATE TABLE " + ACCOUNTS + "(" + COLUMN_USERNAME
                + " TEXT, " + COLUMN_PASSWORD + " TEXT" + ")";
        // CREATE TABLE Accounts ( Username TEXT, Password TEXT )
        String CREATE_DATABASE_TASK = "CREATE TABLE " + TASKS + "(" + COLUMN_TASKID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TASKSTATUS + " INTEGER, " + COLUMN_TASKNAME + " TEXT, " + COLUMN_TASKDESC + " TEXT," +
                COLUMN_HOUR + " INTEGER, " + COLUMN_MINUTE + " INTEGER, " + COLUMN_YEAR + " INTEGER, " + COLUMN_MONTH + " INTEGER, " + COLUMN_DAYOFMONTH + " INTEGER" + ")";
        // CREATE TABLE Tasks ( TaskID INTEGER PRIMARY KEY AUTOINCREMENT, TaskStatus INTEGER TaskName, TaskName TEXT, TaskDesc TEXT
        //                         TaskHour INTEGER, TaskMinute INTEGER, TaskYear INTEGER, TaskMonth INTEGER, TaskDayOfMonth INTEGER )
        //execute sql commands
        db.execSQL(CREATE_DATABASE);
        db.execSQL(CREATE_DATABASE_TASK);
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

        //updating password
//        db.update(ACCOUNTS, values, COLUMN_PASSWORD + " = ?",
//            new String[]{String.valueOf(userDBData.getPassword())});
//        db.close();
        //db.insert(ACCOUNTS, null, values);
        //db.close();
    }

    // adding task data into task table created
    public void addTask(Task taskData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKID, taskData.getId());
        values.put(COLUMN_TASKSTATUS, taskData.getStatus());
        values.put(COLUMN_TASKNAME, taskData.getTaskName());
        values.put(COLUMN_TASKDESC, taskData.getTaskDesc());
        values.put(COLUMN_HOUR, taskData.getTaskHour());
        values.put(COLUMN_MINUTE, taskData.getTaskMinute());
        values.put(COLUMN_YEAR, taskData.getTaskYear());
        values.put(COLUMN_MONTH, taskData.getTaskMonth());
        values.put(COLUMN_DAYOFMONTH, taskData.getTaskDayOfMonth());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TASKS, null, values);
        Log.v(TAG, "Added to db");
        db.close();
    }

    public ArrayList<Task> getTaskData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TASKS, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int status = cursor.getInt(1);
            String name = cursor.getString(2);
            String desc = cursor.getString(3);
            int hour = cursor.getInt(4);
            int min = cursor.getInt(5);
            int year = cursor.getInt(6);
            int month = cursor.getInt(7);
            int dayOfMonth = cursor.getInt(8);
            Task newTask = new Task(id, status, name, desc, hour, min, year, month ,dayOfMonth);
            taskArrayList.add(newTask);
        }
        db.close();
        return taskArrayList;
    }

    public void deleteTask(Task deleteTask){ // String taskName, String taskDesc
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Tasks","TaskId=? and TaskStatus=? and TaskName=? and TaskDesc=?" +
                        " and TaskHour=? and TaskMinute=? and TaskYear=? and TaskMonth=? and TaskDayOfMonth=?",
                new String[]{String.valueOf(deleteTask.getId()), String.valueOf(deleteTask.getStatus()),
                        deleteTask.getTaskName(), deleteTask.getTaskDesc(), String.valueOf(deleteTask.getTaskHour()),
                        String.valueOf(deleteTask.getTaskMinute()), String.valueOf(deleteTask.getTaskYear()),
                        String.valueOf(deleteTask.getTaskMonth()), String.valueOf(deleteTask.getTaskDayOfMonth())});

        Log.v(TAG, "Task Deleted");
        // db.delete("Tasks","TaskName=? and TaskDesc=?",new String[]{taskName,taskDesc});
    }

    public void deleteAllTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TASKS);
        db.close();
    }
}
