package com.aefyr.astraea.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class AnimatedTextView extends android.support.v7.widget.AppCompatTextView {
    public AnimatedTextView(Context context) {
        super(context);
        initialize();
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }


    private ObjectAnimator textSwapper;
    private String newText;

    private void initialize() {
        textSwapper = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1, 0);
        textSwapper.setRepeatCount(1);
        textSwapper.setRepeatMode(ValueAnimator.REVERSE);
        textSwapper.setDuration(50);
        textSwapper.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                setText(newText);
            }
        });
    }

    public void setText(final String text, boolean animate) {
        if (text.equals(getText()))
            return;

        if (!animate) {
            setText(text);
            return;
        }
        newText = text;
        if (textSwapper.isRunning() && textSwapper.getCurrentPlayTime() >= 50)
            textSwapper.end();

        textSwapper.start();
    }
}
