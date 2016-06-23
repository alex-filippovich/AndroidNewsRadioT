package fili.alex.newsradiot;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    public static volatile Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
    }
}
