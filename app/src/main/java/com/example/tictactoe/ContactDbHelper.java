package com.example.tictactoe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "WINTime_db";
    static final int DATABASE_VERSION = 1;
    final static String CREATE_TABLE = "create table minTime_info(winTime number);";
    final static String DROP_TABLE = "drop table if exists minTime_info";

    public ContactDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addContent(double winTime,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put("winTime",winTime);
        database.insert("minTime_info",null,contentValues);
        Log.e("message","A row added to database");
    }

    public Cursor readContent(SQLiteDatabase database){
        String[] projection = {"winTime"};
        Cursor cursor= database.query("minTime_info",projection,null,null,null,null,null);
        return cursor;
    }
}
