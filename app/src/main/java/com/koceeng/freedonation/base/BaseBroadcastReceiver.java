package com.koceeng.freedonation.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.koceeng.freedonation.alarm.AlarmHelper;
import com.koceeng.freedonation.util.DebugUtil;

public class BaseBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "BaseBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        DebugUtil.getInstance().v(TAG, "onReceive: " + intent.getAction());

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            DebugUtil.getInstance().v(TAG, "onReceive:");

            // reapply all alarm
            AlarmHelper alarmHelper = new AlarmHelper(context);
            alarmHelper.reApplyAlarm();
        }
    }
}
