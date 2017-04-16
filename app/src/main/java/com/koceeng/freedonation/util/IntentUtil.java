package com.koceeng.freedonation.util;

import android.app.Activity;
import android.content.Context;
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

    public void goToMarket(Context context, boolean showDeveloperPage) {
        String link;
        String alternateLink;
        if (showDeveloperPage) {
            link = "market://dev?id=Koceeng+Dev";
            alternateLink = "https://play.google.com/store/apps/developer?id=Koceeng+Dev";
        } else {
            link = "market://details?id=" + context.getPackageName();
            alternateLink = "https://play.google.com/store/apps/details?id=" + context.getPackageName();
        }

        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(i);
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(alternateLink)));
        }
    }

    private static IntentUtil intentUtil = null;

    public static IntentUtil getInstance() {
        return intentUtil == null ? new IntentUtil() : intentUtil;
    }
}
