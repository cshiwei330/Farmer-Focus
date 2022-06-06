package sg.edu.np.mad.week4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sg.edu.np.mad.madassignment1.User;

public class DBHandler extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "accountsDB.db";
    public static String ACCOUNTS = "Accounts"; //table name
    public static String COLUMN_USERNAME = "Username";
    public static String COLUMN_PASSWORD = "Password";
    public static int DATABASE_VERSION = 1;

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_DATABASE = "CREATE TABLE " + ACCOUNTS + "(" + COLUMN_USERNAME
                + " TEXT, " + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_DATABASE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
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



}
