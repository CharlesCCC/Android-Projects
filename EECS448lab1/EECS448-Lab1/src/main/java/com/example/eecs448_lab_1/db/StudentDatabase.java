package com.example.eecs448_lab_1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by xushengxiao on 1/15/18.
 */

public class StudentDatabase extends SQLiteOpenHelper {
    public static final String DB_NAME = "student_data";

    //Constants for table and columns
    public static final String TABLE = "student";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";

    private static final String CREATE_TABLE_TUTORIALS = "create table " + TABLE + " (" + ID + " integer primary key autoincrement, " + NAME
            + " text, " + PHONE + " text );";

    private static final String DB_SCHEMA = CREATE_TABLE_TUTORIALS;


    public static final String[] ALL_COLUMNS =
            {ID, NAME, PHONE};

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Student database", "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");
        db.execSQL("DROP TABLE IF EXISTS " + "student");
        onCreate(db);
    }

    public StudentDatabase(Context context) {
        super(context, DB_NAME, null, 1);
    }
}
