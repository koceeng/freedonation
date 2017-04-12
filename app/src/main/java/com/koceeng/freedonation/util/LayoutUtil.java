package com.koceeng.freedonation.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.koceeng.freedonation.R;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class LayoutUtil {

    public void toggleVisibility(View view, Boolean isVisible) {
        int visibility = (isVisible) ? View.VISIBLE : View.GONE;
        setVisibility(view, visibility);
    }

    public void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public void setVisibleIfGone(View view) {
        if (view != null && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void setText(View view, CharSequence text) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            } else if (view instanceof TextSwitcher) {
                ((TextSwitcher) view).setText(text);
            }
        }
    }

    public void setCurrentText(View view, CharSequence text) {
        if (view != null && view instanceof TextSwitcher) {
            ((TextSwitcher) view).setCurrentText(text);
        }
    }

    public void setImageResource(View view, int imageResId) {
        if (view != null) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(imageResId);
            } else if (view instanceof ImageSwitcher) {
                ((ImageSwitcher) view).setImageResource(imageResId);
            }
        }
    }

    public Boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public void prepareTextSwitcher(final Context context, TextSwitcher textSwitcher) {
        prepareTextSwitcher(context, textSwitcher, null, null, null, null, null);
    }

    public void prepareTextSwitcher(final Context context, TextSwitcher textSwitcher, Integer size) {
        prepareTextSwitcher(context, textSwitcher, size, null, null, null, null);
    }

    public void prepareTextSwitcher(final Context context, TextSwitcher textSwitcher, Integer size,
                                    Integer gravity, String fontPath,
                                    Integer animationIn, Integer animationOut) {
        Log.e("NOTE", "prepareTextSwitcher: do");
        if (context == null || textSwitcher == null)
            return;

        Log.e("NOTE", "prepareTextSwitcher: do ok");
        if (size == null)
            size = R.dimen.text_default;
        if (gravity == null)
            gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        if (fontPath == null)
            fontPath = "fonts/default.ttf";
        if (animationIn == null)
            animationIn = R.anim.fast_transition_in;
        if (animationOut == null)
            animationOut = R.anim.fast_transition_out;

        final int finalSize = size;
        final int finalGravity = gravity;
        final String finalFontPath = fontPath;
        final int finalAnimationIn = animationIn;
        final int finalAnimationOut = animationOut;

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                TextView textView = new TextView(context);
                textView.setGravity(finalGravity);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(finalSize));
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorThemeWhite));
                CalligraphyUtils.applyFontToTextView(context, textView, finalFontPath);
                return textView;
            }
        });
        textSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, finalAnimationIn));
        textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, finalAnimationOut));
    }

    public void prepareImageSwitcher(final Context context, ImageSwitcher imageSwitcher) {
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(context);
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out));

    }

    private static LayoutUtil layoutUtil = null;

    public static LayoutUtil getInstance() {
        return layoutUtil == null ? new LayoutUtil() : layoutUtil;
    }
}
