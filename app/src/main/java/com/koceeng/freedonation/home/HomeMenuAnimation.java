package com.koceeng.freedonation.home;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HomeMenuAnimation extends Animation {

    private int layoutFakeHeightFirst = 0;
    private int layoutFakeHeightActual = 0;

    private int initialHeight;
    private View layout;
    private Boolean isReveal;

    public HomeMenuAnimation(View layout) {
        this.layout = layout;

        if (layout != null && ViewCompat.isAttachedToWindow(layout)) {
            layoutFakeHeightFirst = layout.getHeight();
            layoutFakeHeightActual = layout.getHeight();
        }
    }

    public void setIsReveal(Boolean isReveal) {
        this.isReveal = isReveal;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;

        if (isReveal) {
            newHeight = (int) (initialHeight - (initialHeight * interpolatedTime));
        } else {
            newHeight = (int) (layoutFakeHeightFirst * interpolatedTime);
            if (interpolatedTime == 1)
                layout.setVisibility(View.VISIBLE);
        }

        layout.getLayoutParams().height = newHeight;
        layout.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        initialHeight = layoutFakeHeightActual;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
