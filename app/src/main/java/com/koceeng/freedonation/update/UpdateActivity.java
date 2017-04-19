package com.koceeng.freedonation.update;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.IntentUtil;

import butterknife.ButterKnife;

public class UpdateActivity extends BaseActivity {

    public static final String IX_UPDATE_CRITICAL = "IX_UPDATE_CRITICAL";

    boolean isCritical;

    public static class Factory {
        public static Intent getIntent(Context context, Boolean isCritical) {
            Intent intent = new Intent(context, UpdateActivity.class);
            if (isCritical)
                intent.putExtra(IX_UPDATE_CRITICAL, true);
            return intent;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);
        ButterKnife.bind(this);

        isCritical = IntentUtil.getInstance().validateBooleanIntent(getIntent(), IX_UPDATE_CRITICAL);
    }
}
