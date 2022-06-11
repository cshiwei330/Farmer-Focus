package sg.edu.np.mad.madassignment1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;

import sg.edu.np.mad.madassignment1.User;

public class DBHandler extends SQLiteOpenHelper {
    private String TAG = "DB Handler";
    public static String DATABASE_NAME = "accountsDB.db";
    public static String ACCOUNTS = "Accounts"; //table name
    public static String COLUMN_USERNAME = "Username";
    public static String COLUMN_PASSWORD = "Password";

    public static String TASKS = "Tasks";
    public static String COLUMN_TASKID = "TaskId";
    public static String COLUMN_TASKSTATUS = "TaskStatus";
    public static String COLUMN_TASKNAME = "TaskName";
    public static String COLUMN_TASKDESC = "TaskDesc";

    public static int DATABASE_VERSION = 6;

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_DATABASE = "CREATE TABLE " + ACCOUNTS + "(" + COLUMN_USERNAME
                + " TEXT, " + COLUMN_PASSWORD + " TEXT" + ")";
        String CREATE_DATABASE_TASK = "CREATE TABLE " + TASKS + "(" + COLUMN_TASKID  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TASKSTATUS + " INTEGER, " + COLUMN_TASKNAME + " TEXT, " + COLUMN_TASKDESC + " TEXT" + ")";
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

    public void addUser(User userData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, userData.getUsername());
        values.put(COLUMN_PASSWORD, userData.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ACCOUNTS, null, values);
        db.close();
    }

    public void addTask(Task taskData){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASKID, taskData.getId());
        values.put(COLUMN_TASKSTATUS, taskData.getStatus());
        values.put(COLUMN_TASKNAME, taskData.getTaskName());
        values.put(COLUMN_TASKDESC, taskData.getTaskDesc());


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
            Task newTask = new Task(id, status, name, desc);
            taskArrayList.add(newTask);
        }
        db.close();
        return taskArrayList;
    }

    public void deleteAllTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TASKS);
        db.close();
    }
}
