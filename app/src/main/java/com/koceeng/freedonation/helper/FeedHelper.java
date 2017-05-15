package com.koceeng.freedonation.helper;

import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.DataPathUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import java.util.Random;

public class FeedHelper {

    private final String TAG = "FeedHelper";
    HomeActivity activity;
    InterstitialAd interstitialAd;
    Content result;
    boolean adInterstitialDone;
    boolean getFeedDataDone;
    int handleCounter = 0;
    int handleDelay = 500;
    int handleRepeat = 5;
    public FeedHelper(final HomeActivity activity) {
        this.activity = activity;
    }

    public void get(Boolean force) {
        get(force, false);
    }

    public void get(Boolean force, Boolean showAd) {
        // check last get data
        if (!force) {
            Content content = SQLiteUtils.getInstance(activity).getContent();
            if (content != null && System.currentTimeMillis() - content.getTimestamp() < 86400000) { // 86400000 is one day
                activity.onFeedChangeStatus(FeedStatus.NO_NEED);
                return;
            }
        }

        activity.onFeedChangeStatus(FeedStatus.START);
        adInterstitialDone = !showAd;
        getFeedDataDone = false;
        result = null;
        doGetFeed();

        if (showAd) {
            // show ad
            MobileAds.initialize(activity);

            interstitialAd = new InterstitialAd(activity);
            interstitialAd.setAdUnitId(activity.getString(R.string.ad_unit_interstitial));
            interstitialAd.loadAd(AdUtil.getInstance().getAdRequest());
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    adInterstitialDone = true;
                    afterGetFeed();
                }
            });

            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handleCounter++;
                    DebugUtil.getInstance().v(TAG, "handle run: " + handleCounter);
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else if (handleCounter >= handleRepeat) {
                        adInterstitialDone = true;
                        afterGetFeed();
                    } else {
                        handler.postDelayed(this, handleDelay);
                    }
                }
            };

            handleCounter = 0;
            handler.postDelayed(runnable, handleDelay);
        }
    }

    private void doGetFeed() {

        activity.onFeedChangeStatus(FeedStatus.GETTING_LAST);

        // get language
        String currentLang = PreferenceUtil.getInstance().getString(activity, activity.getString(R.string.PREFERENCE_LANGUAGE), false);
        if (currentLang.isEmpty())
            currentLang = activity.getString(R.string.PREFERENCE_LANGUAGE_IN);
        final String finalLang = currentLang;

        DataPathUtil.getInstance().getContentLast(finalLang)
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
                        DataPathUtil.getInstance().getContentById(finalLang, String.valueOf(randomInt))
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            activity.onFeedChangeStatus(FeedStatus.FAILED);
                                            return;
                                        }

                                        Content content = dataSnapshot.getValue(Content.class);
                                        content.setTimestamp(System.currentTimeMillis());
                                        result = content;

                                        getFeedDataDone = true;
                                        afterGetFeed();

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

    private void afterGetFeed() {
        if (!adInterstitialDone || !getFeedDataDone)
            return;

        if (result != null) {
            activity.onFeedChangeStatus(FeedStatus.SUCCESS);

        } else {
            activity.onFeedChangeStatus(FeedStatus.FAILED);
        }
    }

    public enum FeedStatus {NO_NEED, START, GETTING_LAST, GENERATING_RANDOM, GETTING_DATA, SUCCESS, SAVING_DATA, FAILED}
}
