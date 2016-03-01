package com.example.ashwin.hajeri.helperFunctions;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.example.ashwin.hajeri.R;

/**
 * Created by Ashwin on 11/19/2015.
 */
public class RandomColor {
    final Resources res;
    private final TypedArray colors;
    private final int NUM_OF_COLORS = 16;

    public RandomColor(Context context) {
        res = context.getResources();
        colors = res.obtainTypedArray(R.array.circle_colors);
    }

    public int getRandomColor(String num) {
        final int index = Math.abs(num.hashCode()) % NUM_OF_COLORS;
        try {
            return colors.getColor(index, R.color.color_app);
        } finally {
            colors.recycle();
        }

    }
}
