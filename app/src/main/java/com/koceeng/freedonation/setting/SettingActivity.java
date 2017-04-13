package com.koceeng.freedonation.setting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.setting_switch_ad_interstitial) SwitchCompat switchAdInterstitial;

    SettingActivity settingActivity;

    boolean holdChange = false;

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, SettingActivity.class);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);

        settingActivity = this;
        setTag("SettingActivity");

        initToolbar(getString(R.string.setting_title));

        Log.e(TAG, "onCreate: dsda");

        String preferenceLang = PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE));
        switchAdInterstitial.setChecked(preferenceLang != null && preferenceLang.equals(getString(R.string.PREFERENCE_LANGUAGE_IN)));

        switchAdInterstitial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holdChange)
                    return;

                holdChange = true;

                // TODO: 13/04/17
                String lang = (switchAdInterstitial.isChecked()) ? getString(R.string.PREFERENCE_LANGUAGE_IN) : getString(R.string.PREFERENCE_LANGUAGE_EN);
                PreferenceUtil.getInstance().putString(thisContext, getString(R.string.PREFERENCE_LANGUAGE), lang);

                Resources res = getResources();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.setLocale(new Locale(lang));
                res.updateConfiguration(conf, res.getDisplayMetrics());

                recreate();

                holdChange = false;
            }
        });
    }
}
