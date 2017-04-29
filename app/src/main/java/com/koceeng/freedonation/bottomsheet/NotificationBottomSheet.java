package com.koceeng.freedonation.bottomsheet;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.alarm.AlarmReceiver;
import com.koceeng.freedonation.base.BaseBottomSheet;
import com.koceeng.freedonation.home.HomeActivity;

import java.util.Calendar;

public class NotificationBottomSheet extends BaseBottomSheet
        implements View.OnClickListener {

    private HomeActivity homeActivity;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        setTag("NotificationBottomSheet");

        View view = View.inflate(getContext(), R.layout.setting_notification, null);
        setContentView(dialog, view);

        view.findViewById(R.id.setting_notification_button_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_notification_button_save) {
            Calendar calendar = Calendar.getInstance();
            Log.e(TAG, "onClick: schedule the alarm");

//            calendar.set(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 19);
            calendar.set(Calendar.MINUTE, 39);
            calendar.set(Calendar.SECOND, 10);
            calendar.set(Calendar.MILLISECOND, 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(homeActivity, 0,
                    new Intent(homeActivity, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR, pendingIntent);

            Log.e(TAG, "onClick: schedule the alarm DONE");
        }
    }

    @Override
    public void inputFieldAction() {
        super.inputFieldAction();
    }
}
