package com.koceeng.freedonation.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.koceeng.freedonation.R;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver", "onReceive: ");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText("Test content");
        builder.setSmallIcon(R.drawable.like);

        notificationManager.notify(10, builder.build());

//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Log.e("AlarmReceiver", "onReceive: RESET ALARM");
//        }
    }
}
