package com.example.myeventnote.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeventnote.R;
import com.example.myeventnote.adapters.RecyclerViewAdapter;
import com.example.myeventnote.helpers.MySQLiteOpenHelper;
import com.example.myeventnote.objects.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;
    private final List<Event> eventList = new ArrayList<>();
    private final List<Event> emptyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        searchView = (SearchView) findViewById(R.id.search_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
    }

    private void setView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        database = mySQLiteOpenHelper.getWritableDatabase();
        searchView.onActionViewExpanded();
        searchView.setSubmitButtonEnabled(true);
        //queryAll();
        recyclerViewAdapter = new RecyclerViewAdapter(this, emptyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_no, R.anim.anim_no);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(SearchActivity.this, EventListActivity.class);
        startActivity(intent);
    }

    private void queryAll() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String str = "";
        Date date = new Date();
        Cursor cursor = database.query("events", new String[]{"id", "eventName", "eventDateTime"}, null, null, null, null, "eventDateTime");
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
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void doMySearch(String query) {
        List<Event> found = new ArrayList<>();
        if (query != null && !query.isEmpty()) {
            for (Event item : eventList) {
                if (item.name.contains(query)) {
                    found.add(item);
                }
            }
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, found);
        recyclerView.setAdapter(adapter);
    }

    private void refresh() {
        queryAll();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, emptyList);
        recyclerView.setAdapter(adapter);
    }
}
