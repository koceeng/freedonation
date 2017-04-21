package com.koceeng.freedonation.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LanguageUtil;

import org.greenrobot.eventbus.EventBus;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    public String TAG = "BaseActivity";

    protected Activity thisActivity;
    protected AppCompatActivity thisAppCompatActivity;
    protected BaseActivity thisBaseActivity;
    protected Context thisContext;

    protected Boolean isActivityVisible = false;
    private Boolean isRegisterEventBus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DebugUtil.getInstance().v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        thisActivity = this;
        thisAppCompatActivity = this;
        thisBaseActivity = this;
        thisContext = this;

        LanguageUtil.getInstance().updateLanguageResource(thisContext);

        // transparent status bar
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isRegisterEventBus)
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        DebugUtil.getInstance().v(TAG, "onResume");
        super.onResume();
        isActivityVisible = true;
    }

    @Override
    protected void onPause() {
        DebugUtil.getInstance().v(TAG, "onPause");
        super.onPause();
        isActivityVisible = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRegisterEventBus && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        DebugUtil.getInstance().v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DebugUtil.getInstance().v(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DebugUtil.getInstance().v(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        DebugUtil.getInstance().v(TAG, "attachBaseContext");
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setTag(String tag) {
        this.TAG = tag;
    }

    protected void registerEventBus() {
        isRegisterEventBus = true;
    }

    protected FirebaseUser getUser() {
        DebugUtil.getInstance().v(TAG, "getUser");

        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void inputFieldAction() {
        // super first
        DebugUtil.getInstance().v(TAG, "inputFieldAction");
    }
}
