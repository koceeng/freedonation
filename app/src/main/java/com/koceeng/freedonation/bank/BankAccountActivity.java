package com.koceeng.freedonation.bank;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankAccountActivity extends BaseActivity {

    private final String TAG = "BankAccountActivity";

    @BindView(R.id.bank_account_toolbar) Toolbar toolbar;
    @BindView(R.id.bank_account_layout_loading) View layoutLoading;
    @BindView(R.id.bank_account_layout_main) View layoutMain;
    @BindView(R.id.bank_account_progressbar) ProgressBar progressBar;
    @BindView(R.id.bank_account_recyclerview) RecyclerView recyclerView;

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, BankAccountActivity.class);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_account);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.bank_account_title));
        }

        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));

        DataPathUtil.getInstance().getBankAccount()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists() || dataSnapshot.getChildrenCount() == 0) {
                            Toast.makeText(thisContext, getString(R.string.bank_account_error_message), Toast.LENGTH_SHORT).show();
                            DebugUtil.getInstance().e(TAG, "datasnapshot is empty");
                            finish();
                            return;
                        }

                        List<BankAccountGroup> bankAccountGroups = dataSnapshot.getValue(new GenericTypeIndicator<List<BankAccountGroup>>() {
                        });

                        List<Pair<String, BankAccount>> bankAccountPairList = new ArrayList<>();
                        for (BankAccountGroup group : bankAccountGroups) {
                            List<BankAccount> groupContent = group.getGroupContent();
                            if (groupContent == null || groupContent.size() <= 0)
                                continue;

                            for (int i = 0; i < groupContent.size(); i++) {
                                bankAccountPairList.add(new Pair<>(
                                        i == 0 ? group.getGroupName() : "",
                                        groupContent.get(i)
                                ));
                            }
                        }

                        recyclerView.setAdapter(new BankAccountRecyclerAdapter(thisContext, bankAccountPairList));

                        LayoutUtil.getInstance().toggleVisibility(layoutLoading, false);
                        LayoutUtil.getInstance().toggleVisibility(layoutMain, true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(thisContext, getString(R.string.changelog_error_message), Toast.LENGTH_SHORT).show();
                        DebugUtil.getInstance().f(TAG, databaseError);
                        finish();
                    }
                });
    }
}
