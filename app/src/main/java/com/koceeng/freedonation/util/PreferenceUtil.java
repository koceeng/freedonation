package com.koceeng.freedonation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    public void putBoolean(Context context, String preferenceKey, Boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(preferenceKey, value);
        editor.apply();
    }

    public Boolean getBoolean(Context context, String preferenceKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (preferences != null) && preferences.getBoolean(preferenceKey, false);
    }

    public void putString(Context context, String preferenceKey, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(preferenceKey, value);
        editor.apply();
    }

    public String getString(Context context, String preferenceKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (preferences != null) ? preferences.getString(preferenceKey, null) : null;
    }

    private static PreferenceUtil preferenceUtil;

    public static PreferenceUtil getInstance() {
        if (preferenceUtil == null)
            preferenceUtil = new PreferenceUtil();
        return preferenceUtil;
    }
}
