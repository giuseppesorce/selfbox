package com.docgenerici.selfbox.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.Random;

/**
 * @author Giuseppe Sorce
 */
public class MathUtils {

    public static int getMinMax(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();

        return Math.round(dp
                * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }


}
