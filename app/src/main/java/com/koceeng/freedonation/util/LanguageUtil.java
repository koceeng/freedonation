package com.koceeng.freedonation.util;

import android.content.Context;
import android.content.res.Resources;

import com.koceeng.freedonation.R;

import java.util.Locale;

public class LanguageUtil {

    public Locale getCurrentLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }

    public void updateLanguageResource(Context context) {
        this.updateLanguageResource(context, PreferenceUtil.getInstance().getString(context, context.getString(R.string.PREFERENCE_LANGUAGE)));
    }

    public void updateLanguageResource(Context context, String lang) {
        if (lang == null)
            lang = context.getString(R.string.PREFERENCE_LANGUAGE_IN);
        Resources res = context.getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    private static LanguageUtil languageUtil;

    public static LanguageUtil getInstance() {
        if (languageUtil == null)
            languageUtil = new LanguageUtil();
        return languageUtil;
    }
}
