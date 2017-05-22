package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.impression.ImpressionActivity;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.AppUtil;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

public class SplashActivity extends BaseActivity {

    SplashActivity splashActivity;

    InterstitialAd interstitialAd;

    int handleCounter = 0;
    int handleDelay = 500;
    int handleRepeat = 10;

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, SplashActivity.class);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashActivity = this;

        checkIfActive();
        AppUtil.getInstance().checkVersion(this);

        if (!PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_NOT_FIRST_LAUNCH))) {
            startActivity(ImpressionActivity.Factory.getIntent(this));
            return;
        }

        Boolean preferenceAdInterstitial = !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_HIDE_INTERSTITIAL_AD));
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

    private void checkIfActive() {
        DataPathUtil.getInstance().getIsActive().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || !dataSnapshot.getValue(Boolean.class))
                    startActivity(NotActiveActivity.Factory.getIntent(splashActivity));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DebugUtil.getInstance().f(TAG, databaseError);
                startActivity(NotActiveActivity.Factory.getIntent(splashActivity));
            }
        });
    }

    private void showHomeActivity() {
        startActivity(HomeActivity.Factory.getIntent(splashActivity));
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }
}
