package com.koceeng.freedonation.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class IntentUtil {

    public boolean validateIntent(Intent i, String key) {
        return (i != null
                && i.getExtras() != null
                && i.getExtras().size() > 0
                && i.hasExtra(key));
    }

    public boolean validateStringIntent(Intent i, String key) {
        return (validateIntent(i, key)
                && i.getStringExtra(key) != null
                && !i.getStringExtra(key).isEmpty());
    }

    public boolean validateBooleanIntent(Intent i, String key) {
        return (validateIntent(i, key)
                && i.getBooleanExtra(key, false));
    }

    public void goToMarket(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            activity.startActivity(i);
        } catch (android.content.ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private static IntentUtil intentUtil = null;

    public static IntentUtil getInstance() {
        return intentUtil == null ? new IntentUtil() : intentUtil;
    }
}
