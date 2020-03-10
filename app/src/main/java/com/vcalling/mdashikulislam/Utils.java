package com.vcalling.mdashikulislam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {

    public static void statusBarColor(Activity activity, int color){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            activity.getWindow().setStatusBarColor(setColor(ContextCompat.getColor(activity,color)));

        }
    }

    // Code to darken the color supplied (mostly color of toolbar)
    private static int setColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.00f;
        return Color.HSVToColor(hsv);
    }
}
