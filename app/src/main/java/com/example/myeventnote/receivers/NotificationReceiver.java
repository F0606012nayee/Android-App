package com.example.myeventnote.receivers;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.myeventnote.R;

public class NotificationReceiver extends BroadcastReceiver {
    private String title, content;

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("nocTitle");
        content = intent.getStringExtra("nocContent");
        showNotification(context);
    }

    @SuppressLint("NewApi")
    private void showNotification(Context context) {
        NotificationCompat.Builder builder;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "channel_01";
        String name = "Channel Noc";

        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = manager.getNotificationChannel(id);

            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription("這是行程通知的頻道");
                mChannel.shouldShowLights();
                manager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(context, "channel_01");
            builder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_notify_event_note)
                .setColor(ContextCompat.getColor(context, R.color.blue_A200))
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker("這是行程通知")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setCategory(Notification.CATEGORY_EVENT);
        }
        else {
            builder = new NotificationCompat.Builder(context, "channel_01");
            builder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_notify_event_note)
                .setColor(ContextCompat.getColor(context, R.color.blue_A200))
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker("這是行程通知")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setCategory(Notification.CATEGORY_EVENT)
                .setPriority(Notification.PRIORITY_DEFAULT);
        }
        Notification notification = builder.build();
        manager.notify(1, notification);
    }
}
