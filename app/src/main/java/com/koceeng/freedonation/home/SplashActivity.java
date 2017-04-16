package com.koceeng.freedonation.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

public class SplashActivity extends BaseActivity {

    SplashActivity splashActivity;

    InterstitialAd interstitialAd;

    int handleCounter = 0;
    int handleDelay = 500;
    int handleRepeat = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashActivity = this;

        Boolean preferenceAdInterstitial = !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_SHOW_INTERSTITIAL_AD));

        if (!preferenceAdInterstitial) {
            showHomeActivity();
            return;
        }

        MobileAds.initialize(thisContext);

        interstitialAd = new InterstitialAd(thisContext);
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_interstitial));
        interstitialAd.loadAd(AdUtil.getInstance().getAdRequest());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                showHomeActivity();
            }
        });

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleCounter++;
                Log.e(TAG, "handle run: " + handleCounter);
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else if (handleCounter >= handleRepeat) {
                    showHomeActivity();
                } else {
                    handler.postDelayed(this, handleDelay);
                }
            }
        };

        handler.postDelayed(runnable, handleDelay);
    }

    private void showHomeActivity() {
        startActivity(HomeActivity.Factory.getIntent(splashActivity));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
