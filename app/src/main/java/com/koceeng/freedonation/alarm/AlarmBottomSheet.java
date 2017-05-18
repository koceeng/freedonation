package com.koceeng.freedonation.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseBottomSheet;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.setting.SettingHelper;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.Calendar;

public class AlarmBottomSheet extends BaseBottomSheet
        implements View.OnClickListener {

    View emptyText;
    RecyclerView recyclerView;
    AlarmRecyclerAdapter alarmRecyclerAdapter;

    AlarmHelper alarmHelper;
    HomeActivity homeActivity;

    Integer handleAlarmCountBeforeLoad = null;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        setTag("AlarmBottomSheet");

        alarmHelper = new AlarmHelper(homeActivity);

        View view = View.inflate(getContext(), R.layout.setting_alarm, null);
        setContentView(dialog, view);

        alarmRecyclerAdapter = new AlarmRecyclerAdapter(getContext(), this, alarmHelper);

        emptyText = view.findViewById(R.id.setting_alarm_empty_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.setting_alarm_recyclerview_main);

        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView.setAdapter(alarmRecyclerAdapter);
        recyclerView.setHasFixedSize(false);

        view.findViewById(R.id.setting_alarm_button_add).setOnClickListener(this);
        view.findViewById(R.id.setting_alarm_button_cancel).setOnClickListener(this);
    }

    public void onAlarmDataCountChange(final Integer count) {
        if (emptyText == null || recyclerView == null) {
            handleAlarmCountBeforeLoad = count;
        } else {
            Log.e(TAG, "onAlarmDataCountChange: " + count);
            LayoutUtil.getInstance().toggleVisibility(emptyText, count <= 0);
            LayoutUtil.getInstance().toggleVisibility(recyclerView, count > 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (handleAlarmCountBeforeLoad != null) {
            onAlarmDataCountChange(handleAlarmCountBeforeLoad);
        }
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
                    AlarmObject alarmObject = alarmHelper.addAlarmData(hourOfDay, minute);
                    if (alarmObject != null)
                        alarmRecyclerAdapter.putData(alarmObject);
                }
            }, hour, minutes, false);
            timePickerDialog.show();

        } else if (view.getId() == R.id.setting_alarm_button_cancel) {
            dismiss();
        }
    }

    @Override
    public void onDestroy() {
        if (homeActivity != null)
            homeActivity.onLanguageChange(SettingHelper.Type.NOTIFICATION, true);

        super.onDestroy();
    }
}
