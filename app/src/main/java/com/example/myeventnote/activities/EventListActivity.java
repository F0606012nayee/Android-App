package com.example.myeventnote.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeventnote.R;
import com.example.myeventnote.adapters.RecyclerViewAdapter;
import com.example.myeventnote.helpers.MySQLiteOpenHelper;
import com.example.myeventnote.objects.Event;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button buttonTotal;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;
    private RecyclerViewAdapter recyclerViewAdapter;
    private final List<Event> eventList = new ArrayList<>();
    private int valueClass, value;
    private final Long[] date = new Long[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        findView();

        setView();

        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_list);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        buttonTotal = (Button) findViewById(R.id.button_total);
    }

    private void setView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        database = mySQLiteOpenHelper.getWritableDatabase();
        Intent intent = getIntent();
        valueClass = intent.getIntExtra("itemClass", 0);
        value = intent.getIntExtra("itemValue", 0);
        date[0] = intent.getLongExtra("itemStartDate", 0);
        date[1] = intent.getLongExtra("itemEndDate", 0);
        //query(valueClass, value);
        recyclerViewAdapter = new RecyclerViewAdapter(this, eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setListener() {

        buttonTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total, income = 0, cost = 0;
                for (Event e: eventList) {
                    income += e.income;
                    cost += e.cost;
                }
                total = income - cost;
                String message = "????????? = " + income + "\n" + "????????? = " + cost + "\n" + "????????? = " + total;
                new MaterialAlertDialogBuilder(EventListActivity.this)
                    .setTitle("????????????")
                    .setMessage(message)
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void query(int valueClass, int value) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar cals = Calendar.getInstance();
        Calendar cale = Calendar.getInstance();
        if (valueClass == 1) { //???
            if (value == 0) { //?????????
                cals.add(Calendar.DAY_OF_MONTH, -2);
                cale.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (value == 1) { //?????????
                cals.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (value == 2) { //??????
                cale.add(Calendar.DAY_OF_MONTH, 1);
            }
            else if (value == 3) { //??????
                cals.add(Calendar.DAY_OF_MONTH, 1);
                cale.add(Calendar.DAY_OF_MONTH, 2);
            }
            else if (value == 4) { //??????
                cals.add(Calendar.DAY_OF_MONTH, 2);
                cale.add(Calendar.DAY_OF_MONTH, 3);
            }
        }
        else if (valueClass == 2) { //???
            cals.set(Calendar.DAY_OF_WEEK, 1);
            cale.set(Calendar.DAY_OF_WEEK, 1);
            if (value == 0) { //?????????
                cals.add(Calendar.WEEK_OF_MONTH, -2);
                cale.add(Calendar.WEEK_OF_MONTH, -1);
            }
            else if (value == 1) { //?????????
                cals.add(Calendar.WEEK_OF_MONTH, -1);
            }
            else if (value == 2) { //??????
                cale.add(Calendar.WEEK_OF_MONTH, 1);
            }
            else if (value == 3) { //??????
                cals.add(Calendar.WEEK_OF_MONTH, 1);
                cale.add(Calendar.WEEK_OF_MONTH, 2);
            }
            else if (value == 4) { //?????????
                cals.add(Calendar.WEEK_OF_MONTH, 2);
                cale.add(Calendar.WEEK_OF_MONTH, 3);
            }
        }
        else if (valueClass == 3) { //???
            cals.set(Calendar.DAY_OF_MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            if (value == 0) { //????????????
                cals.add(Calendar.MONTH, -2);
                cale.add(Calendar.MONTH, -1);
            }
            else if (value == 1) { //????????????
                cals.add(Calendar.MONTH, -1);
            }
            else if (value == 2) { //??????
                cale.add(Calendar.MONTH, 1);
            }
            else if (value == 3) { //?????????
                cals.add(Calendar.MONTH, 1);
                cale.add(Calendar.MONTH, 2);
            }
            else if (value == 4) { //????????????
                cals.add(Calendar.MONTH, 2);
                cale.add(Calendar.MONTH, 3);
            }
        }
        else if (valueClass == 4) { //???
            if (value == 0) { //????????????
                cals.add(Calendar.YEAR, -1);
            }
            else if (value == 1) { //???????????? 1~6???(1/1~7/1)
                cals.set(Calendar.DAY_OF_MONTH, 1);
                cale.set(Calendar.DAY_OF_MONTH, 1);
                cals.set(Calendar.MONTH, 0);
                cale.set(Calendar.MONTH, 6);
            }
            else if (value == 2) { //?????? 1~12???(1/1~1/1)
                cals.set(Calendar.DAY_OF_MONTH, 1);
                cale.set(Calendar.DAY_OF_MONTH, 1);
                cals.set(Calendar.MONTH, 0);
                cale.set(Calendar.MONTH, 11);
                cale.add(Calendar.MONTH, 1);
            }
            else if (value == 3) { //???????????? 7~12???(7/1~1/1)
                cals.set(Calendar.DAY_OF_MONTH, 1);
                cale.set(Calendar.DAY_OF_MONTH, 1);
                cals.set(Calendar.MONTH, 6);
                cale.set(Calendar.MONTH, 11);
                cale.add(Calendar.MONTH, 1);
            }
            else if (value == 4) { //????????????
                cals.add(Calendar.DAY_OF_MONTH, 1);
                cale.add(Calendar.DAY_OF_MONTH, 1);
                cale.add(Calendar.YEAR, 1);
            }
        }
        else if (valueClass == 5) { //??????
            if (value == 0) { //??????
                cals.setTimeInMillis(date[0]);
                cale.setTimeInMillis(date[0]);
                cale.add(Calendar.DAY_OF_MONTH, 1);
            }
            else if (value == 1) { //??????
                cals.setTimeInMillis(date[0]);
                cale.setTimeInMillis(date[1]);
                cale.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        cals.set(Calendar.SECOND, 0);
        cals.set(Calendar.MILLISECOND, 0);
        cals.set(Calendar.MINUTE, 0);
        cals.set(Calendar.HOUR_OF_DAY, 0);

        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.HOUR_OF_DAY, 0);

        String str = "";
        Date date = new Date();
        long startTime = cals.getTimeInMillis();
        long endTime = cale.getTimeInMillis();
        // eventDateTime>=startTime and eventDateTime<endTime
        Cursor cursor = database.query("events", new String[]{"id", "eventName", "eventDateTime", "eventIncome", "eventCost"}, "eventDateTime>=? and eventDateTime<?", new String[] { String.valueOf(startTime), String.valueOf(endTime) }, null, null, "eventDateTime");
        if (cursor.moveToFirst()) {
            eventList.clear();
            do {
                Event e = new Event();
                e.id = cursor.getInt(cursor.getColumnIndex("id"));
                e.name = cursor.getString(cursor.getColumnIndex("eventName"));
                e.income = cursor.getInt(cursor.getColumnIndex("eventIncome"));
                e.cost = cursor.getInt(cursor.getColumnIndex("eventCost"));
                str = cursor.getString(cursor.getColumnIndex("eventDateTime"));
                date.setTime(Long.parseLong(str));
                e.datetime = df.format(date);
                eventList.add(e);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void refresh() {
        query(valueClass, value);
        recyclerViewAdapter = new RecyclerViewAdapter(this, eventList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
