package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.TextSwitcher;

import com.google.android.gms.ads.AdView;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.object.HomeMenu;
import com.koceeng.freedonation.object.HomeMenuList;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.LayoutUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    public static final String IX_FROM_SPLASH = "IX_FROM_SPLASH";

    @BindView(R.id.home_text_app_name) TextSwitcher textAppName;
    @BindView(R.id.home_adview_bottom) AdView adViewBottom;

    // setting page
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

        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAppName, R.dimen.text_mid_large);
        LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

        homeMenuList = new HomeMenuList(thisContext);
        homeMenuList.putItem(HomeMenuList.Name.FEED, new HomeMenu(
                findViewById(R.id.home_layout_feed),
                findViewById(R.id.home_layout_feed_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_feed),
                findViewById(R.id.feed_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.SETTING, new HomeMenu(
                findViewById(R.id.home_layout_setting),
                findViewById(R.id.home_layout_setting_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_setting),
                findViewById(R.id.setting_layout_parent)
        ));

        homeMenuList.setActive(HomeMenuList.Name.FEED);

        initSetting();
    }

    private void initSetting() {
        String preferenceLang = PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE));
        switchAdInterstitial.setChecked(preferenceLang != null && preferenceLang.equals(getString(R.string.PREFERENCE_LANGUAGE_IN)));

        switchAdInterstitial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (settingHoldChange)
                    return;

                settingHoldChange = true;

                // TODO: 13/04/17
                String lang = (switchAdInterstitial.isChecked()) ? getString(R.string.PREFERENCE_LANGUAGE_IN) : getString(R.string.PREFERENCE_LANGUAGE_EN);
                PreferenceUtil.getInstance().putString(thisContext, getString(R.string.PREFERENCE_LANGUAGE), lang);

                Resources res = getResources();
                Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(lang));
                res.updateConfiguration(conf, res.getDisplayMetrics());

                // TODO: 15/04/17
                // recreate();
                LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

                settingHoldChange = false;
            }
        });
    }
}
