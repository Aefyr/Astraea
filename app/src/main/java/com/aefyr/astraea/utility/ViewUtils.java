package com.aefyr.astraea.utility;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.aefyr.astraea.R;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class ViewUtils {
    public static void highLightTV(Resources r, final TextView tv) {
        ValueAnimator colorAnimator = new ValueAnimator();
        colorAnimator.setIntValues(r.getColor(R.color.colorErrorText), r.getColor(R.color.colorText));
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(6);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setDuration(125);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tv.setTextColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimator.start();
    }

    public static void animatedTextSwap(final TextView textView, final String newText){
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, View.SCALE_Y, 1,0);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(50);
        animator.addListener(new Animator.AnimatorListener() {
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
                textView.setText(newText);
            }
        });
        animator.start();
    }

    public static void animateViewVisibilityChange(final View view, final boolean visible){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, visible?0:1,visible?1:0);
        animator.setDuration(100);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(visible)
                    view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!visible)
                    view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
