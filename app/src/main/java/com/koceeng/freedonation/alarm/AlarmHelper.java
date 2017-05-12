package com.koceeng.freedonation.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.DebugUtil;

import java.util.Calendar;
import java.util.List;

public class AlarmHelper {

    private final String TAG = "AlarmHelper";

    Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public List<AlarmObject> getAllData() {
        return SQLiteUtils.getInstance(context).getAlarms();
    }

    public AlarmObject addAlarmData(int hourOfDay, int minute) {
        DebugUtil.getInstance().v(TAG, "prepare alarm on " + hourOfDay + ":" + minute);

        AlarmObject alarmObject = new AlarmObject(hourOfDay, minute);
        alarmObject = SQLiteUtils.getInstance(context).putAlarm(alarmObject);

        addAlarm(alarmObject);

        DebugUtil.getInstance().v(TAG, "alarm set on " + hourOfDay + ":" + minute);
        return alarmObject;
    }

    public void addAlarm(AlarmObject alarmObject) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, alarmObject.getHourOfDay());
        calendar.set(Calendar.MINUTE, alarmObject.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarmObject.getPendingIntentRequestCode(),
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void removeAlarmData(AlarmObject alarmObject) {
        removeAlarm(alarmObject);

        // remove alarm from database
        SQLiteUtils.getInstance(context).removeAlarm(alarmObject.getId());
    }

    public void removeAlarm(AlarmObject alarmObject) {
        if (alarmObject.getPendingIntentRequestCode() != null) {

            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context,
                    alarmObject.getPendingIntentRequestCode(), intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

    public void reApplyAlarm(Context context) {
        // TODO: 12/05/17 remove context
        Toast.makeText(context, "reApplyAlarm do", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "reApplyAlarm: do");
        List<AlarmObject> alarmObjects = getAllData();
        for (AlarmObject alarmObject : alarmObjects) {
            Toast.makeText(context, "reApplyAlarm for " + alarmObject.getId()+ "|" + alarmObject.getDisplay(context), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "reApplyAlarm: for "  + alarmObject.getId()+ "|" + alarmObject.getDisplay(context));
            addAlarm(alarmObject);
        }
    }
}
