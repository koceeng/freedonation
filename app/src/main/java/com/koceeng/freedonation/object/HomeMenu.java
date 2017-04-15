package com.koceeng.freedonation.object;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

public class HomeMenu {

    private View layout;
    private View indicator;
    private AppCompatImageView image;
    private View content;

    public HomeMenu(View layout, View indicator, AppCompatImageView image, View content) {
        this.layout = layout;
        this.indicator = indicator;
        this.image = image;
        this.content = content;
    }

    public void toggleActive(Context context, boolean active) {
        LayoutUtil.getInstance().toggleVisibility(indicator, active);
        image.setColorFilter(ContextCompat.getColor(context, (active ? R.color.colorThemeWhite : R.color.colorAccent)));

        // TODO: 15/04/17 animate
        LayoutUtil.getInstance().toggleVisibility(content, active);
        // LayoutUtil.getInstance().setVisibility(content, (active ? View.VISIBLE : View.INVISIBLE));
        // content.animate().alpha(active ? 1.0f : 0.0f).setDuration(500);

//        if (content.getId() == R.id.feed_layout_parent) {
//            content.animate().translationY(active ? 0 : 500)
//                    .setDuration(500);
//        } else {
//            content.animate().translationY(active ? 0 : -500)
//                    .setDuration(500);
//        }

//        TranslateAnimation animate;
//        if (active) {
//            animate = new TranslateAnimation(0, 0, content.getHeight(), 0);
//        } else {
//            animate = new TranslateAnimation(0, 0, 0, content.getHeight());
//        }
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        content.startAnimation(animate);
//        content.setVisibility(View.GONE);
    }

    public View getLayout() {
        return layout;
    }

    public View getIndicator() {
        return indicator;
    }

    public AppCompatImageView getImage() {
        return image;
    }
}
