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
        builder.setContentTitle(context.getString(R.string.setting_notification_text_title));
        builder.setContentText(context.getString(R.string.setting_notification_text_content));
        builder.setSmallIcon(R.drawable.icon_vector);

        notificationManager.notify(0, builder.build());
    }
}
