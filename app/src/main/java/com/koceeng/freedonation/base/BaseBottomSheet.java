package com.koceeng.freedonation.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.koceeng.freedonation.util.LanguageUtil;

public class BaseBottomSheet extends BottomSheetDialogFragment {

    protected AppCompatActivity thisAppCompatActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisAppCompatActivity = (AppCompatActivity) getActivity();

        LanguageUtil.getInstance().updateLanguageResource(thisAppCompatActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (thisAppCompatActivity == null || thisAppCompatActivity.isFinishing())
            dismiss();
    }

    protected BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
                dismiss();
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
    };

    protected void setContentView(Dialog dialog, View view) {
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }
    }

    public void inputFieldAction() {

    }
}
