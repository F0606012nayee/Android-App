package com.example.myeventnote.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import com.example.myeventnote.R;
import com.example.myeventnote.adapters.EventAdapter;
import com.example.myeventnote.backup.LocalBackup;
import com.example.myeventnote.helpers.MySQLiteOpenHelper;
import com.example.myeventnote.objects.Event;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button buttonDay, buttonWeek, buttonMonth, buttonYear, buttonCustom;
    private TextView textView;
    private ListView listView;
    private FloatingActionButton buttonAdd;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;
    private EventAdapter eventAdapter;
    private final List<Event> eventList = new ArrayList<>();
    private final String[] dayOfWeek = { "日", "一", "二", "三", "四", "五", "六" };
    private final String[] listDay = { "前兩天", "前一天", "今天", "明天", "後天" };
    private final String[] listWeek = { "前兩週", "前一週", "本週", "下週", "下下週" };
    private final String[] listMonth = { "前兩個月", "前一個月", "本月", "下個月", "下下個月" };
    private final String[] listYear = { "過去一年", "上半年", "今年", "下半年", "未來一年" };
    private final String[] listCustom = { "一天", "範圍" };
    private LocalBackup localBackup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        buttonDay = (Button) findViewById(R.id.button_day);
        buttonWeek = (Button) findViewById(R.id.button_week);
        buttonMonth = (Button) findViewById(R.id.button_month);
        buttonYear = (Button) findViewById(R.id.button_year);
        buttonCustom = (Button) findViewById(R.id.button_custom);
        textView = (TextView) findViewById(R.id.text_no_content);
        listView = (ListView) findViewById(R.id.list_main);
        buttonAdd = (FloatingActionButton) findViewById(R.id.button_add);
    }

    private void setView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        database = mySQLiteOpenHelper.getWritableDatabase();
        localBackup = new LocalBackup(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int wd = calendar.get(Calendar.DAY_OF_WEEK);
        toolbar.setSubtitle(String.format("%d年%02d月%02d日星期%s", year, month, day, dayOfWeek[wd - 1]));
        //query();
        eventAdapter = new EventAdapter(this, eventList);
        eventAdapter.notifyDataSetChanged();
        listView.setAdapter(eventAdapter);
    }

    private void setListener() {
        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] select = new int[1]; //儲存選擇值 預設值為2
                select[0] = 2;
                new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("選擇要查看的時間")
                    .setSingleChoiceItems(listDay, 2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select[0] = which;
                        }
                    })
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                            intent.putExtra("itemClass", 1);
                            intent.putExtra("itemValue", select[0]);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });

        buttonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] select = new int[1];
                select[0] = 2;
                new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("選擇要查看的時間")
                    .setSingleChoiceItems(listWeek, 2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select[0] = which;
                        }
                    })
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                            intent.putExtra("itemClass", 2);
                            intent.putExtra("itemValue", select[0]);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });

        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] select = new int[1];
                select[0] = 2;
                new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("選擇要查看的時間")
                    .setSingleChoiceItems(listMonth, 2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select[0] = which;
                        }
                    })
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                            intent.putExtra("itemClass", 3);
                            intent.putExtra("itemValue", select[0]);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });

        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] select = new int[1];
                select[0] = 2;
                new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("選擇要查看的時間")
                    .setSingleChoiceItems(listYear, 2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            select[0] = which;
                        }
                    })
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                            intent.putExtra("itemClass", 4);
                            intent.putExtra("itemValue", select[0]);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });

        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(MainActivity.this)
                    .setTitle("選擇要查看的時間")
                    .setItems(listCustom, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) { // 一天
                                MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                                        .setTitleText("選擇日期")
                                        .build();
                                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                                    @Override
                                    public void onPositiveButtonClick(Long selection) {
                                        Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                                        intent.putExtra("itemClass", 5);
                                        intent.putExtra("itemValue", which);
                                        intent.putExtra("itemStartDate", selection);
                                        startActivity(intent);
                                    }
                                });
                                picker.show(getSupportFragmentManager(), "Material_Date_Picker");
                            }
                            else {  //範圍
                                MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                                        .setTitleText("選擇日期")
                                        .build();
                                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                                    @Override
                                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                                        Intent intent = new Intent(MainActivity.this, EventListActivity.class);
                                        intent.putExtra("itemClass", 5);
                                        intent.putExtra("itemValue", which);
                                        intent.putExtra("itemStartDate", selection.first);
                                        intent.putExtra("itemEndDate", selection.second);
                                        startActivity(intent);
                                    }
                                });
                                picker.show(getSupportFragmentManager(), "Material_Date_Range_Picker");
                            }
                        }
                    })
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EventEditActivity.class);
                intent.putExtra("id", eventList.get(position).id);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplicationContext());
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_no, R.anim.anim_no);  // 取消切換動畫
        }
        else if (id == R.id.action_backup_file) {
            Toast.makeText(MainActivity.this, "備份檔案", Toast.LENGTH_SHORT).show();
            String outFileName = Environment.getExternalStorageDirectory().getPath() + File.separator + getResources().getString(R.string.app_name) + File.separator;
            localBackup.performBackup(helper, outFileName);
        }
        else if (id == R.id.action_import_file) {
            Toast.makeText(MainActivity.this, "匯入檔案", Toast.LENGTH_SHORT).show();
            localBackup.performRestore(helper);
        }
        else if (id == R.id.action_backup_description) {
            String text = "檔案儲存在MyEventNote資料夾內\n" +
                    "匯入時從資料夾的檔案匯入";
            new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("備份說明")
                .setMessage(text)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        else if (id == R.id.action_delete_all) { //清除資料
            Toast.makeText(MainActivity.this, "刪除所有資料", Toast.LENGTH_SHORT).show();
            new MaterialAlertDialogBuilder(this)
                .setTitle("刪除所有記事")
                .setMessage("所有記事資料即將刪除")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mySQLiteOpenHelper.deleteAll(database);
                        refresh();
                        Toast.makeText(MainActivity.this, "資料已刪除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        else if (id == R.id.action_about) {
            new MaterialAlertDialogBuilder(MainActivity.this)
                .setTitle("關於")
                .setMessage("開發者\n\t\t陳昌俊 陳立生 盧冠華\n\t\t劉承齊 徐浩瑋\n發布日期\n\t\t2021年5月30日")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void query() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = new Date();
        Date today = calendar.getTime();
        Cursor cursor = database.query("events", new String[]{"id", "eventName", "eventDateTime"}, "eventDateTime>=?", new String[] {String.valueOf(today.getTime())}, null, null, "eventDateTime");
        if (cursor.moveToFirst()) {
            eventList.clear();
            do {
                Event e = new Event();
                e.id = cursor.getInt(cursor.getColumnIndex("id"));
                e.name = cursor.getString(cursor.getColumnIndex("eventName"));
                str = cursor.getString(cursor.getColumnIndex("eventDateTime"));
                date.setTime(Long.parseLong(str));
                e.datetime = df.format(date);
                eventList.add(e);
                if (eventList.size() >= 5) {
                    break;
                }
            } while (cursor.moveToNext());
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        cursor.close();
    }

    public void refresh() { // 刷新資料
        query();
        eventAdapter = new EventAdapter(this, eventList);
        listView.setAdapter(eventAdapter);
    }
}
