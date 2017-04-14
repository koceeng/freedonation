package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.setting.SettingActivity;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    public static final String IX_FROM_SPLASH = "IX_FROM_SPLASH";

    @BindView(R.id.home_adview_bottom) AdView adViewBottom;

    HomeActivity homeActivity;

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
    }

    public void actionHome(View view) {
        recreate();
    }

    public void actionSetting(View view) {
        startActivity(SettingActivity.Factory.getIntent(homeActivity));
    }
}
