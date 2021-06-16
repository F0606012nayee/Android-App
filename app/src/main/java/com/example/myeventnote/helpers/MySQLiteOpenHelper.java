package com.example.myeventnote.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String db_name = "MySchedule";
    private static final int version = 1;

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  sql = "create table events(" +
                "id Integer primary key autoincrement," +
                "eventName varchar(20) not null," +
                "eventDateTime Text," +
                "eventNotify varchar(10)," +
                "eventLocation varchar(20)," +
                "eventIncome Integer default 0," +
                "eventCost Integer default 0," +
                "eventContent Text" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists events");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists events");
        onCreate(db);
    }
}
