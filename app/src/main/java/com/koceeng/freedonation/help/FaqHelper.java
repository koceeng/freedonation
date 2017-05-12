package com.koceeng.freedonation.help;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;

import java.util.List;

public class FaqHelper {

    private final String TAG = "FaqHelper";
    public enum FaqStatus {GETTING_DATA, SUCCESS, FAILED}

    HomeActivity activity;

    public FaqHelper(HomeActivity activity) {
        this.activity = activity;
    }

    public void get() {
        activity.onFaqChangeStatus(FaqStatus.GETTING_DATA);
        DataPathUtil.getInstance().getFaq()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            activity.onFaqChangeStatus(FaqStatus.FAILED);
                            return;
                        }

                        List<Faq> faqs = dataSnapshot.getValue(new GenericTypeIndicator<List<Faq>>() {});
                        activity.onFaqChangeStatus(FaqStatus.SUCCESS, faqs);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DebugUtil.getInstance().f(TAG, databaseError);
                        activity.onFaqChangeStatus(FaqStatus.FAILED);
                    }
                });
    }
}
