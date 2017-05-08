package com.koceeng.freedonation.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.koceeng.freedonation.alarm.AlarmHelper;

public class BaseBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // reapply all alarm
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.reAppplyAlarm();
        }
    }
}
