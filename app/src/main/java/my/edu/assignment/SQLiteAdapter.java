package my.edu.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class SQLiteAdapter {

    //constant variable
    public static final String MYDATABASE_NAME = "MY_DATABASE";
    public static final String MYDATABASE_TABLE = "MY_TABLE_EXPENSE";
    public static final String MYDATABASE_FRIEND_TABLE = "MY_TABLE_FRIEND";
    public static final int MYDATABASE_VERSION = 6;

    public static final String KEY_CONTENT = "Name";
    public static final String KEY_CONTENT_2 = "PeopleName";
    public static final String KEY_CONTENT_3 = "Currency";
    public static final String KEY_CONTENT_4 = "PeopleNPayAmount";
    public static final String KEY_CONTENT_5 = "TotalAmount";
    public static final String KEY_CONTENT_6 = "PaymentStatus";
    public static final String KEY_CONTENT_7 = "Date";

    public static final String FRIENDS_NAME = "FriendName";
    public static final String FRIENDS_PHONENO = "FriendPhoneNo";

    //SQL command to create a table
    private static final String SCRIPT_CREATE_DATABASE =
            "create table if not exists " + MYDATABASE_TABLE
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_CONTENT + " text not null, "
                    + KEY_CONTENT_2 + " text not null, "
                    + KEY_CONTENT_3 + " text not null, "
                    + KEY_CONTENT_4 + " text not null, "
                    + KEY_CONTENT_5 + " text not null, "
                    + KEY_CONTENT_6 + " text not null, "
                    + KEY_CONTENT_7 + " text not null);";

    private static final String SCRIPT_CREATE_FRIEND =
            "create table if not exists " + MYDATABASE_FRIEND_TABLE
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FRIENDS_NAME + " text not null, "
                    + FRIENDS_PHONENO + " text not null);";

    //variables for db creation
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor for SQLiteAdapter
    public SQLiteAdapter(Context c)
    {
        context = c;
    }

    //open the database to write something
    public SQLiteAdapter openToWrite() throws android.database.SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase(); //writing mode

        return this;
    }

    //open the database to read something
    public SQLiteAdapter openToRead() throws android.database.SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null,
                MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase(); //reading mode

        return this;
    }

    //to write the data into the table
    public long insertFriend(String content, String content2)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of KEY_CONTENT
        contentValues.put(FRIENDS_NAME, content);
        contentValues.put(FRIENDS_PHONENO, content2);

        return sqLiteDatabase.insert(MYDATABASE_FRIEND_TABLE, null, contentValues);
    }

    //to write the data into the table with THREE columns
    public long insert(String content, String content_2, String content_3,
                       String content_4, String content_5, String content_6, String content_7)
    {
        ContentValues contentValues = new ContentValues();
        //to write the content to the column of all CONTENT
        contentValues.put(KEY_CONTENT, content);
        contentValues.put(KEY_CONTENT_2, content_2);
        contentValues.put(KEY_CONTENT_3, content_3);
        contentValues.put(KEY_CONTENT_4, content_4);
        contentValues.put(KEY_CONTENT_5, content_5);
        contentValues.put(KEY_CONTENT_6, content_6);
        contentValues.put(KEY_CONTENT_7, content_7);

        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }

    //select for specific expense name
    public String queueAll_Optional(String condition)
    {
        //which column u wanna read
        String [] columns = new String[] {KEY_CONTENT_2, KEY_CONTENT_4};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
                KEY_CONTENT+"=?", new String[]{condition}, null, null,
                null) ;

        String result = "";

        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2); //people name
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4); //people pay amount

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_2) +"; "
                    + cursor.getString(index_CONTENT_4) + "; ";
        }

        return result;
    }

    public String queueAll_Optional2(String expenseName, String date)
    {
        //which column u wanna read
        String [] columns = new String[] {KEY_CONTENT_2, KEY_CONTENT_4,KEY_CONTENT_6,KEY_CONTENT_7};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
                KEY_CONTENT + "=?"+" AND " + KEY_CONTENT_7 + "=?",
                new String[] {expenseName, date},null, null,
                KEY_CONTENT_2 + " ASC") ;

        String result = "";

        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2); //people name
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4); //people pay amount
        int index_CONTENT_6 = cursor.getColumnIndex(KEY_CONTENT_6); //people payment status

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT_2) +"; "
                    + cursor.getString(index_CONTENT_4) +"; "
                    + cursor.getString(index_CONTENT_6) +"; ";
        }

        return result;
    }


    public String queueAll()
    {
        //which column u wanna read
        String [] columns = new String[] {KEY_CONTENT, KEY_CONTENT_2, KEY_CONTENT_3,
                KEY_CONTENT_4, KEY_CONTENT_5, KEY_CONTENT_6, KEY_CONTENT_7};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, columns,
                null, null, null,
                null, null) ;

        String result = "";

        int index_CONTENT = cursor.getColumnIndex(KEY_CONTENT); //expense name
        int index_CONTENT_2 = cursor.getColumnIndex(KEY_CONTENT_2); //PeopleName
        int index_CONTENT_3 = cursor.getColumnIndex(KEY_CONTENT_3); //Currency
        int index_CONTENT_4 = cursor.getColumnIndex(KEY_CONTENT_4); //PeopleNPayAmount
        int index_CONTENT_5 = cursor.getColumnIndex(KEY_CONTENT_5); //Total Amount
        int index_CONTENT_6 = cursor.getColumnIndex(KEY_CONTENT_6); //PaymentStatus
        int index_CONTENT_7 = cursor.getColumnIndex(KEY_CONTENT_7); //Date

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT) +"; "
                    + cursor.getString(index_CONTENT_2) + "; "
                    + cursor.getString(index_CONTENT_3) + "; "
                    + cursor.getString(index_CONTENT_4) + "; "
                    + cursor.getString(index_CONTENT_5) + "; "
                    + cursor.getString(index_CONTENT_6) + "; "
                    + cursor.getString(index_CONTENT_7) + "; ";
        }

        return result;
    }

    public String queueAllFriend()
    {
        //which column u wanna read
        String [] columns = new String[] {FRIENDS_NAME, FRIENDS_PHONENO};

        //to locate the cursor
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_FRIEND_TABLE, columns,
                null, null, null,
                null, FRIENDS_NAME+" ASC") ;

        String result = "";

        int index_CONTENT = cursor.getColumnIndex(FRIENDS_NAME); //friend name
        int index_CONTENT_2 = cursor.getColumnIndex(FRIENDS_PHONENO); //friend phone no

        //it will read all the data until finish
        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT) +"; "
                    + cursor.getString(index_CONTENT_2) + ";";
        }

        return result;
    }


    //update expense table
    public void updateRecord(int rowId, String content, String content2, String content3,
                             String content4, String content5, String content6, String content7) {
        ContentValues values = new ContentValues();
        values.put(KEY_CONTENT, content);
        values.put(KEY_CONTENT_2, content2);
        values.put(KEY_CONTENT_3, content3);
        values.put(KEY_CONTENT_4, content4);
        values.put(KEY_CONTENT_5, content5);
        values.put(KEY_CONTENT_6, content6);
        values.put(KEY_CONTENT_7, content7);

        String whereClause = KEY_CONTENT + "=?";
        String[] whereArgs = {String.valueOf(rowId)};

        // Update the record in the database
        int rowsAffected = sqLiteDatabase.update(MYDATABASE_TABLE, values, whereClause, whereArgs);

        if (rowsAffected > 0) {
            // Update successful
        } else {
            // Update failed
        }
    }

    //close the database
    public void close()
    {
        sqLiteHelper.close();
    }

    //reduce the redundancy
    public int deleteAll()
    {
        return sqLiteDatabase.delete(MYDATABASE_TABLE,null, null);
    }

    public int deleteAllFriend() {
        return sqLiteDatabase.delete(MYDATABASE_FRIEND_TABLE, null, null);
    }

    //SQLiteOpenHelper: A helper class to manage database creation and version management
    public class SQLiteHelper extends SQLiteOpenHelper
    {

        public SQLiteHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //create a table with column
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
            db.execSQL(SCRIPT_CREATE_FRIEND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
            db.execSQL(SCRIPT_CREATE_FRIEND);
            if (oldVersion < 6) {
                // If upgrading from version 1 to 2, execute the updated SCRIPT_CREATE_DATABASE
                db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_TABLE); // Drop the old table if it exists
                db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_FRIEND_TABLE); // Drop the old table if it exists
                db.execSQL(SCRIPT_CREATE_FRIEND);
                db.execSQL(SCRIPT_CREATE_DATABASE);
            }
        }
    }
}
