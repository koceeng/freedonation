package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdView;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.bottomsheet.NotificationBottomSheet;
import com.koceeng.freedonation.helper.FeedHelper;
import com.koceeng.freedonation.helper.SettingHelper;
import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.object.HomeMenu;
import com.koceeng.freedonation.object.HomeMenuList;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.IntentUtil;
import com.koceeng.freedonation.util.LayoutUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_text_app_name) TextSwitcher textAppName;
    @BindView(R.id.home_adview_bottom) AdView adViewBottom;
    @BindView(R.id.home_viewflipper) ViewFlipper viewFlipper;

    // feed page
    @BindView(R.id.feed_text_title) TextView textFeedTitle;

    // report page
    @BindView(R.id.report_text_title) TextView textReportTitle;
    @BindView(R.id.report_text_note) TextView textReportNote;
    @BindView(R.id.report_text_note_future_release) TextView textReportNoteFutureRelease;
    @BindView(R.id.report_button) Button buttonReport;

    // setting page
    @BindView(R.id.setting_text_title) TextSwitcher textSettingTitle;
    @BindView(R.id.setting_text_language_label) TextSwitcher textLanguageLabel;
    @BindView(R.id.setting_text_language_value) TextSwitcher textLanguageValue;
    @BindView(R.id.setting_text_ad_interstitial_label) TextSwitcher textAdInterstitialLabel;
    @BindView(R.id.setting_text_ad_interstitial_value) TextSwitcher textAdInterstitialValue;
    @BindView(R.id.setting_text_notification_label) TextSwitcher textNotificationLabel;
    @BindView(R.id.setting_text_notification_value) TextSwitcher textNotificationValue;

    // share page
    @BindView(R.id.share_edittext_comment) EditText editTextShareComment;

    HomeActivity homeActivity;
    HomeMenuList homeMenuList;

    // setting
    FeedHelper feedHelper;
    SettingHelper settingHelper;
    boolean settingHoldChange = false;

    public static class Factory {
        public static Intent getIntent(Context context) {
            return new Intent(context, HomeActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);

        homeActivity = this;
        setTag("HomeActivity");

        adViewBottom.loadAd(AdUtil.getInstance().getAdRequest());

        feedHelper = new FeedHelper(this);
        settingHelper = new SettingHelper(this);

        viewFlipper.setInAnimation(thisContext, R.anim.activity_reversed_in);
        viewFlipper.setOutAnimation(thisContext, R.anim.activity_reversed_out);

        homeMenuList = new HomeMenuList(thisContext, viewFlipper);
        homeMenuList.putItem(HomeMenuList.Name.FEED, new HomeMenu(
                findViewById(R.id.home_layout_feed),
                findViewById(R.id.home_layout_feed_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_feed),
                findViewById(R.id.feed_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.REPORT, new HomeMenu(
                findViewById(R.id.home_layout_report),
                findViewById(R.id.home_layout_report_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_report),
                findViewById(R.id.report_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.SETTING, new HomeMenu(
                findViewById(R.id.home_layout_setting),
                findViewById(R.id.home_layout_setting_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_setting),
                findViewById(R.id.setting_layout_parent)
        ));
        homeMenuList.putItem(HomeMenuList.Name.OTHER_APP, new HomeMenu(
                findViewById(R.id.home_layout_other_app),
                findViewById(R.id.home_layout_other_app_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_other_app)
        ));
        homeMenuList.putItem(HomeMenuList.Name.SHARE, new HomeMenu(
                findViewById(R.id.home_layout_share),
                findViewById(R.id.home_layout_share_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_share),
                findViewById(R.id.share_layout_parent),
                editTextShareComment
        ));
        homeMenuList.putItem(HomeMenuList.Name.HELP, new HomeMenu(
                findViewById(R.id.home_layout_help),
                findViewById(R.id.home_layout_help_indicator),
                (AppCompatImageView) findViewById(R.id.home_image_help),
                findViewById(R.id.help_layout_parent)
        ));

        homeMenuList.setActive(HomeMenuList.Name.FEED, false);

        prepareTextSwitcher();
        onLanguageChange(null);

        feedHelper.get(false);
    }

    private void prepareTextSwitcher() {
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAppName, R.dimen.text_mid_large, R.color.colorThemeWhite, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        // setting
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textSettingTitle, R.dimen.text_extra_big, R.color.colorPrimary);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textLanguageLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textLanguageValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAdInterstitialLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAdInterstitialValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textNotificationLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textNotificationValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
    }

    public void onFeedChangeStatus(FeedHelper.FeedStatus feedStatus) {
        onFeedChangeStatus(feedStatus, null);
    }

    public void onFeedChangeStatus(FeedHelper.FeedStatus feedStatus, Content content) {
        if (feedStatus == null) {
            DebugUtil.getInstance().e(TAG, "FeedChangeStatus: feedStatus is empty");
            return;
        }

        DebugUtil.getInstance().v(TAG, "FeedChangeStatus: " + feedStatus + (content != null ? "|content:" + content : ""));

        // TODO: 27-Apr-17
    }

    public void actionReport(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://freedonation.koceeng.com/report")));
    }

    public void actionSettingLanguage(View view) {
        if (settingHoldChange)
            return;

        settingHoldChange = true;
        settingHelper.changeLanguage();
        settingHoldChange = false;
    }

    public void actionSettingAdInterstitial(View view) {
        if (settingHoldChange)
            return;

        settingHoldChange = true;
        settingHelper.changeAdInterstitial();
        onLanguageChange(SettingHelper.Type.AD_INTERSTITIAL);
        settingHoldChange = false;
    }

    public void actionSettingNotification(View view) {
        settingHoldChange = true;
        NotificationBottomSheet notificationBottomSheet = new NotificationBottomSheet();
        notificationBottomSheet.setHomeActivity(homeActivity);
        notificationBottomSheet.show(getSupportFragmentManager(), notificationBottomSheet.getTag());
        settingHoldChange = false;
    }

    public void actionShare(View view) {
        String shareText = "";
        if (editTextShareComment != null && editTextShareComment.getText() != null
                && !editTextShareComment.getText().toString().trim().isEmpty())
            shareText += editTextShareComment.getText().toString().trim() + "\n\n";

        shareText += IntentUtil.getInstance().getMarketUrl(thisContext);

        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(intent, getString(R.string.share_choose_app)));

        } catch(Exception e) {
            DebugUtil.getInstance().e(TAG, e.toString());
            Toast.makeText(thisContext, getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
        }
    }

    public void onLanguageChange(final SettingHelper.Type type, boolean handle) {
        if (handle) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLanguageChange(type);
                }
            }, 300);
        } else {
            onLanguageChange(type);
        }
    }

    public void onLanguageChange(SettingHelper.Type type) {
        if (type == null) {
            LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

            LayoutUtil.getInstance().setText(textFeedTitle, getString(R.string.feed_title));

            LayoutUtil.getInstance().setText(textReportTitle, getString(R.string.report_title));
            LayoutUtil.getInstance().setText(textReportNote, getString(R.string.report_note));
            LayoutUtil.getInstance().setText(textReportNoteFutureRelease, getString(R.string.report_note_future_release));
            LayoutUtil.getInstance().setText(buttonReport, getString(R.string.report_button_text));

            LayoutUtil.getInstance().setText(textSettingTitle, getString(R.string.setting_title));
            LayoutUtil.getInstance().setText(textLanguageLabel, getString(R.string.setting_language_label));
            LayoutUtil.getInstance().setText(textAdInterstitialLabel, getString(R.string.setting_ad_interstitial_label));
            LayoutUtil.getInstance().setText(textNotificationLabel, getString(R.string.setting_notification_label));
        }

        if (type == null || type.equals(SettingHelper.Type.LANGUAGE)) {
            LayoutUtil.getInstance().setText(textLanguageValue, getString(
                    (PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE), false).equals(getString(R.string.PREFERENCE_LANGUAGE_EN))) ?
                            R.string.PREFERENCE_LANGUAGE_EN_LABEL : R.string.PREFERENCE_LANGUAGE_IN_LABEL));
        }

        if (type == null || type.equals(SettingHelper.Type.AD_INTERSTITIAL)) {
            LayoutUtil.getInstance().setText(textAdInterstitialValue, getString(
                    !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_HIDE_INTERSTITIAL_AD)) ?
                            R.string.yes : R.string.no));
        }

        if (type == null || type.equals(SettingHelper.Type.NOTIFICATION)) {
            // TODO: 22-Apr-17 from sqlite
            LayoutUtil.getInstance().setText(textNotificationValue, "TODO");
        }
    }
}
