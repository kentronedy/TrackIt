package com.aloydev.weighttracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TrackerIntentService extends IntentService {

    private WeightDatabase weightDB;

    public TrackerIntentService() {
        super("TrackerIntentService");
        weightDB = new WeightDatabase(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Get the submitted weight and the username
        String username = intent.getStringExtra("username");
        float weight = intent.getFloatExtra("weight", 0);

        //Create notification channel
        createTrackerNotificationChannel();

        //check weight against the goal and notify if it matches
        Cursor cursor = weightDB.readUserData(username);
        cursor.moveToNext();
        float goal = cursor.getFloat(2);
        if(weight == goal) {
            createTrackerNotification("You reached your goal! Congrats!");
        }

    }

    private final String CHANNEL_ID_TRACKER = "channel_tracker";

    private void createTrackerNotificationChannel() {
        if(Build.VERSION.SDK_INT < 26) return;

        CharSequence name = "Tracker Channel";
        String description = "The channel used to notify the user that their goal has been met.";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_TRACKER, name, importance);
        channel.setDescription(description);

        //Register channel
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private final int NOTIFICATION_ID = 0;

    private void createTrackerNotification(String text) {
        //Create notification and set properties
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_TRACKER)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        //get compatibility notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        //post notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
