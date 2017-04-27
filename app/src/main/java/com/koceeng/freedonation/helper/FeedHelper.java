package com.koceeng.freedonation.helper;

import android.widget.RadioButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.Random;

public class FeedHelper {

    private final String TAG = "FeedHelper";
    public enum FeedStatus { NO_NEED, GETTING_LAST, GENERATING_RANDOM, GETTING_DATA, SUCCESS, SAVING_DATA, FAILED }

    HomeActivity activity;

    public FeedHelper(final HomeActivity activity) {
        this.activity = activity;
    }

    public void get(Boolean force) {
        // check last get data
        if (!force) {
            Content content = SQLiteUtils.getInstance(activity).getContent();
            if (content != null && System.currentTimeMillis() - content.getTimestamp() < 86400000) // 86400000 is one day
                return;
        }

        activity.onFeedChangeStatus(FeedStatus.GETTING_LAST);

        // get language
        final String currentLang = PreferenceUtil.getInstance().getString(activity, activity.getString(R.string.PREFERENCE_LANGUAGE), false);
        DataPathUtil.getInstance().getContentLast(currentLang)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists() || dataSnapshot.getValue(Integer.class) == null) {
                            activity.onFeedChangeStatus(FeedStatus.FAILED);
                            return;
                        }

                        activity.onFeedChangeStatus(FeedStatus.GENERATING_RANDOM);
                        Random random = new Random();
                        int randomInt = random.nextInt(dataSnapshot.getValue(Integer.class));

                        activity.onFeedChangeStatus(FeedStatus.GETTING_DATA);
                        DataPathUtil.getInstance().getContentById(currentLang, String.valueOf(randomInt))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            activity.onFeedChangeStatus(FeedStatus.FAILED);
                                            return;
                                        }

                                        Content content = dataSnapshot.getValue(Content.class);
                                        content.setTimestamp(System.currentTimeMillis());
                                        activity.onFeedChangeStatus(FeedStatus.SUCCESS, content);

                                        SQLiteUtils.getInstance(activity).putContent(content);
                                        activity.onFeedChangeStatus(FeedStatus.SAVING_DATA);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        DebugUtil.getInstance().f(TAG, databaseError);
                                        activity.onFeedChangeStatus(FeedStatus.FAILED);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DebugUtil.getInstance().f(TAG, databaseError);
                        activity.onFeedChangeStatus(FeedStatus.FAILED);
                    }
                });
    }
}
