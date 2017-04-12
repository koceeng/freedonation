package com.koceeng.freedonation.util;

import android.app.Activity;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;

public class DebugUtil {

    public void v(Object tag, String string) {
        String tagDisplay = tag instanceof Activity || tag instanceof Fragment ? tag.getClass().getSimpleName() : tag.toString();

        if (BuildConfig.DEBUG) {
            Log.e(tagDisplay, string);
        } else {
            FirebaseCrash.log(tagDisplay + "::" + string);
        }
    }

    public void e(Object tag, String string) {
        String tagDisplay = tag instanceof Activity || tag instanceof Fragment ? tag.getClass().getSimpleName() : tag.toString();

        if (BuildConfig.DEBUG) {
            Log.e(tagDisplay, string);
        } else {
            FirebaseCrash.report(new Exception(tagDisplay + "::" + string));
        }
    }

    public void f(Object tag, DatabaseError databaseError) {
        if ((databaseError.getMessage().toLowerCase().contains("permission denied")
                || databaseError.getMessage().equalsIgnoreCase("This client does not have permission to perform this operation"))
                && FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        String tagDisplay = tag instanceof Activity || tag instanceof Fragment ? tag.getClass().getSimpleName() : tag.toString();
        String string = databaseError.getMessage() + "::" + databaseError;

        if (BuildConfig.DEBUG) {
            Log.e(tagDisplay, string);
        } else {
            FirebaseCrash.report(new Exception(tagDisplay + "::" + string));
        }
    }

    public void b(Object tag, String string) {

        String tagDisplay = tag instanceof Activity || tag instanceof Fragment ? tag.getClass().getSimpleName() : tag.toString();
        string = "billingError::" + string;

        if (string.toLowerCase().contains("User canceled".toLowerCase())
                || string.toLowerCase().contains("Billing Unavailable".toLowerCase())) {
            FirebaseCrash.log("Non-fatal error skipped: " + string);
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.e(tagDisplay, string);
        } else {
            FirebaseCrash.report(new Exception(tagDisplay + "::" + string));
        }
    }

    private static DebugUtil debugUtil = null;

    public static DebugUtil getInstance() {
        if (debugUtil == null) {
            debugUtil = new DebugUtil();
        }

        return debugUtil;
    }
}
