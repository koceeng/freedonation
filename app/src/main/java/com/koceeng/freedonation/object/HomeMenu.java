package com.koceeng.freedonation.object;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ViewFlipper;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LayoutUtil;

public class HomeMenu {

    public final String TAG = "HomeMenu";

    private View layout;
    private View indicator;
    private AppCompatImageView image;
    private View content;
    private View selectable;

    private ViewFlipper viewFlipper;

    public HomeMenu(View layout, View indicator, AppCompatImageView image, View content, View selectable) {
        this.layout = layout;
        this.indicator = indicator;
        this.image = image;
        this.content = content;
        this.selectable = selectable;
    }

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
    }

    public void setViewFlipper(ViewFlipper viewFlipper) {
        this.viewFlipper = viewFlipper;
    }

    public void toggleActive(Context context, boolean active, boolean withAnimation) {
        DebugUtil.getInstance().v(TAG, "toggleActive " + active);
        LayoutUtil.getInstance().toggleVisibility(indicator, active);
        image.setColorFilter(ContextCompat.getColor(context, (active ? R.color.colorThemeWhite : R.color.colorAccentDark)));

        if (withAnimation && active && viewFlipper != null) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(content));
        } else {
            LayoutUtil.getInstance().toggleVisibility(content, active);
        }

        if (selectable != null)
            selectable.requestFocus();
    }

    public View getLayout() {
        return layout;
    }

    public View getContent() {
        return content;
    }
}
