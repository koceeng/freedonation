package com.koceeng.freedonation.impression;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

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

        setTag("ImpressionActivity");

        // fill content data
        DebugUtil.getInstance().v(TAG, "Prepare execute query: content_first");
        String contentQuery = getString(R.string.content_first);
        SQLiteUtils.getInstance(thisContext).executeQuery(contentQuery);
        DebugUtil.getInstance().v(TAG, "Successfully execute query: content_first");
    }

    public void actionImpressionNext(View view) {
        PreferenceUtil.getInstance().putBoolean(thisContext, getString(R.string.PREFERENCE_NOT_FIRST_LAUNCH), true);
        startActivity(HomeActivity.Factory.getIntent(this));
    }
}
