package com.example.myeventnote.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String db_name = "MySchedule";
    private static final int version = 1;
    private Context mContext;

    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context, db_name, null, version);
        mContext = context;
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

    public void deleteAll(SQLiteDatabase db) {
        db.execSQL("drop table if exists events");
        onCreate(db);
    }

    public void exportDB(String outFileName) {  // 備份
        //資料庫路徑
        final String inFileName = mContext.getDatabasePath(db_name).toString();
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // 開啟空檔案
            OutputStream output = new FileOutputStream(outFileName);
            // 將內容輸出到檔案
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            // 關閉streams
            output.flush();
            output.close();
            fis.close();
            Toast.makeText(mContext, "備份完成", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "無法備份! 請重試", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) { // 匯入
        final String outFileName = mContext.getDatabasePath(db_name).toString();
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            fis.close();
            Toast.makeText(mContext, "匯入完成", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "無法匯入! 請重試", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
