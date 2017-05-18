package com.koceeng.freedonation.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.AppUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LanguageUtil;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        DebugUtil.getInstance().v(TAG, "onReceive");

        // skip if app is in foreground
        if (AppUtil.getInstance().isAppOnForeground(context))
            return;

        LanguageUtil.getInstance().updateLanguageResource(context);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentNotification = new Intent(context, AlarmNotificationHandleService.class);
        // intentNotification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentNotification,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // get defaults
        Notification notificationDefaults = new Notification();
        notificationDefaults.defaults |= Notification.DEFAULT_SOUND;
        notificationDefaults.defaults |= Notification.DEFAULT_VIBRATE;

        Notification notification = new NotificationCompat.Builder(context)
                .setDefaults(notificationDefaults.defaults)
                .setContentTitle(context.getString(R.string.setting_notification_text_title))
                .setContentText(context.getString(R.string.setting_notification_text_content))
                .setSmallIcon(R.drawable.icon_vector)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xFFff0000;
        notification.ledOnMS = 100;
        notification.ledOffMS = 100;

        notificationManager.notify(0, notification);
    }
}
