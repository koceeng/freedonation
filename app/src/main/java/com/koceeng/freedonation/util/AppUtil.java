package com.koceeng.freedonation.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.BuildConfig;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.object.VersionData;
import com.koceeng.freedonation.update.UpdateActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppUtil {

    private final String TAG = getClass().getSimpleName();

    public String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public void checkVersion(final BaseActivity activity) {
        FirebaseDatabase.getInstance().getReference("version").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /* no problem
                if (activity == null || activity.isFinishing())
                    return;
                */

                VersionData versionData = dataSnapshot.getValue(VersionData.class);
                String appVersion = getAppVersion().replace(".", "-");

                boolean updateCritical = (!versionData.getSupported().containsKey(appVersion) || !versionData.getSupported().get(appVersion));
                boolean updateNotLatest = (versionCompare(versionData.getCurrent(), appVersion) > 0);

                if (updateCritical || updateNotLatest)
                    activity.startActivity(UpdateActivity.Factory.getIntent(activity, updateCritical));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                DebugUtil.getInstance().e(TAG, databaseError.getMessage());
                Toast.makeText(activity, activity.getString(R.string.update_check_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * note: It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\-");
        String[] vals2 = str2.split("\\-");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    public boolean validateSnapshot(DataSnapshot dataSnapshot, String key) {
        return (dataSnapshot.hasChild(key)
                && dataSnapshot.child(key).exists()
                && dataSnapshot.child(key).getValue() != null);
    }

    public boolean validateStringSnapshot(DataSnapshot dataSnapshot, String key) {
        return (validateSnapshot(dataSnapshot, key)
                && dataSnapshot.child(key).getValue().toString() != null
                && !dataSnapshot.child(key).getValue().toString().isEmpty());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean checkGooglePlayServices(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            /* show error dialog
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            */

            // automatically download google play service
            googleApiAvailability.makeGooglePlayServicesAvailable(activity);
            return false;
        }
        return true;
    }

    public boolean isAppOnForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
        }

        return true;
    }

    private static AppUtil appUtil = null;

    public static AppUtil getInstance() {
        if (appUtil == null)
            appUtil = new AppUtil();
        return appUtil;
    }
}
