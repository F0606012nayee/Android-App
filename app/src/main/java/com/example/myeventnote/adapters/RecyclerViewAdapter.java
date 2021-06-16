package com.example.myeventnote.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myeventnote.R;
import com.example.myeventnote.activities.EventEditActivity;
import com.example.myeventnote.activities.EventListActivity;
import com.example.myeventnote.activities.MainActivity;
import com.example.myeventnote.objects.Event;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Event> eventList = new ArrayList<>();

    public RecyclerViewAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MyViewHolder holder, int position) {
        holder.name.setText(eventList.get(position).name);
        holder.datetime.setText(eventList.get(position).datetime);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, EventEditActivity.class);
                intent.putExtra("id", eventList.get(position).id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView name, datetime;
    public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
        super(itemView);
        view = (View) itemView;
        name = (TextView) itemView.findViewById(R.id.event_item_name);
        datetime = (TextView) itemView.findViewById(R.id.event_item_datetime);
    }
}