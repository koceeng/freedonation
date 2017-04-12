package com.koceeng.freedonation.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    SplashActivity splashActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashActivity = this;

        // TODO: 12-Apr-17 show interstitial ads

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(HomeActivity.Factory.getIntent(splashActivity));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }, 1000);
    }
}
