package com.aefyr.astraea.utility;

import android.graphics.Color;

/**
 * Created by Aefyr on 11.09.2017.
 */

public class MathUtils {
    public static int lerp(int a, int b, float t) {
        return (int) (a + ((float) (b - a) * t));
    }

    public static int colorLerp(int color1, int color2, float t) {
        return Color.rgb(lerp(Color.red(color1), Color.red(color2), t), lerp(Color.green(color1), Color.green(color2), t), lerp(Color.blue(color1), Color.blue(color2), t));
    }

    public static int vector1Distance(int x, int x1) {
        return Math.abs(x1 - x);
    }
}
