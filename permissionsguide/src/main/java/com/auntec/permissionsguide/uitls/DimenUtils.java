package com.auntec.permissionsguide.uitls;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author Shrek
 * @date: 2016-07-29
 */
public class DimenUtils {

    public static float dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return  px;
    }

    public static float dpToPx(float dp, Context context){
        return dpToPx(dp,context.getResources());
    }

    public static int dpToPx(int dp, Context context){
        return (int)dpToPx(dp,context.getResources());
    }
}
