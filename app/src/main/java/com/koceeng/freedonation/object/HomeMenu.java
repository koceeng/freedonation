package com.koceeng.freedonation.object;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ViewFlipper;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

public class HomeMenu {

    private View layout;
    private View indicator;
    private AppCompatImageView image;
    private View content;
    private ViewFlipper viewFlipper;

    public HomeMenu(View layout, View indicator, AppCompatImageView image, View content) {
        this.layout = layout;
        this.indicator = indicator;
        this.image = image;
        this.content = content;
    }

    public HomeMenu(View layout, View indicator, AppCompatImageView image) {
        this.layout = layout;
        this.indicator = indicator;
        this.image = image;
        this.content = null;
    }

    public void setViewFlipper(ViewFlipper viewFlipper) {
        this.viewFlipper = viewFlipper;
    }

    public void toggleActive(Context context, boolean active, boolean withAnimation) {
        Log.e("NOTE", "toggleActive " + active);
        LayoutUtil.getInstance().toggleVisibility(indicator, active);
        image.setColorFilter(ContextCompat.getColor(context, (active ? R.color.colorThemeWhite : R.color.colorAccentDark)));

        if (withAnimation && active && viewFlipper != null) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(content));
        } else {
            LayoutUtil.getInstance().toggleVisibility(content, active);
        }
    }

    public View getLayout() {
        return layout;
    }

    public View getContent() {
        return content;
    }
}
