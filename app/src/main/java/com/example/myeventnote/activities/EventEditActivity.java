package com.example.myeventnote.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myeventnote.R;
import com.example.myeventnote.SharedPreUtils;
import com.example.myeventnote.helpers.MySQLiteOpenHelper;
import com.example.myeventnote.receivers.NotificationReceiver;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout layoutDate, layoutTime, layoutNotify;
    private TextInputEditText editTextName, editTextDate, editTextTime, editTextNotify, editTextLocation, editTextIncome, editTextCost, editTextContent;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;
    private int id, income, cost, notValue;
    private String strName, strDateTime, strNotify, strLocation, strContent;
    private final String[] listNot = { "???", "5?????????", "15?????????", "30?????????", "1?????????", "1??????" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        findView();

        setView();

        setListener();
    }

    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_event);
        layoutDate = (TextInputLayout) findViewById(R.id.layout_date);
        layoutTime = (TextInputLayout) findViewById(R.id.layout_time);
        layoutNotify = (TextInputLayout) findViewById(R.id.layoutNotify);
        editTextName = (TextInputEditText) findViewById(R.id.event_name);
        editTextDate = (TextInputEditText) findViewById(R.id.event_date);
        editTextTime = (TextInputEditText) findViewById(R.id.event_time);
        editTextNotify = (TextInputEditText) findViewById(R.id.event_notify);
        editTextLocation = (TextInputEditText) findViewById(R.id.event_location);
        editTextIncome = (TextInputEditText) findViewById(R.id.event_income);
        editTextCost = (TextInputEditText) findViewById(R.id.event_cost);
        editTextContent = (TextInputEditText) findViewById(R.id.event_content);
    }

    private void setView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        database = mySQLiteOpenHelper.getWritableDatabase();

        loadData();
    }

    private void setListener() {
        layoutDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int y = year;
                        int m = month + 1;
                        int d = dayOfMonth;
                        editTextDate.setText(String.format("%d/%02d/%02d", y, m, d));
                    }
                }, mYear,mMonth, mDay).show();
            }
        });

        layoutTime.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int h = hourOfDay;
                        int m = minute;
                        editTextTime.setText(String.format("%02d:%02d", h, m));
                    }
                }, hour, minute, true).show();
            }
        });

        layoutNotify.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] str = new String[1];
                str[0] = listNot[0];
                notValue = 0;
                new MaterialAlertDialogBuilder(EventEditActivity.this)
                    .setTitle("??????????????????")
                    .setSingleChoiceItems(listNot, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            str[0] = listNot[which];
                            notValue = which;
                        }
                    })
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editTextNotify.setText(str[0]);
                        }
                    })
                    .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {  // ???????????????????????????
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN ) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
            //return !(ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            new MaterialAlertDialogBuilder(this)
                .setTitle("????????????")
                .setMessage("???????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        else if (id == R.id.item_save) {
            saveEvent();
        }
        else if (id == R.id.item_delete) {
            deleteEvent();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
            .setTitle("????????????")
            .setMessage("???????????????????")
            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .show();
    }

    private void loadData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        if (id != 0) {
            toolbar.setTitle("????????????");
            getEvent(id);
            editTextName.setText(strName);
            String[] dt = strDateTime.split(" ");
            editTextDate.setText(dt[0]);
            editTextTime.setText(dt[1]);
            editTextNotify.setText(strNotify);
            editTextLocation.setText(strLocation);
            editTextIncome.setText(String.valueOf(income));
            editTextCost.setText(String.valueOf(cost));
            editTextContent.setText(strContent);
        }
        else { //??????
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            editTextDate.setText(df.format(date));
            df = new SimpleDateFormat("HH:mm");
            editTextTime.setText(df.format(date.getTime()));
            editTextNotify.setText("???");
            editTextIncome.setText("0");
            editTextCost.setText("0");
        }
    }

    private void getEvent(int id) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str = "";
        Date date = new Date();
        Cursor cursor = database.query("events", null, "id=?", new String[] {String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        strName = cursor.getString(cursor.getColumnIndex("eventName"));
        str = cursor.getString(cursor.getColumnIndex("eventDateTime"));
        date.setTime(Long.parseLong(str));
        strDateTime = df.format(date);
        strNotify = cursor.getString(cursor.getColumnIndex("eventNotify"));
        strLocation = cursor.getString(cursor.getColumnIndex("eventLocation"));
        income = cursor.getInt(cursor.getColumnIndex("eventIncome"));
        cost = cursor.getInt(cursor.getColumnIndex("eventCost"));
        strContent = cursor.getString(cursor.getColumnIndex("eventContent"));
        cursor.close();
    }

    private void saveEvent() {
        String name = editTextName.getText().toString();
        if (name.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                .setTitle("?????????????????????")
                .setMessage("???????????????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            try {
                Date writeTime = df.parse(editTextDate.getText().toString() + " " + editTextTime.getText().toString());
                strDateTime = String.valueOf(writeTime.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // ?????????
            income = Integer.parseInt(editTextIncome.getText().toString());
            cost = Integer.parseInt(editTextCost.getText().toString());

            ContentValues values = new ContentValues();
            values.put("eventName", name);
            values.put("eventDateTime", strDateTime);
            values.put("eventNotify", editTextNotify.getText().toString());
            values.put("eventLocation", editTextLocation.getText().toString());
            values.put("eventIncome", income);
            values.put("eventCost", cost);
            values.put("eventContent", editTextContent.getText().toString().trim());

            if (id != 0) {
                Toast.makeText(EventEditActivity.this, "?????????", Toast.LENGTH_SHORT).show();
                database.update("events", values, "id=?", new String[]{ String.valueOf(id) });
                cancelNotification();
            }
            else {
                Toast.makeText(EventEditActivity.this, "?????????", Toast.LENGTH_SHORT).show();
                database.insert("events", null, values);
            }

            if (!editTextNotify.getText().toString().equals(listNot[0])) {
                setNotification();
            }

            Intent intent = new Intent();
            intent.setClass(EventEditActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_no, R.anim.anim_no);
            finish();
        }
    }

    private void deleteEvent() {
        if (id != 0) {
            new MaterialAlertDialogBuilder(this)
                .setTitle("????????????")
                .setMessage("??????????????????")
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.delete("events", "id=?", new String[]{ String.valueOf(id) });
                        if (!editTextNotify.getText().toString().equals(listNot[0])) {
                            cancelNotification();
                        }
                        Toast.makeText(EventEditActivity.this, "?????????", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        else {
            Intent intent = new Intent();
            intent.setClass(EventEditActivity.this, EventListActivity.class);
            startActivity(intent);
        }
    }

    private void setNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(strDateTime));

        if (notValue == 1) { //5?????????
            calendar.add(Calendar.MINUTE, -5);
        }
        else if (notValue == 2) { //15?????????
            calendar.add(Calendar.MINUTE, -15);
        }
        else if (notValue == 3) { //30?????????
            calendar.add(Calendar.MINUTE, -30);
        }
        else if (notValue == 4) { //1?????????
            calendar.add(Calendar.HOUR_OF_DAY, -1);
        }
        else if (notValue == 5) { //1??????
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("nocTitle", editTextName.getText().toString());
        intent.putExtra("nocContent", editTextContent.getText().toString());

        int alarmId = SharedPreUtils.getInt(this, "alarm_id", 0);
        SharedPreUtils.setInt(this, "alarm_id",   ++alarmId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        int alarmId = SharedPreUtils.getInt(this, "alarm_id", 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
