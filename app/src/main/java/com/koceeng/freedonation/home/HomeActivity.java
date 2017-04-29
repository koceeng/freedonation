package com.koceeng.freedonation.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseActivity;
import com.koceeng.freedonation.bottomsheet.NotificationBottomSheet;
import com.koceeng.freedonation.helper.FeedHelper;
import com.koceeng.freedonation.helper.SettingHelper;
import com.koceeng.freedonation.object.Content;
import com.koceeng.freedonation.object.HomeMenu;
import com.koceeng.freedonation.object.HomeMenuList;
import com.koceeng.freedonation.sqlite.SQLiteUtils;
import com.koceeng.freedonation.util.AdUtil;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.IntentUtil;
import com.koceeng.freedonation.util.LanguageUtil;
import com.koceeng.freedonation.util.LayoutUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_text_app_name) TextSwitcher textAppName;
    @BindView(R.id.home_adview_bottom) AdView adViewBottom;
    @BindView(R.id.home_viewflipper) ViewFlipper viewFlipper;

    // feed page
    @BindView(R.id.feed_progress) View progressFeed;
    @BindView(R.id.feed_text_title) TextView textFeedTitle;
    @BindView(R.id.feed_text_content_title) TextSwitcher textFeedContentTitle;
    @BindView(R.id.feed_text_content_subtitle) TextSwitcher textFeedContentSubtitle;
    @BindView(R.id.feed_text_content_text) TextSwitcher textFeedContentText;
    @BindView(R.id.feed_text_content_footer) TextSwitcher textFeedContentFooter;

    // report page
    @BindView(R.id.report_text_title) TextView textReportTitle;
    @BindView(R.id.report_text_note) TextView textReportNote;
    @BindView(R.id.report_text_note_future_release) TextView textReportNoteFutureRelease;
    @BindView(R.id.report_button) Button buttonReport;

    // setting page
    @BindView(R.id.setting_text_title) TextSwitcher textSettingTitle;
    @BindView(R.id.setting_text_language_label) TextSwitcher textLanguageLabel;
    @BindView(R.id.setting_text_language_value) TextSwitcher textLanguageValue;
    @BindView(R.id.setting_text_hide_content_detail_labe) TextSwitcher textHideContentDetailLabel;
    @BindView(R.id.setting_text_hide_content_detail_value) TextSwitcher textHideContentDetailValue;
    @BindView(R.id.setting_text_ad_interstitial_label) TextSwitcher textAdInterstitialLabel;
    @BindView(R.id.setting_text_ad_interstitial_value) TextSwitcher textAdInterstitialValue;
    @BindView(R.id.setting_text_notification_label) TextSwitcher textNotificationLabel;
    @BindView(R.id.setting_text_notification_value) TextSwitcher textNotificationValue;

    // share page
    @BindView(R.id.share_text_title) TextView textShareTitle;
    @BindView(R.id.share_text_hint) TextView textShareHint;
    @BindView(R.id.share_edittext_comment) EditText editTextShareComment;
    @BindView(R.id.share_button) Button buttonShare;

    // help page
    @BindView(R.id.help_text_title) TextView textHelpTitle;

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

        // feed
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textFeedContentTitle, R.dimen.text_default, R.color.colorPrimary, Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textFeedContentSubtitle, R.dimen.text_mini, R.color.colorThemeGrayDark);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textFeedContentText, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textFeedContentFooter, R.dimen.text_mini, R.color.colorThemeGrayDark, Gravity.END);

        // setting
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textSettingTitle, R.dimen.text_extra_big, R.color.colorPrimary);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textLanguageLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textLanguageValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textHideContentDetailLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textHideContentDetailValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAdInterstitialLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textAdInterstitialValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textNotificationLabel, R.dimen.text_small);
        LayoutUtil.getInstance().prepareTextSwitcher(thisContext, textNotificationValue, R.dimen.text_small, R.color.colorPrimary, Gravity.CENTER_VERTICAL | Gravity.END);
    }

    public void onFeedChangeStatus(FeedHelper.FeedStatus feedStatus) {
        onFeedChangeStatus(feedStatus, null);
    }

    public void onFeedChangeStatus(FeedHelper.FeedStatus feedStatus, String message) {
        onFeedChangeStatus(feedStatus, message, null);
    }

    public void onFeedChangeStatus(FeedHelper.FeedStatus feedStatus, String message, Content content) {
        if (feedStatus == null) {
            DebugUtil.getInstance().e(TAG, "FeedChangeStatus: feedStatus is empty");
            return;
        }

        DebugUtil.getInstance().v(TAG, "FeedChangeStatus: " + feedStatus + (content != null ? "|content:" + content : ""));

        switch (feedStatus) {
            case NO_NEED:
                onFeedChange(null);
                break;
            case START:
                LayoutUtil.getInstance().setVisibility(progressFeed, View.VISIBLE);
                break;
            case FAILED:
                LayoutUtil.getInstance().setVisibility(progressFeed, View.INVISIBLE);
                // TODO: 28/04/17 pake toast?
                if (message != null)
                    Toast.makeText(thisContext, message, Toast.LENGTH_SHORT).show();
                break;
            case SUCCESS:
                LayoutUtil.getInstance().setVisibility(progressFeed, View.INVISIBLE);
                if (content != null)
                    onFeedChange(content);
                break;
        }

        // TODO: 27-Apr-17
    }

    public void actionReload(View view) {
        feedHelper.get(true, true);
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

    public void actionSettingHideContentDetail(View view) {
        if (settingHoldChange)
            return;

        settingHoldChange = true;
        settingHelper.changeHideContentDetail();
        onLanguageChange(SettingHelper.Type.HIDE_CONTENT_DETAIL);
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

        // load new feed based on language
        feedHelper.get(true);
    }

    public void onLanguageChange(SettingHelper.Type type) {
        LanguageUtil.getInstance().updateLanguageResource(thisContext);

        if (type == null) {
            LayoutUtil.getInstance().setText(textAppName, getString(R.string.title));

            LayoutUtil.getInstance().setText(textFeedTitle, getString(R.string.feed_title));

            LayoutUtil.getInstance().setText(textReportTitle, getString(R.string.report_title));
            LayoutUtil.getInstance().setText(textReportNote, getString(R.string.report_note));
            LayoutUtil.getInstance().setText(textReportNoteFutureRelease, getString(R.string.report_note_future_release));
            LayoutUtil.getInstance().setText(buttonReport, getString(R.string.report_button_text));

            LayoutUtil.getInstance().setText(textSettingTitle, getString(R.string.setting_title));
            LayoutUtil.getInstance().setText(textLanguageLabel, getString(R.string.setting_language_label));
            LayoutUtil.getInstance().setText(textHideContentDetailLabel, getString(R.string.setting_hide_content_detail_label));
            LayoutUtil.getInstance().setText(textAdInterstitialLabel, getString(R.string.setting_ad_interstitial_label));
            LayoutUtil.getInstance().setText(textNotificationLabel, getString(R.string.setting_notification_label));

            LayoutUtil.getInstance().setText(textShareTitle, getString(R.string.share_title));
            LayoutUtil.getInstance().setText(textShareHint, getString(R.string.share_hint));
            LayoutUtil.getInstance().setText(editTextShareComment, getString(R.string.share_text_default));
            LayoutUtil.getInstance().setText(buttonShare, getString(R.string.share_button_text));

            LayoutUtil.getInstance().setText(textHelpTitle, getString(R.string.help_title));
        }

        if (type == null || type.equals(SettingHelper.Type.LANGUAGE)) {
            LayoutUtil.getInstance().setText(textLanguageValue, getString(
                    (PreferenceUtil.getInstance().getString(thisContext, getString(R.string.PREFERENCE_LANGUAGE), false).equals(getString(R.string.PREFERENCE_LANGUAGE_EN))) ?
                            R.string.PREFERENCE_LANGUAGE_EN_LABEL : R.string.PREFERENCE_LANGUAGE_IN_LABEL));
        }

        if (type == null || type.equals(SettingHelper.Type.HIDE_CONTENT_DETAIL)) {
            LayoutUtil.getInstance().setText(textHideContentDetailValue, getString(
                    !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_HIDE_CONTENT_DETAIL)) ?
                            R.string.yes : R.string.no));
            onFeedChange(null);
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

    private void onFeedChange(Content content) {

        if (content == null)
            content = SQLiteUtils.getInstance(thisContext).getContent();

        Boolean showContentDetail = !PreferenceUtil.getInstance().getBoolean(thisContext, getString(R.string.PREFERENCE_HIDE_CONTENT_DETAIL));

        if (showContentDetail && content != null && content.getTitle() != null) {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentTitle, true);
            LayoutUtil.getInstance().setText(textFeedContentTitle, content.getTitle());
            Log.e(TAG, "onFeedChange: "+content.getTitle());
        } else {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentTitle, false);
        }

        if (showContentDetail && content != null && content.getSubtitle() != null) {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentSubtitle, true);
            LayoutUtil.getInstance().setText(textFeedContentSubtitle, content.getSubtitle());
            Log.e(TAG, "onFeedChange: "+content.getSubtitle());
        } else {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentSubtitle, false);
        }

        if (content != null && content.getText() != null) {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentText, true);
            LayoutUtil.getInstance().setText(textFeedContentText, content.getText());
            Log.e(TAG, "onFeedChange: "+content.getText());
        } else {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentText, false);
        }

        if (showContentDetail && content != null && content.getFooter() != null) {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentFooter, true);
            LayoutUtil.getInstance().setText(textFeedContentFooter, content.getFooter());
        } else {
            LayoutUtil.getInstance().toggleVisibility(textFeedContentFooter, false);
        }
    }
}
