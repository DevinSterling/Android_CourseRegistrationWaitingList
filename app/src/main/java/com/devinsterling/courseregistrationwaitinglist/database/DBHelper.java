package com.devinsterling.courseregistrationwaitinglist.database;
/* 
    Devin Sterling
    2022 - 07 - 26
    Course Registration Waiting List
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Create new db if not existent */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.SQL_CREATE_COURSES);
        db.execSQL(DBContract.SQL_CREATE_STUDENTS);
        db.execSQL(DBContract.SQL_INSERT_SAMPLE_COURSES);
        db.execSQL(DBContract.SQL_INSERT_SAMPLE_STUDENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}