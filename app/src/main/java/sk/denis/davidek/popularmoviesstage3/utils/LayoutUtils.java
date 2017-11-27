package sk.denis.davidek.popularmoviesstage3.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by denis on 16.11.2017.
 */

public class LayoutUtils {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }
}
