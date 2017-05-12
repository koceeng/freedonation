package com.koceeng.freedonation.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.koceeng.freedonation.alarm.AlarmHelper;

public class BaseBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "BaseBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 12/05/17 remove log
        Log.e(TAG, "onReceive: " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e(TAG, "onReceive:");

            // reapply all alarm
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.reApplyAlarm(context);
        }
    }
}
