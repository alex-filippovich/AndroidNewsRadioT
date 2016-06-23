package fili.alex.newsradiot.util;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import fili.alex.newsradiot.App;
import fili.alex.newsradiot.R;

public class AndroidHelper {
    private static Boolean isTablet = null;
    public static float density = 1;
    public static Point displaySize = new Point();
    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    static {
        density = App.appContext.getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }


    public static boolean isTablet() {
        if (isTablet == null) {
            isTablet = App.appContext.getResources().getBoolean(R.bool.isTablet);
        }
        return isTablet;
    }

    public static boolean isSmallTablet() {
        float minSide = Math.min(displaySize.x, displaySize.y) / density;
        return minSide <= 700;
    }

    public static boolean isLandscape() {
        return App.appContext.getResources().getBoolean(R.bool.isLand);
    }


    public static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager) App.appContext.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    if (android.os.Build.VERSION.SDK_INT < 13) {
                        displaySize.set(display.getWidth(), display.getHeight());
                    } else {
                        display.getSize(displaySize);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("AndroidHelper", e.getMessage());
        }
    }
}
