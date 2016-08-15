package fili.alex.newsradiot.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import fili.alex.newsradiot.App;
import fili.alex.newsradiot.R;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class AndroidHelper {
    private static Boolean isTablet = null;
    public static float density = 1;
    public static Point displaySize = new Point();
    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    static {
        density = App.appContext.getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
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

    public static String[] getStringArray(int id) {
        return App.appContext.getResources().getStringArray(id);
    }

    public static String getString(int id) {
        return App.appContext.getString(id);
    }

    public static Observable<Bitmap> loadBitmap(Picasso picasso, String imageUrl) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        subscriber.onError(new Exception("failed to load " + imageUrl));
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };
                subscriber.add(new Subscription() {
                    private boolean unSubscribed;

                    @Override
                    public void unsubscribe() {
                        picasso.cancelRequest(target);
                        unSubscribed = true;
                    }

                    @Override
                    public boolean isUnsubscribed() {
                        return unSubscribed;
                    }
                });
                picasso.load(imageUrl).into(target);
            }
        });
    }
}
