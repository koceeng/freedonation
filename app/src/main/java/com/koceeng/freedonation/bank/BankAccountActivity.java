package com.koceeng.freedonation.bank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;

import java.util.List;

public class BankAccountActivity extends BaseActivity {

    private final String TAG = "BankAccountActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_account);

        DataPathUtil.getInstance().getBankAccount()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // TODO: 20/05/17

                        List<BankAccountGroup> bankAccountGroups = dataSnapshot.getValue(new GenericTypeIndicator<List<BankAccountGroup>>() {
                        });
                        Integer wtf = 1;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DebugUtil.getInstance().f(TAG, databaseError);
                        finish();
                    }
                });
    }

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, BankAccountActivity.class);
        }
    }
}
