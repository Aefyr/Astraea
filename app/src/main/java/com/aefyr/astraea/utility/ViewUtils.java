package com.aefyr.astraea.utility;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
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
}
