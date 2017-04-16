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

    public void setViewFlipper(ViewFlipper viewFlipper) {
        this.viewFlipper = viewFlipper;
    }

    public void toggleActive(Context context, boolean active, boolean withAnimation) {
        Log.e("NOTE", "toggleActive " + active);
        LayoutUtil.getInstance().toggleVisibility(indicator, active);
        image.setColorFilter(ContextCompat.getColor(context, (active ? R.color.colorThemeWhite : R.color.colorAccent)));

        if (withAnimation && active && viewFlipper != null) {
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(content));
        } else {
            LayoutUtil.getInstance().toggleVisibility(content, active);
        }
        // TODO: 15/04/17 animate
//        LayoutUtil.getInstance().toggleVisibility(content, active);
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
//        Log.e("NOTE", "toggleActive: " + content.getHeight());
//        if (active) {
//            animate = new TranslateAnimation(0, 0, content.getHeight(), 0);
//            LayoutUtil.getInstance().setVisibility(content, View.INVISIBLE);
//        } else {
//            animate = new TranslateAnimation(0, 0, 0, content.getHeight());
//            animate.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    LayoutUtil.getInstance().setVisibility(content, View.GONE);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//        }
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//
//        content.startAnimation(animate);
//        content.setVisibility(View.GONE);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public View getLayout() {
        return layout;
    }
}
