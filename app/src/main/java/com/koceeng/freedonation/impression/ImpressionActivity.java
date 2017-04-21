package com.koceeng.freedonation.impression;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.PreferenceUtil;

import butterknife.ButterKnife;

public class ImpressionActivity extends BaseActivity {

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, ImpressionActivity.class);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impression);
        ButterKnife.bind(this);

        // TODO: 21/04/17 run this on success
        PreferenceUtil.getInstance().putBoolean(thisContext, getString(R.string.PREFERENCE_NOT_FIRST_LAUNCH), false);
    }
}
