package com.koceeng.freedonation.helper;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.bottomsheet.LanguageBottomSheet;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.util.PreferenceUtil;

public class SettingHelper {

    public enum Type {LANGUAGE, HIDE_CONTENT_DETAIL, AD_INTERSTITIAL, NOTIFICATION}

    private HomeActivity context;

    public SettingHelper(HomeActivity context) {
        this.context = context;
    }

    public void changeLanguage() {
        LanguageBottomSheet languageBottomSheet = new LanguageBottomSheet();
        languageBottomSheet.setHomeActivity(context);
        languageBottomSheet.show(context.getSupportFragmentManager(), languageBottomSheet.getTag());
    }

    public void changeHideContentDetail() {
        PreferenceUtil.getInstance().putBoolean(context, context.getString(R.string.PREFERENCE_HIDE_CONTENT_DETAIL),
                !PreferenceUtil.getInstance().getBoolean(context, context.getString(R.string.PREFERENCE_HIDE_CONTENT_DETAIL)));
    }

    public void changeAdInterstitial() {
        PreferenceUtil.getInstance().putBoolean(context, context.getString(R.string.PREFERENCE_HIDE_INTERSTITIAL_AD),
                !PreferenceUtil.getInstance().getBoolean(context, context.getString(R.string.PREFERENCE_HIDE_INTERSTITIAL_AD)));
    }
}
