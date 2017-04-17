package com.koceeng.freedonation.bottomsheet;

import android.app.Dialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.base.BaseBottomSheet;
import com.koceeng.freedonation.home.HomeActivity;
import com.koceeng.freedonation.util.LanguageUtil;
import com.koceeng.freedonation.util.PreferenceUtil;

public class LanguageBottomSheet extends BaseBottomSheet implements View.OnClickListener {

    private HomeActivity homeActivity;
    private RadioGroup radioGroup;

    public void setHomeActivity(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = View.inflate(getContext(), R.layout.setting_language, null);
        setContentView(dialog, view);

        if (PreferenceUtil.getInstance().getString(thisAppCompatActivity, thisAppCompatActivity.getString(R.string.PREFERENCE_LANGUAGE), false)
                .equals(getString(R.string.PREFERENCE_LANGUAGE_EN))) {
            ((RadioButton) view.findViewById(R.id.setting_language_radio_en)).setChecked(true);
        } else {
            ((RadioButton) view.findViewById(R.id.setting_language_radio_in)).setChecked(true);
        }

        radioGroup = (RadioGroup) view.findViewById(R.id.setting_language_radio_group);
        view.findViewById(R.id.setting_language_button_save).setOnClickListener(this);
        view.findViewById(R.id.setting_language_button_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_language_button_cancel :
                dismiss();
                break;
            case R.id.setting_language_button_save :
                inputFieldAction();
                break;
        }
    }

    @Override
    public void inputFieldAction() {
        super.inputFieldAction();

        if (thisAppCompatActivity == null || thisAppCompatActivity.isFinishing() || radioGroup == null) {
            dismiss();
            return;
        }

        String newLang = (radioGroup.getCheckedRadioButtonId() == R.id.setting_language_radio_en) ? getString(R.string.PREFERENCE_LANGUAGE_EN) : getString(R.string.PREFERENCE_LANGUAGE_IN);
        String currentLang = PreferenceUtil.getInstance().getString(thisAppCompatActivity, thisAppCompatActivity.getString(R.string.PREFERENCE_LANGUAGE));
        if (currentLang != null && newLang.equals(currentLang)) {
            dismiss();
            return;
        }

        LanguageUtil.getInstance().updateLanguageResource(thisAppCompatActivity, newLang);
        PreferenceUtil.getInstance().putString(thisAppCompatActivity, thisAppCompatActivity.getString(R.string.PREFERENCE_LANGUAGE), newLang);

        if (homeActivity != null && !homeActivity.isFinishing())
            homeActivity.onLanguageChange(null, true);

        dismiss();
    }
}
