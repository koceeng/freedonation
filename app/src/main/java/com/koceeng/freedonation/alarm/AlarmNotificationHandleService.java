package com.koceeng.freedonation.alarm;

import android.app.IntentService;
import android.content.Intent;

import com.koceeng.freedonation.home.SplashActivity;
import com.koceeng.freedonation.util.AppUtil;

public class AlarmNotificationHandleService extends IntentService {

    private final String TAG = "HandleNotification";

    public AlarmNotificationHandleService() {
        super("AlarmNotificationHandleService");
    }

    public AlarmNotificationHandleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!AppUtil.getInstance().isAppOnForeground(this))
            startActivity(SplashActivity.Factory.getIntent(this));
    }
}
