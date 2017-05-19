package com.koceeng.freedonation.changelog;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.sqlite.SQLiteUtil;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangelogActivity extends BaseActivity {

    private final String TAG = "ChangelogActivity";

    @BindView(R.id.changelog_toolbar)
    Toolbar toolbar;
    @BindView(R.id.changelog_appbarlayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.changelog_layout_loading)
    View layoutLoading;
    @BindView(R.id.changelog_progressbar)
    ProgressBar progressBar;
    @BindView(R.id.changelog_scroll)
    NestedScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog);
        ButterKnife.bind(this);

        prepareChangelist();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.changelog_title));
        }

        LayoutUtil.getInstance().autoElevateToolbar(appBarLayout, scrollView);

        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.colorThemeWhite), PorterDuff.Mode.SRC_IN);
    }

    private void prepareChangelist() {
        DataPathUtil.getInstance().getChangelogUpdateCode()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(thisContext, getString(R.string.changelog_error_message), Toast.LENGTH_SHORT).show();
                            DebugUtil.getInstance().e(TAG, "changelist updatecode is empty");
                            finish();
                            return;
                        }

                        String updatecode = SQLiteUtil.getInstance(thisContext).getStringParam(SQLiteUtil.PARAM_CHANGELOG_UPDATE_CODE);
                        if (updatecode == null || !dataSnapshot.getValue(Long.class).toString().equals(updatecode)) {
                            SQLiteUtil.getInstance(thisContext).putStringParam(SQLiteUtil.PARAM_CHANGELOG_UPDATE_CODE, dataSnapshot.getValue(Long.class).toString());
                            getChangelistData();

                        } else {
                            showLayout();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(thisContext, getString(R.string.changelog_error_message), Toast.LENGTH_SHORT).show();
                        DebugUtil.getInstance().f(TAG, databaseError);
                        finish();
                    }
                });
    }

    private void getChangelistData() {
        DataPathUtil.getInstance().getChangelog()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(thisContext, getString(R.string.changelog_error_message), Toast.LENGTH_SHORT).show();
                            DebugUtil.getInstance().e(TAG, "changelist updatecode is empty");
                            finish();
                            return;
                        }

                        SQLiteUtil.getInstance(thisContext).clearChangelog();
                        for (DataSnapshot versionSnapshot : dataSnapshot.getChildren()) {
                            if (versionSnapshot.getKey().equals("updatecode"))
                                continue;

                            String versionCode = versionSnapshot.getKey().replace("-", ".");
                            String versionName = versionSnapshot.child("name").getValue(String.class);
                            Boolean versionCritical = versionSnapshot.child("critical").getValue(Boolean.class);
                            if (versionSnapshot.hasChild("features")) {
                                for (DataSnapshot featureSnapshot : versionSnapshot.child("features").getChildren()) {
                                    SQLiteUtil.getInstance(thisContext).addChangelogEntry(new ChangelogEntry(
                                            versionCode,
                                            versionName,
                                            versionCritical,
                                            ChangelogEntry.KIND_FEATURE,
                                            featureSnapshot.child("type").getValue(String.class),
                                            featureSnapshot.child("note").getValue(String.class)
                                    ));
                                }
                            }

                            if (versionSnapshot.hasChild("bugfixes")) {
                                for (DataSnapshot bugfixSnapshot : versionSnapshot.child("bugfixes").getChildren()) {
                                    SQLiteUtil.getInstance(thisContext).addChangelogEntry(new ChangelogEntry(
                                            versionCode,
                                            versionName,
                                            versionCritical,
                                            ChangelogEntry.KIND_BUGFIX,
                                            bugfixSnapshot.child("type").getValue(String.class),
                                            bugfixSnapshot.child("note").getValue(String.class)
                                    ));
                                }
                            }
                        }

                        showLayout();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(thisContext, getString(R.string.changelog_error_message), Toast.LENGTH_SHORT).show();
                        DebugUtil.getInstance().f(TAG, databaseError);
                        finish();
                    }
                });
    }

    private void showLayout() {
        List<ChangelogEntry> changelistEntries = SQLiteUtil.getInstance(thisContext).getChangelogEntries();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.changelog_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(thisContext));
        recyclerView.setAdapter(new ChangelogAdapter(changelistEntries));
        recyclerView.setHasFixedSize(true);

        LayoutUtil.getInstance().toggleVisibility(layoutLoading, false);
        LayoutUtil.getInstance().toggleVisibility(scrollView, true);
    }

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, ChangelogActivity.class);
        }
    }
}
