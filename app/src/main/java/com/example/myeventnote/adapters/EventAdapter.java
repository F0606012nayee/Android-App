package com.example.myeventnote.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myeventnote.R;
import com.example.myeventnote.objects.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList = new ArrayList<>();

    static class ViewHolder {
        RelativeLayout itemLayout;
        TextView name, datetime;
    }

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.event_list_item, null);
            holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.event_item_frame);
            holder.name = (TextView) convertView.findViewById(R.id.event_item_name);
            holder.datetime = (TextView) convertView.findViewById(R.id.event_item_datetime);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(eventList.get(position).name);
        holder.datetime.setText(eventList.get(position).datetime);

        return convertView;
    }
}
