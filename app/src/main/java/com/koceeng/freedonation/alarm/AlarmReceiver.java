package com.koceeng.freedonation.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.home.SplashActivity;
import com.koceeng.freedonation.util.LanguageUtil;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmReceiver", "onReceive: ");

        LanguageUtil.getInstance().updateLanguageResource(context);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intentNotification = new Intent(context, SplashActivity.class);
        intentNotification.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentNotification,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.setting_notification_text_title))
                .setContentText(context.getString(R.string.setting_notification_text_content))
                .setSmallIcon(R.drawable.icon_vector)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(0, notification);
    }
}
