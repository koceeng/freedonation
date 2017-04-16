package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdView;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.object.HomeMenu;
import com.koceeng.freedonation.object.HomeMenuList;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.LayoutUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    public static final String IX_FROM_SPLASH = "IX_FROM_SPLASH";

    @BindView(R.id.home_text_app_name) TextSwitcher textAppName;
    @BindView(R.id.home_adview_bottom) AdView adViewBottom;
    @BindView(R.id.home_viewflipper) ViewFlipper viewFlipper;

    // feed page
    @BindView(R.id.feed_text_title) TextView textFeedTitle;

    // report page
    @BindView(R.id.report_text_title) TextView textReportTitle;

    // setting page
    @BindView(R.id.setting_text_title) TextSwitcher textSettingTitle;
    @BindView(R.id.setting_text_language) TextSwitcher textLanguage;
    @BindView(R.id.setting_spinner_language) AppCompatSpinner spinnerLanguage;
    @BindView(R.id.setting_text_ad_interstitial) TextSwitcher textAdInterstitial;
    @BindView(R.id.setting_switch_ad_interstitial) SwitchCompat switchAdInterstitial;

    HomeActivity homeActivity;
    HomeMenuList homeMenuList;
//    private int animationHeightFirst = 0;
//    private int animationHeightActual = 0;
//    private int animationHeightInitial;

    // setting
    boolean settingHoldChange = false;

    public static class Factory {
        public enum IntentType { FLAG_CLEAR }
        public static Intent getIntent(Context context, IntentType... intentTypes) {
            Intent intent = new Intent(context, HomeActivity.class);
            for (IntentType intentType : intentTypes) {
                switch (intentType) {
                    case FLAG_CLEAR:
                        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        break;
                }
            }

            if (context instanceof SplashActivity) {
                intent.putExtra(IX_FROM_SPLASH, true);
            }

            return intent;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutId(R.layout.home);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        homeActivity = this;
        setTag("HomeActivity");

        adViewBottom.loadAd(AdUtil.getInstance().getAdRequest());

        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAppName, R.dimen.text_mid_large, R.color.colorThemeWhite, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

        LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

        viewFlipper.setInAnimation(thisContext, R.anim.activity_reversed_in);
        viewFlipper.setOutAnimation(thisContext, R.anim.activity_reversed_out);

        homeMenuList = new HomeMenuList(thisContext, viewFlipper);
        homeMenuList.putItem(HomeMenuList.Name.FEED, new HomeMenu(
                findViewById(R.id.home_layout_feed),
                findViewById(R.id.home_layout_feed_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_feed),
                findViewById(R.id.feed_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.REPORT, new HomeMenu(
                findViewById(R.id.home_layout_report),
                findViewById(R.id.home_layout_report_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_report),
                findViewById(R.id.report_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.SETTING, new HomeMenu(
                findViewById(R.id.home_layout_setting),
                findViewById(R.id.home_layout_setting_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_setting),
                findViewById(R.id.setting_layout_parent)
        ));

        homeMenuList.setActive(HomeMenuList.Name.FEED, false);

        initSetting();

        onLanguageChange();
    }

    private void initSetting() {
        settingHoldChange = true;

        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textSettingTitle, R.dimen.text_extra_big, R.color.colorPrimary);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textLanguage, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAdInterstitial, R.dimen.text_small);

        spinnerLanguage.setAdapter(new ArrayAdapter<>(thisContext,
                R.layout.setting_spinner, new String[]{
                getString(R.string.PREFERENCE_LANGUAGE_IN_LABEL),
                getString(R.string.PREFERENCE_LANGUAGE_EN_LABEL)
        }));
//        List<String> wtf = new ArrayList<>();
//        wtf.add("INDON");
//        wtf.add("ENGL");
//        spinnerLanguage.setAdapter();
        String preferenceLang = PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE));
        spinnerLanguage.setSelection((preferenceLang != null && preferenceLang.equals(getString(R.string.PREFERENCE_LANGUAGE_EN))) ? 1 : 0);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (settingHoldChange)
                    return;

                settingHoldChange = true;

                String newLang = (i == 0) ? getString(R.string.PREFERENCE_LANGUAGE_IN) : getString(R.string.PREFERENCE_LANGUAGE_EN);
                String currentLang = PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE));
                if (currentLang != null && newLang.equals(currentLang)) {
                    settingHoldChange = false;
                    return;
                }

                Resources res = getResources();
                Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(newLang));
                res.updateConfiguration(conf, res.getDisplayMetrics());
                PreferenceUtil.getInstance().putString(thisContext, getString(R.string.PREFERENCE_LANGUAGE), newLang);

                onLanguageChange();

                settingHoldChange = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        Boolean preferenceAdInterstitial = !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_SHOW_INTERSTITIAL_AD));
        switchAdInterstitial.setChecked(preferenceAdInterstitial);
        switchAdInterstitial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingHoldChange)
                    return;

                settingHoldChange = true;
                PreferenceUtil.getInstance().putBoolean(thisContext, getString(R.string.PREFERENCE_SHOW_INTERSTITIAL_AD), !isChecked);
                settingHoldChange = false;
            }
        });

        settingHoldChange = false;
    }

    private void onLanguageChange() {
        LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));
        LayoutUtil.getInstance().setText(textFeedTitle, getString(R.string.feed_title));
        LayoutUtil.getInstance().setText(textReportTitle, getString(R.string.report_title));
        LayoutUtil.getInstance().setText(textSettingTitle, getString(R.string.setting_title));
        LayoutUtil.getInstance().setText(textLanguage, getString(R.string.setting_language_label));
        LayoutUtil.getInstance().setText(textAdInterstitial, getString(R.string.setting_ad_interstitial_label));
    }
}
