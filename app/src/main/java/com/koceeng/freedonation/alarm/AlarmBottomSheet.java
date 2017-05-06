package com.koceeng.freedonation.alarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TimePicker;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseBottomSheet;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.DebugUtil;

import java.util.Calendar;

public class AlarmBottomSheet extends BaseBottomSheet
        implements View.OnClickListener {

    RecyclerView recyclerView;
    AlarmRecyclerAdapter alarmRecyclerAdapter;

    private HomeActivity homeActivity;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        setTag("AlarmBottomSheet");

        View view = View.inflate(getContext(), R.layout.setting_alarm, null);
        setContentView(dialog, view);

        alarmRecyclerAdapter = new AlarmRecyclerAdapter(getContext(), this);
        alarmRecyclerAdapter.putData(SQLiteUtils.getInstance(homeActivity).getAlarms());

        recyclerView = (RecyclerView) view.findViewById(R.id.setting_alarm_recyclerview_main);

        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView.setAdapter(alarmRecyclerAdapter);
        recyclerView.setHasFixedSize(false);

        view.findViewById(R.id.setting_alarm_button_add).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_alarm_button_add) {

            Calendar currentDate = Calendar.getInstance();
            int hour = currentDate.get(Calendar.HOUR_OF_DAY);
            int minutes = currentDate.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(homeActivity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    addAlarm(hourOfDay, minute);
                }
            }, hour, minutes, false);
            timePickerDialog.show();
        }
    }

    private void addAlarm(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        DebugUtil.getInstance().v(TAG, "prepare alarm on " + hourOfDay + ":" + minute);

        AlarmObject alarmObject = new AlarmObject(hourOfDay, minute);
        alarmObject = SQLiteUtils.getInstance(homeActivity).putAlarm(alarmObject);

        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(homeActivity,
                alarmObject.getPendingIntentRequestCode(),
                new Intent(homeActivity, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HOUR, pendingIntent);

        alarmRecyclerAdapter.putData(alarmObject);

        DebugUtil.getInstance().v(TAG, "alarm set on " + hourOfDay + ":" + minute);
    }

    public void removeAlarm(AlarmObject alarmObject) {
        if (alarmObject.getPendingIntentRequestCode() != null) {

            Intent intent = new Intent(homeActivity, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(homeActivity,
                    alarmObject.getPendingIntentRequestCode(), intent, 0);
            AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

        // remove alarm from database
        SQLiteUtils.getInstance(homeActivity).removeAlarm(alarmObject.getId());
        alarmRecyclerAdapter.removeData(alarmObject.getId());
    }

    @Override
    public void inputFieldAction() {
        super.inputFieldAction();
    }
}
