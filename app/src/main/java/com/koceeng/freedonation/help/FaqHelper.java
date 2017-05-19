package com.koceeng.freedonation.help;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.sqlite.SQLiteUtil;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.List;

public class FaqHelper {

    private final String TAG = "FaqHelper";
    HomeActivity activity;

    public FaqHelper(HomeActivity activity) {
        this.activity = activity;
    }

    public void get(boolean force) {

        // check last get data
        if (!force) {
            String lastFaqTimestampString = SQLiteUtil.getInstance(activity).getStringParam(SQLiteUtil.PARAM_LAST_FAQ_TIMESTAMP);
            Long lastFaqTimestamp = 0L;
            try {
                if (lastFaqTimestampString != null)
                    lastFaqTimestamp = Long.parseLong(lastFaqTimestampString);
            } catch (Exception e) {
                DebugUtil.getInstance().e(TAG, "lastFaqTimestamp String to Long conversion error: " + e.getMessage());
            }

            if (System.currentTimeMillis() - lastFaqTimestamp < 86400000) { // 86400000 is one day
                activity.onFaqChangeStatus(FaqStatus.NO_NEED);
                return;
            }
        }

        activity.onFaqChangeStatus(FaqStatus.START);

        // get language
        String currentLang = PreferenceUtil.getInstance().getString(activity, activity.getString(R.string.PREFERENCE_LANGUAGE), false);
        if (currentLang.isEmpty())
            currentLang = activity.getString(R.string.PREFERENCE_LANGUAGE_IN);

        DataPathUtil.getInstance().getFaq(currentLang)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            activity.onFaqChangeStatus(FaqStatus.FAILED);
                            return;
                        }

                        List<Faq> faqs = dataSnapshot.getValue(new GenericTypeIndicator<List<Faq>>() {});
                        activity.onFaqChangeStatus(FaqStatus.SAVING_DATA);

                        SQLiteUtil.getInstance(activity).putFaqs(faqs);
                        SQLiteUtil.getInstance(activity).putStringParam(SQLiteUtil.PARAM_LAST_FAQ_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

                        activity.onFaqChangeStatus(FaqStatus.SUCCESS);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DebugUtil.getInstance().f(TAG, databaseError);
                        activity.onFaqChangeStatus(FaqStatus.FAILED);
                    }
                });
    }

    public enum FaqStatus {NO_NEED, START, SAVING_DATA, SUCCESS, FAILED}
}
